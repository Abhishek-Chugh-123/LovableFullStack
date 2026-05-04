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
<img width="1918" height="1028" alt="Screenshot 2026-05-04 123520" src="https://github.com/user-attachments/assets/8558d832-879a-4f0e-8183-40b9ca0d1822" />
<img width="1706" height="914" alt="Screenshot 2026-05-04 122921" src="https://github.com/user-attachments/assets/20cb1fb0-d0b3-4c62-8639-b17a3f335be6" />
<img width="1917" height="1028" alt="Screenshot 2026-05-03 160114" src="https://github.com/user-attachments/assets/3085809e-c2e4-4882-a5a4-a667030c7aa0" />
<img width="1910" height="802" alt="Screenshot 2026-05-01 172808" src="https://github.com/user-attachments/assets/436af65d-6a17-4371-aaf5-5f8ca33ff4ee" />
<img width="1917" height="1023" alt="Screenshot 2026-05-04 123353" src="https://github.com/user-attachments/assets/9f7deab0-b281-4557-bf70-9a90e9ec147a" />
<img width="1919" height="1030" alt="Screenshot 2026-05-04 123304" src="https://github.com/user-attachments/assets/a0e1b74c-eeab-4e01-aff3-3306778b3aa1" />
<img width="1912" height="1014" alt="Screenshot 2026-05-04 123253" src="https://github.com/user-attachments/assets/e27f8a56-0fef-405c-903d-172cfe2e07d4" />
<img width="1911" height="921" alt="Screenshot 2026-05-01 172800" src="https://github.com/user-attachments/assets/a83d8020-c91b-42ac-b031-e5f5916449d7" />
