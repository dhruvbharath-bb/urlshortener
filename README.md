# URLShortener (V1)

A backend URL shortening service built with Spring Boot and MongoDB.

This project implements the core functionality of a URL shortener with clean layering, correct HTTP semantics, and a design aligned with the original HLD.

---

## What This Project Does (V1)
* **Create Short URLs**: Generates unique short codes for long URLs.
* **Redirection**: Redirects short URLs to the original destination.
* **Expiration Enforcement**: Checks if a URL is expired during the redirect.
* **Base62 Encoding**: Generates unique, readable short codes.
* **Persistence**: Uses MongoDB for data storage.
* **Clean Architecture**: Follows separation between controller, service, and domain layers.

---

## Tech Stack
* **Java 17**
* **Spring Boot**
* **Spring Data MongoDB**
* **MongoDB** (Docker for local development)
* **Gradle**

---

## High-Level Architecture
The application is stateless and horizontally scalable.

Standard Flow: Client -> Controller (HTTP concerns) -> Service (Business rules) -> Repository (MongoDB)

### Project Structure
* **api**: Handles HTTP endpoints and request/response DTOs.
* **domain**: Core business concepts (UrlMapping, redirect resolution results).
* **service**: Business logic and orchestration (URL creation, redirect resolution).
* **repository**: MongoDB repositories.
* **util**: Stateless utilities (Base62 encoder).

---

## Core Domain Concepts

### UrlMapping
Represents a shortened URL and its lifecycle. Includes shortCode, longUrl, createdAt, and optional expiresAt.

### RedirectResult
A value object representing the outcome of resolving a short URL. States include:
* FOUND
* NOT_FOUND
* EXPIRED

Note: This design keeps business logic out of the controller and avoids leaking HTTP concerns into the service layer.

---

## API Overview

### 1. Create Short URL
**POST** `/api/v1/shorten`
* Accepts a long URL and optional expiry duration.
* Returns a generated short URL.

### 2. Redirect
**GET** `/{shortCode}`
* Valid short code: HTTP 302 redirect
* Expired short code: HTTP 410 Gone
* Unknown short code: HTTP 404 Not Found

Note: Redirect endpoints are root-level (not under /api) to match real-world URL shortener behavior.

---

## Persistence and Expiration
* **Expiration**: Enforced at redirect time. (Physical cleanup via TTL index is deferred to a later version).
* **ID Generation**: Uses an atomic find-and-modify pattern in the counters collection to ensure uniqueness under concurrency.

---

## Testing (V1)
* **Included**: Unit tests for Base62 logic and manual end-to-end testing via browser or curl.
* **Deferred**: Controller integration tests and embedded MongoDB tests are planned for future versions to keep V1 focused on architecture.

---

## Local Development
1. Ensure MongoDB is running via Docker.
2. The application runs locally on port 8080.

---

## Roadmap

### Out of Scope for V1
* Redis caching
* Analytics and hit counting
* Asynchronous processing (Kafka)
* Rate limiting

### Future Iterations
* **V2**: Redis caching for redirects.
* **V3**: Analytics and async processing.
* **V4**: Rate limiting and hardening.

---

## V1 Status: Complete
Includes full create/redirect flow, correct HTTP semantics, and clean domain modeling aligned with the HLD.
