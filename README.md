# Web Chat Application

Web Chat is a secure, distributed real-time messaging platform built to enable seamless communication between users with end-to-end encryption (E2E) for privacy. The application supports user registration, authentication (including Google OAuth), contact management, and live chat functionality. Built with Spring Boot on the backend and React on the frontend, this application adheres to distributed architecture principles to ensure scalability, reliability, and efficient resource utilization.

## Features

- **Real-time Messaging**: Instant chat between users with read receipts and message status updates.
- **End-to-End Encryption (E2E)**: Secure communication through encrypted message payloads.
- **User Management**: User registration, login, role-based authorization, and Google OAuth integration.
- **Contact Management**: User contact list with easy lookup and management.
- **Monitoring & Observability**: Integrated with Prometheus and Grafana for real-time monitoring and insights.
- **Scalability and Reliability**: Distributed architecture to support high availability and load distribution.

## Tech Stack

- **Backend**: Spring Boot (Java), PostgreSQL, Prometheus, Grafana
- **Frontend**: HTML, CSS, JavaScript
- **Authentication**: Google OAuth, JWT
- **Monitoring**: Prometheus for metrics, Grafana for visualization

## Getting Started

1. **Prerequisites**: Ensure Docker and Docker Compose are installed.
2. **Clone the Repository**:
   ```bash
   git clone https://github.com/akhrullo/web-chat
   cd web-chat
3. **Start the Application:**:
   ```bash
   docker-compose up -d

