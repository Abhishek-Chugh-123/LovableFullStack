# LovableFullStack 🚀

AI-powered SaaS platform where users generate full React apps from natural-language prompts via real-time LLM streaming (SSE). Each project runs in an isolated Kubernetes pod (Vite + MinIO syncer sidecar), routed via a Node.js reverse proxy using Redis. Includes JWT + RBAC, Stripe webhook subscriptions, token quota tracking, and pgvector AI context injection.

## Tech Stack
| Layer | Tech |
|-------|------|
| Backend | Spring Boot 4.0, Spring AI, Spring Security |
| Frontend | React, Vite, TypeScript |
| Infrastructure | Kubernetes, Docker, Fabric8 |
| Storage | MinIO, PostgreSQL + pgvector |
| Routing | Node.js Reverse Proxy, Redis |
| Payments | Stripe Webhooks |
| Auth | JWT, RBAC |

## Architecture
- **AI Layer** — Spring AI streams LLM responses via SSE with pgvector-backed context injection
- **Runner Pool** — Each project gets an isolated K8s pod (Vite dev server + MinIO syncer sidecar)
- **Reverse Proxy** — Node.js proxy resolves project subdomains to live pod IPs via Redis
- **Payments** — Full Stripe webhook lifecycle (checkout, renewal, cancellation, payment failure)

  <img width="1917" height="968" alt="Screenshot 2026-05-04 123818" src="https://github.com/user-attachments/assets/16677f70-384e-4ead-97ab-7f5240c8eaef" />
<img width="1912" height="1019" alt="Screenshot 2026-05-04 123800" src="https://github.com/user-attachments/assets/6f8f3643-3cfa-451f-bc34-4f1ef7c8b1d9" />
<img width="1706" height="914" alt="Screenshot 2026-05-04 122921" src="https://github.com/user-attachments/assets/93b11a80-a5cb-4033-8e7c-e0b4e8b27e2b" />
<img width="1912" height="1014" alt="Screenshot 2026-05-04 123253" src="https://github.com/user-attachments/assets/a3516547-a6ca-45f0-a379-756980b0b62f" />


<img width="1919" height="1030" alt="Screenshot 2026-05-04 123304" src="https://github.com/user-attachments/assets/94f665b1-dbfa-4481-aaaf-5883bfb722b2" />

 <img width="1918" height="1028" alt="Screenshot 2026-05-04 123520" src="https://github.com/user-attachments/assets/d260445a-5e72-499e-9905-108cf8039a2d" />
 
