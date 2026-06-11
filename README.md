# nullcanvas

A monolithic blog application built with Jakarta EE and Spring Boot, providing a robust content management system with
flexible content delivery options.

## Overview

nullcanvas is a full-featured blog platform that supports both full content retrieval and chunked content delivery,
making it suitable for handling blog posts of varying sizes efficiently. The application includes a robust authentication
and authorization system to secure content and manage user access.

## Tech Stack

- **Jakarta EE** - Enterprise Java platform
- **Spring Boot** - Application framework
- **Spring Security** - Authentication and authorization
- **OAuth 2.0** - Third-party authentication
- **Jackson (tools.jackson)** - JSON processing
- **Lombok** - Boilerplate code reduction
- **UUID** - Unique identifier generation

## Features

- **Dual Content Delivery Modes**
    - Full content retrieval for complete blog posts
    - Chunked content delivery for paginated reading experience

- **Content Management**
    - Complete writer workflow for blog creation and editing
    - Draft and publish capabilities
    - Content versioning and updates
    - Metadata management

- **Rich Metadata Support**
    - Title, summary, and slug management
    - Cover image URL support
    - Visibility and status controls
    - Content size tracking
    - Creation timestamps

- **Flexible Content Storage**
    - JSON-based content storage
    - Dynamic content parsing
    - Structured content retrieval

- **Authentication & Authorization**
    - OAuth 2.0 integration for third-party authentication
      - Secure user authentication
    - Role-based access control
    - Protected endpoints
    - Session management

## Architecture

The application follows a layered architecture with clear separation of concerns:

### Service Layer

- **BlogContentReaderOrchestrator** - Coordinates content reading operations
- **FullContentReader** - Handles complete blog content retrieval
- **ChunkContentReader** - Manages paginated content delivery
- **BlogWriterService** - Manages blog creation, updates, and writer workflow
- **AuthService** - Manages authentication and authorization operations
- **OAuthService** - Handles OAuth 2.0 authentication flow

### DTO Layer

- `FullContentResponse` - Complete blog content with metadata
- `ChunkContentResponse` - Paginated content chunk with navigation info
- `MetadataResponse` - Blog metadata information

### Repository Layer

- `BlogRepository` - Data access layer for blog entities

## Design Patterns

The application leverages several design patterns to ensure maintainability, flexibility, and clean code:

### Strategy Pattern
Used in the content reading system where `FullContentReader` and `ChunkContentReader` provide different strategies for content retrieval. This allows the application to switch between full content delivery and chunked delivery seamlessly.

### Orchestrator Pattern

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Your preferred IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

1. Clone the repository:
