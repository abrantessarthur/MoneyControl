# Money Control — frontend

Interface React/Vite integrada à API Spring Boot do projeto.

## Rodar localmente

1. Inicie o backend na porta `8080`.
2. Nesta pasta, execute `pnpm install`.
3. Execute `pnpm dev`.
4. Abra `http://localhost:5173`.

Durante o desenvolvimento, chamadas para `/api` são encaminhadas pelo Vite para `http://localhost:8080`. Para apontar para outra API, copie `.env.example` para `.env` e altere `VITE_API_URL`.

O botão **Explorar com dados demonstrativos** abre o painel sem autenticação e sem persistir registros.
