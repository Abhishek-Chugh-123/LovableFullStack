package com.codingshuttle.projects.lovable_clone.service.impl;

import com.codingshuttle.projects.lovable_clone.dto.project.FileContentResponse;
import com.codingshuttle.projects.lovable_clone.dto.project.FileNode;
import com.codingshuttle.projects.lovable_clone.dto.project.FileTreeResponse;
import com.codingshuttle.projects.lovable_clone.entity.Project;
import com.codingshuttle.projects.lovable_clone.entity.ProjectFile;
import com.codingshuttle.projects.lovable_clone.error.ResourceNotFoundException;
import com.codingshuttle.projects.lovable_clone.mapper.ProjectFileMapper;
import com.codingshuttle.projects.lovable_clone.repository.ProjectFileRepository;
import com.codingshuttle.projects.lovable_clone.repository.ProjectRepository;
import com.codingshuttle.projects.lovable_clone.service.ProjectFileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectFileServiceImpl implements ProjectFileService {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final MinioClient minioClient;
    private final ProjectFileMapper projectFileMapper;

    @Value("${minio.project-bucket}")
    private String projectBucket;

    private static final String BUCKET_NAME = "projects";

    @Override
    public FileTreeResponse getFileTree(Long projectId) {
        List<ProjectFile> projectFileList = projectFileRepository.findByProject_Id(projectId);
        List<FileNode> projectFileNodes = projectFileMapper.toListOfFileNode(projectFileList);
        return new FileTreeResponse(projectFileNodes);
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path) {
        String objectName = projectId + "/" + path;
        try (
                InputStream is = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(objectName)
                                .build())) {

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return new FileContentResponse(path, content);
        } catch (Exception e) {
            log.error("Failed to read file: {}/{}", projectId, path, e);
            throw new RuntimeException("Failed to read file content", e);
        }
    }

    @Override
    public void saveFile(Long projectId, String path, String content) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project", projectId.toString())
        );

        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String objectKey = projectId + "/" + cleanPath;

        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(contentBytes);

            // Upload file to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(projectBucket)
                            .object(objectKey)
                            .stream(inputStream, contentBytes.length, -1)
                            .contentType(determineContentType(path))
                            .build());

            // Save metadata
            ProjectFile file = projectFileRepository.findByProject_IdAndPath(projectId, cleanPath)
                    .orElseGet(() -> ProjectFile.builder()
                            .project(project)
                            .path(cleanPath)
                            .createdAt(Instant.now())
                            .build());

            file.setMinioObjectKey(objectKey);
            file.setUpdatedAt(Instant.now());

            projectFileRepository.saveAndFlush(file);

            log.info("Saved file: {}", objectKey);

        } catch (Exception e) {
            log.error("Failed to save file {}/{}", projectId, cleanPath, e);
            throw new RuntimeException("File save failed", e);
        }
    }

    // ================================
    // 🚀 NEW METHOD (IMPORTANT FIX)
    // ================================

    public void uploadProjectFolder(File folder, Long projectId) {
        uploadFolderRecursive(folder, projectId, "");
    }

    private void uploadFolderRecursive(File folder, Long projectId, String basePath) {

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {

            String newPath = basePath.isEmpty()
                    ? file.getName()
                    : basePath + "/" + file.getName();

            if (file.isDirectory()) {
                uploadFolderRecursive(file, projectId, newPath);
            } else {
                try (InputStream is = new FileInputStream(file)) {

                    String objectKey = projectId + "/" + newPath;

                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(projectBucket)
                                    .object(objectKey)
                                    .stream(is, file.length(), -1)
                                    .contentType(determineContentType(file.getName()))
                                    .build()
                    );

                    log.info("Uploaded: {}", objectKey);

                } catch (Exception e) {
                    log.error("Upload failed: {}", file.getName(), e);
                }
            }
        }
    }

    private String determineContentType(String path) {
        String type = URLConnection.guessContentTypeFromName(path);
        if (type != null) return type;
        if (path.endsWith(".jsx") || path.endsWith(".ts") || path.endsWith(".tsx")) return "text/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".css")) return "text/css";
        return "text/plain";
    }
}