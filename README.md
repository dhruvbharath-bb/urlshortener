URLShortener (V1)

A backend URL shortening service built with Spring Boot and MongoDB.
This project implements the core functionality of a URL shortener with clean layering, correct HTTP semantics, and a design aligned with the original HLD.

What This Project Does (V1)

Creates short URLs for long URLs

Redirects short URLs to the original URL

Enforces URL expiration during redirect

Generates unique short codes using Base62 encoding

Uses MongoDB for persistence

Follows clean separation between controller, service, and domain layers

Tech Stack

Java 17

Spring Boot

Spring Data MongoDB

MongoDB (Docker for local development)

Gradle

High-Level Architecture

Client
→ Controller (HTTP concerns only)
→ Service (business rules)
→ Repository (MongoDB)

The application is stateless and horizontally scalable.

Project Structure

api
Handles HTTP endpoints and request/response DTOs

domain
Core business concepts such as UrlMapping and redirect resolution results

service
Business logic and orchestration (URL creation, redirect resolution)

repository
MongoDB repositories

util
Stateless utilities (Base62 encoder)

Core Domain Concepts

UrlMapping
Represents a shortened URL and its lifecycle.
Includes shortCode, longUrl, createdAt, and optional expiresAt.

RedirectResult
A value object representing the outcome of resolving a short URL.
Possible states are FOUND, NOT_FOUND, and EXPIRED.
This keeps business logic out of the controller and avoids leaking HTTP concerns into the service layer.

API Overview

Create Short URL
POST /api/v1/shorten
Accepts a long URL and optional expiry duration.
Returns a generated short URL.

Redirect
GET /{shortCode}

Behavior:

Valid short code → HTTP 302 redirect

Expired short code → HTTP 410 Gone

Unknown short code → HTTP 404 Not Found

Redirect endpoints are intentionally root-level (not under /api) to match real-world URL shortener behavior.

Expiration Handling

Expiration is enforced at redirect time

Expired URLs are not deleted in V1

HTTP 410 Gone is returned for expired URLs

Physical cleanup (TTL index) is intentionally deferred to a later version

Persistence Model

MongoDB collections:

url_mapping: stores URL mappings

counters: stores atomic counters for ID generation

ID generation uses an atomic find-and-modify pattern to ensure uniqueness and correctness under concurrency.

Testing (V1)

Included:

Unit tests for Base62 encoding logic

Manual end-to-end testing using browser and curl

Not included (by design in V1):

Controller integration tests

Embedded MongoDB tests

Performance tests

The focus of V1 is correctness and architecture, not exhaustive test coverage.

Local Development

MongoDB runs via Docker.
The application runs locally on port 8080.

What V1 Does Not Include

These are intentionally out of scope for V1:

Redis caching

Analytics / hit counting

Asynchronous processing (Kafka)

Rate limiting

Authentication or user management

Automatic TTL cleanup

V1 Status

V1 is complete.

It includes:

Full create and redirect flow

Correct HTTP semantics

Expiration enforcement

Clean domain modeling

Git-based development workflow

Alignment with the original HLD

Next Steps (Post-V1)

Planned future iterations:

V2: Redis caching for redirects

V3: Analytics and async processing

V4: Rate limiting, abuse prevention, and hardening

Notes

This project was built incrementally using feature branches and GitHub issues, mirroring real-world backend development practices. The emphasis is on clarity, correctness, and extensibility rather than feature volume.