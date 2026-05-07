package com.codingshuttle.projects.lovable_clone.service;


import com.codingshuttle.projects.lovable_clone.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {

    List<ChatResponse> getProjectChatHistory(Long projectId);
}
