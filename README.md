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
