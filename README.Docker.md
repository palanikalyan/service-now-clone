# Request Management System - Docker Compose Guide

This project uses Docker Compose to run both the Angular frontend and Spring Boot backend together.

## Prerequisites
- Docker and Docker Compose installed (WSL recommended for Windows)
- Source code for both frontend (`request-management-ui`) and backend (`service-now`) in the same root directory

## How to Build and Run

1. **Open a terminal in the project root directory** (where `docker-compose.yaml` is located).
2. **Build and start all services:**
   ```sh
   docker compose up --build
   ```
   This will build and start both the frontend and backend containers.

3. **Access the applications:**
   - Frontend: [http://localhost:4200](http://localhost:4200)
   - Backend: [http://localhost:8080](http://localhost:8080)

## How to Stop
```sh
docker compose down
```

## How to Transfer Files (WSL)
- You can use `cp`, `mv`, or drag-and-drop in your WSL file explorer.
- To copy files from Windows to WSL, use:
  ```sh
  cp /mnt/c/Users/Asus/Downloads/rm/filename /home/youruser/
  ```
- To copy files from WSL to Docker container:
  ```sh
  docker cp localfile service-now:/app/
  ```

## Notes
- Make sure your backend is accessible to the frontend (CORS settings).
- You can customize ports in `docker-compose.yaml` as needed.
- For production, use Nginx for frontend and set proper environment variables for backend.

---
For more details, see the Dockerfiles in each project folder.
