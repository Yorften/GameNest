# GameNest Platform

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/Yorften/GameNest/actions)

GameNest is a platform designed to allow developers to easily upload, integrate, automatically build, and deploy their Godot engine games, making them accessible to players. This initial version focuses on hosting the creator's personal games. The project is inspired by platforms like itch.io.

## Table of Contents

- [Features](#features)
  - [Frontend](#frontend)
  - [Backend & Build System](#backend--build-system)
- [Technologies](#technologies)
- [Architecture Overview](#architecture-overview)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Local Setup (Docker Compose)](#local-setup-docker-compose)
  - [Access](#access)
  - [Configuration](#configuration)
- [Notes](#notes)

## Features

### Frontend

- User Registration and secure Login.
- Game catalog Browse.
- Directly play hosted Godot games within the platform.
- User Dashboard for managing games and builds.

### Backend & Build System

- Centralized API for managing users, games, builds, tags, and categories.
- Secure endpoints using Spring Security.
- Integration with GitHub Apps/Webhooks to trigger automated Godot builds upon code pushes.
- Build service that clones repositories, injects presets, runs Godot export (headless), and stores build artifacts.
- Real-time build logs (potentially via WebSockets).
- Admin Dashboard for managing platform metadata (tags, categories).
- Game and build management accessible to users/admins.
- Validation and sanitization of game data.

## Technologies

- **Frontend:** React, Vite, Tailwind CSS
- **Backend:** Java 17, Spring Boot 3, Spring Security, JWT, JGit, github-api (JAVA)
- **Game Engine:** Godot Engine (v4.2.2)
- **Database:** PostgreSQL (Production), H2 (Development)
- **Build/CI:** Godot Headless Export, GitHub API/Apps, GitHub Actions
- **Deployment:** Docker, Docker Compose, Nginx

## Architecture Overview

- **Monorepo Structure:** Frontend, Backend, Nginx config likely in one main repository.
- **Backend API:** A Spring Boot application providing RESTful endpoints for all data management and triggering builds.
- **Frontend:** A React Single Page Application (SPA) interacting with the backend API.
- **Game Serving:** Nginx acts as a reverse proxy and static file server, serving both the React frontend and the exported Godot game builds from the same origin.
- **Game Builds:** Triggered by GitHub events or user action, handled by the backend service which runs Godot headless builds inside its Docker container. Build artifacts are stored in a shared Docker volume accessible by Nginx.
- **Game Display:** Games are embedded as `iframe` elements within the React frontend. Cross-Origin Isolation headers (COOP/COEP) are managed by Nginx to enable features like `SharedArrayBuffer`.

## Project Structure

```
. (Project Root)
├── .env                      # (For local secrets/config used by Docker Compose)
├── .github/
│   └── workflows/            # GitHub Actions CI/CD workflows
├── docker-compose.yml        # (Defines services, networks, volumes)
├── frontend/                 # React Frontend Application
│   ├── public/
│   │   └── assets/
│   │       └── images/       # Static images served directly
│   ├── src/                  # Frontend source code
│   │   ├── app/              # Core app setup (store, router, etc.)
│   │   ├── components/       # Reusable UI components
│   │   │   ├── guards/       # Route guards (e.g., auth)
│   │   │   ├── layouts/      # Page layout components (e.g., header, sidebar)
│   │   │   └── miscs/        # Miscellaneous shared components
│   │   ├── features/         # Feature-specific modules/slices (Redux Toolkit style)
│   │   │   ├── auth/
│   │   │   ├── builds/
│   │   │   ├── categories/
│   │   │   ├── games/
│   │   │   ├── repositories/
│   │   │   └── tags/
│   │   ├── hooks/            # Custom React hooks
│   │   ├── pages/            # Top-level page components
│   │   │   └── dashboard/    # Pages specific to the dashboard section
│   │   └── types/            # TypeScript type definitions
│
├── gamenest/                 # Backend Application (Spring Boot)
│   ├── Dockerfile            # (Builds the backend Docker image)
│   ├── export/               # Contains export_presets.cfg for Godot builds
│   ├── logs/                 # Log directory used *inside* the container if mapped from host
│   ├── pom.xml               # (Assumes Maven, could be build.gradle)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── gamenest/ # Main application package
│   │   │   │           ├── config/       # Spring configuration classes
│   │   │   │           ├── controller/   # REST API Controllers
│   │   │   │           │   ├── auth/
│   │   │   │           │   └── webhook/
│   │   │   │           ├── dto/          # Data Transfer Objects
│   │   │   │           ├── events/       # Application events and listeners (e.g., GameBuildEvent)
│   │   │   │           ├── exception/    # Custom exceptions and handlers
│   │   │   │           │   └── handler/
│   │   │   │           ├── mapper/       # MapStruct or similar mappers
│   │   │   │           ├── model/        # JPA Entities / Domain models
│   │   │   │           │   └── enums/
│   │   │   │           ├── repository/   # Spring Data JPA repositories
│   │   │   │           ├── service/      # Business logic layer
│   │   │   │           │   ├── implementation/
│   │   │   │           │   └── interfaces/
│   │   │   │           ├── util/         # Utility classes
│   │   │   │           └── validation/   # Custom validation logic
│   │   │   └── resources/          # Non-Java resources (config, templates)
│   │   │       ├── application.yaml # (Main Spring Boot config)
│   │   │       └── META-INF/        # Standard Java META-INF directory
│   │   └── test/                 # Test sources
│   │       ├── java/
│   │       │   └── unit/
│   │       └── resources/
│   └── target/               # Maven/Gradle build output (contains JAR)
├── logs/                     # Top-level logs directory (maybe redundant?)
└── nginx/                    # Nginx configuration and Docker setup
    ├── Dockerfile            # (Builds Nginx image with frontend)
    └── nginx.conf            # (Nginx configuration file)
```

## Getting Started

### Prerequisites

Ensure you have the following software installed on your local machine:

- [Docker](https://www.docker.com/products/docker-desktop/) & Docker Compose (Usually included with Docker Desktop)
- [Git](https://git-scm.com/downloads)
- A text editor or IDE (like VS Code)
- Access to a command line/terminal
- A configured GitHub App with its App ID and Private Key file downloaded.

_(Optional for local non-Docker builds/development)_
_(You will need to setup a nginx server to act as a bucket for game builds, you have to change the CORP to "cross-origin" from "same-site")_

- Maven or Gradle (matching the backend project)
- Node.js (v20+) and npm (matching the frontend project)
- Java JDK (v17)
- PostgreSQL client

### Configuration

Application configuration is managed primarily through:

1.  **`.env` File:** Located at the project root, used by Docker Compose to inject environment variables into the services during startup and build args during build time. This is ideal for secrets and environment-specific settings.
2.  **`docker-compose.yml`:** Defines services, volumes, networks, ports, dependencies, healthchecks, and sets environment variables/build arguments for containers.
3.  **`application.yaml` (Backend):** Spring Boot configuration file located in `gamenest/src/main/resources`. It reads values from environment variables defined in `docker-compose.yml` (using `${VAR_NAME:defaultValue}` syntax).
4.  **`nginx.conf` (Frontend/Proxy):** Nginx configuration file located in `nginx/`. It defines how the frontend and game builds are served. Build-time variables for the frontend are baked in during the Docker build process.

**_Important_**
    You must configure your Github Application with the following:
    - **Prmisisons**: Contents (Read-only) / Metadata (Read-only)
    - **Webhook**: Make sure to activate Webhooks and setup a secret phrase `SPRING_GITHUB_WEBHOOK_SECRET`
    - **Callback**: Configure the callback url `<your-frontend-url>/callback`
    - **Use a proxy**: I recommend using [Ngrok](https://ngrok.com/downloads/window/)
    ```bash
    ngrok http --url=<your-ngrok-url> 8085
    ```
    - **Webhook URL**: Don't forget to configure your Webhook URL `https://<your-ngrok-url>/api/v1/webhook`

### Local Setup (Docker Compose)

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/Yorften/Ashbound
    cd Ashbound
    ```

2.  **Configure Environment:**

    - First you need to convert your github app secret to a friendly Java format (from .pem to der):
      ```bash
      openssl pkcs8 -topk8 -inform PEM -outform DER -in <path to your secret> -out <output path> -nocrypt
      ```
    - Locate or create the `.env` file in the project root directory. If an `.env.example` file exists, copy it to `.env`.
    - Edit the `.env` file and fill in the required environment variables. Key variables include:
      - `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB` (for the database container)
      - `SPRING_JWT_SECRET`, `SPRING_JWT_EXPIRATION`
      - `SPRING_GITHUB_WEBHOOK_SECRET`
      - `SPRING_GITHUB_APP_ID`
      - `VITE_API_URL=http://localhost:8085/api/v1`
      - `VITE_GITHUB_APP_URL=https://github.com/apps/<your-github-application>` _(Note: This is used at build time)_
      - `VITE_GAME_BUILDS_URL=/builds` _(Note: This is used at build time)_
    - **Important:** Open the `docker-compose.yml` file. Locate the `volumes` section under the `gamenest-app` service and update the host path for the GitHub private key mount to point to the actual location of your `.der` key file on your machine:
      ```yaml
      volumes:
        # ... other volumes ...
        # Update the path on the left to your key's location
        - /path/on/your/host/machine/your-key-file.der:/app/secrets/github-key.der:ro
      ```

3.  **Build and Run Containers:**
    Open a terminal in the project root directory and run:
    ```bash
    docker compose up -d --build
    ```
    - This command will:
      - Build the backend image (installing Godot).
      - Build the Nginx image (installing frontend dependencies, building the React app with Vite, setting up Nginx).
      - Start the PostgreSQL database, backend, and Nginx containers.
      - The Nginx container will wait for the backend to report healthy (via Actuator) before fully starting.

### Access

Once the containers are up and running:

- **Frontend Application:** Open your web browser and navigate to `http://localhost:81` (or the host port you mapped to container port 80 in `docker-compose.yml`).
- **Backend API (Directly):** The API is available at `http://localhost:8085` (or the host port mapped to container port 8085). You can use tools like Postman or `curl` to interact with it if needed.
- **Database:** Can be accessed on `localhost:5432` using the credentials from your `.env` file if you need to connect directly with a DB client.

## Notes

- The list of available repositories is filtered based on GitHub's language detection, showing only repositories where GDScript is identified as the primary language. After creating a new repository or pushing significant GDScript code, there might be a delay before GitHub completes its analysis and classifies the language, potentially excluding the repository from the filtered list temporarily.
- For local non-Docker environment, you will need to set up an environment variable **GODOT_PATH** pointing to **the executable (important)** 