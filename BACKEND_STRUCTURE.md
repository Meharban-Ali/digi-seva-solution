# Digi Seva Solution Backend - Architecture & Folder Structure Reference

This document provides a comprehensive technical reference for the **Digi Seva Solution** backend service codebase located at `D:\Meharban-code\digi-seva-solution-backend`.

---

## 1. High-Level Architecture Overview

The backend uses a standard **Clean Layered Architecture** pattern:

$$\text{Client / HTTP Request} \longrightarrow \text{Controller Layer} \longrightarrow \text{Service Layer} \longrightarrow \text{Repository Layer} \longrightarrow \text{Neon PostgreSQL DB}$$

### Key Architectural Characteristics
- **Stateless RESTful API**: Every HTTP request is authenticated independently via stateless Bearer JWT tokens in the `Authorization` header.
- **Strict Separation of Public & Admin Concerns**: Public endpoints under `/api/services`, `/api/content`, `/api/enquiries` require no authentication, while `/api/admin/**` endpoints are strictly protected by Spring Security.
- **Layer Responsibilities**:
  - **Controllers**: Handle HTTP routing, input validation (`@Valid`), response formatting (`ApiResponse<T>`), and status codes.
  - **Services**: Execute domain business logic, rate-limiting checks, transactions (`@Transactional`), third-party REST calls (Resend, Cloudinary), and bilingual fallback resolutions.
  - **Repositories**: Provide Spring Data JPA data abstraction interfaces.
  - **Entities**: Represent relational database schemas mapped via JPA annotations.

### Core Technology Stack
- **Language & Runtime**: Java 21 LTS
- **Framework**: Spring Boot 3.3.4
- **Security**: Spring Security + JJWT 0.12.6 (Stateless JWT)
- **Database & Migration**: Neon PostgreSQL + Flyway 10
- **Third-Party Services**: Resend REST API (OTP delivery), Cloudinary Java SDK 1.38.0 (Media storage)
- **Environment Management**: `me.paulschwarz:spring-dotenv` 4.0.0 (Auto-loads `.env` at startup)
- **API Documentation**: Springdoc OpenAPI / Swagger UI 2.6.0

---

## 2. Complete Folder & File Tree

```
digi-seva-solution-backend/
├── pom.xml
├── .env
├── .env.example
├── .gitignore
├── README.md
├── BACKEND_STRUCTURE.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── digisevasolution/
        │           ├── DigiSevaApplication.java
        │           ├── config/
        │           │   ├── CloudinaryConfig.java
        │           │   ├── JacksonConfig.java
        │           │   └── OpenApiConfig.java
        │           ├── controller/
        │           │   ├── AdminContentBlockController.java
        │           │   ├── AdminEnquiryController.java
        │           │   ├── AdminMediaAssetController.java
        │           │   ├── AdminServiceItemController.java
        │           │   ├── AuthController.java
        │           │   ├── PublicContentBlockController.java
        │           │   ├── PublicEnquiryController.java
        │           │   └── PublicServiceItemController.java
        │           ├── dto/
        │           │   ├── request/
        │           │   │   ├── ChangePasswordRequest.java
        │           │   │   ├── ContentBlockRequest.java
        │           │   │   ├── EnquiryRequest.java
        │           │   │   ├── LoginRequest.java
        │           │   │   ├── ServiceItemRequest.java
        │           │   │   ├── UpdateEnquiryStatusRequest.java
        │           │   │   └── VerifyOtpRequest.java
        │           │   └── response/
        │           │       ├── AdminUserDto.java
        │           │       ├── ApiResponse.java
        │           │       ├── ContentBlockResponse.java
        │           │       ├── EnquiryResponse.java
        │           │       ├── JwtAuthResponse.java
        │           │       ├── MediaAssetResponse.java
        │           │       ├── PublicContentResponse.java
        │           │       ├── PublicServiceResponse.java
        │           │       └── ServiceItemResponse.java
        │           ├── entity/
        │           │   ├── AdminUser.java
        │           │   ├── ContentBlock.java
        │           │   ├── ContentSection.java
        │           │   ├── ContentStatus.java
        │           │   ├── Enquiry.java
        │           │   ├── EnquiryStatus.java
        │           │   ├── MediaAsset.java
        │           │   ├── MediaType.java
        │           │   ├── OtpToken.java
        │           │   ├── ServiceCategory.java
        │           │   └── ServiceItem.java
        │           ├── exception/
        │           │   ├── ApiException.java
        │           │   ├── ErrorDetails.java
        │           │   ├── GlobalExceptionHandler.java
        │           │   ├── InvalidCredentialsException.java
        │           │   ├── InvalidOtpException.java
        │           │   ├── OtpExpiredException.java
        │           │   ├── OtpRateLimitException.java
        │           │   └── ResourceNotFoundException.java
        │           ├── repository/
        │           │   ├── AdminUserRepository.java
        │           │   ├── ContentBlockRepository.java
        │           │   ├── EnquiryRepository.java
        │           │   ├── MediaAssetRepository.java
        │           │   ├── OtpTokenRepository.java
        │           │   └── ServiceItemRepository.java
        │           ├── security/
        │           │   ├── CustomUserDetails.java
        │           │   ├── CustomUserDetailsService.java
        │           │   ├── JwtAuthFilter.java
        │           │   └── SecurityConfig.java
        │           ├── service/
        │           │   ├── AuthService.java
        │           │   ├── CloudinaryService.java
        │           │   ├── ContentBlockService.java
        │           │   ├── EnquiryService.java
        │           │   ├── MediaAssetService.java
        │           │   ├── OtpService.java
        │           │   ├── ResendEmailService.java
        │           │   ├── ServiceItemService.java
        │           │   └── impl/
        │           │       ├── AuthServiceImpl.java
        │           │       ├── ContentBlockServiceImpl.java
        │           │       ├── EnquiryServiceImpl.java
        │           │       ├── MediaAssetServiceImpl.java
        │           │       ├── OtpServiceImpl.java
        │           │       └── ServiceItemServiceImpl.java
        │           └── util/
        │               └── JwtTokenProvider.java
        └── resources/
            ├── application.properties
            ├── application-dev.properties
            ├── application-prod.properties
            └── db/
                └── migration/
                    ├── V1__init.sql
                    ├── V2__create_admin_user_table.sql
                    ├── V3__seed_admin_users.sql
                    ├── V4__create_otp_token_table.sql
                    ├── V5__fix_admin_password_hash.sql
                    ├── V6__create_service_item_table.sql
                    ├── V7__create_content_block_table.sql
                    ├── V8__create_media_asset_table.sql
                    └── V9__create_enquiry_table.sql
```

---

## 3. Package-by-Package Explanation

| Package | Purpose & Role in Request Lifecycle | Typical Inter-Package Communication |
|---|---|---|
| `config` | Spring `@Configuration` beans for third-party libraries (Jackson JSON formatting, Cloudinary client, OpenAPI metadata). | Instantiates beans used by `service` and Spring framework. |
| `security` | Spring Security infrastructure: JWT filter interceptor, CORS configuration, password encoder, and UserDetailsService. | Intercepts requests before `controller`, communicates with `util` (JwtTokenProvider) and `repository` (AdminUserRepository). |
| `controller` | REST API Controllers. Receives HTTP requests, executes `@Valid` validation on request DTOs, delegates business logic to services, and returns `ResponseEntity<ApiResponse<T>>`. | Receives input from `dto.request`, calls `service`, returns `dto.response`. |
| `dto.request` | Incoming request payload data structures annotated with Jakarta Bean Validation (`@NotBlank`, `@NotNull`, `@Pattern`, `@Email`, `@Min`, `@PositiveOrZero`). | Received by `controller`, passed to `service`. |
| `dto.response` | Outgoing response data structures, including the standard `ApiResponse<T>` wrapper, auth tokens, and admin/public views. | Returned by `service` and `controller` to the client. |
| `entity` | JPA Database Entities mapped to PostgreSQL tables, plus domain Enums (`ServiceCategory`, `ContentSection`, `ContentStatus`, `MediaType`, `EnquiryStatus`). | Used by `repository` and `service`. |
| `repository` | Spring Data JPA interfaces extending `JpaRepository`. Provides CRUD and custom SQL/JPQL queries. | Called by `service`, queries Neon PostgreSQL database. |
| `service` & `service.impl` | Interfaces and implementation classes containing all core business logic, rate limiting, Cloudinary/Resend API calls, and bilingual fallbacks. | Called by `controller`, calls `repository`, `util`, and external REST APIs. |
| `exception` | Custom runtime exception classes and `@RestControllerAdvice` `GlobalExceptionHandler` returning consistent `ApiResponse` error bodies. | Catches exceptions thrown by `security`, `controller`, or `service`. |
| `util` | Technical helper utilities (e.g. `JwtTokenProvider` for generating and parsing HMAC-SHA256 JWT tokens). | Used by `security` and `service`. |

---

## 4. Module-by-Module Breakdown

### 4.1 Admin Authentication & Security Module
- **Database Tables**: `admin_users`, `otp_tokens`
- **Key Files**:
  - `entity/AdminUser.java`: Entity for partner admin accounts (`email`, `password_hash`, `full_name`, `is_first_login`).
  - `entity/OtpToken.java`: Entity for 6-digit numeric OTP tokens (`email`, `otp_code`, `expires_at`, `verified`).
  - `repository/AdminUserRepository.java`: Lookup by email (`findByEmail`).
  - `repository/OtpTokenRepository.java`: OTP lookups and rate limit checks (`findTopByEmailOrderByCreatedAtDesc`).
  - `service/AuthService.java` / `impl/AuthServiceImpl.java`: Handles 2-step login and password updates.
  - `service/OtpService.java` / `impl/OtpServiceImpl.java`: Generates 6-digit OTP, enforces 60s rate limit, verifies expiry.
  - `service/ResendEmailService.java`: Delivers OTP via Resend REST API (with local console log fallback).
  - `controller/AuthController.java`: Endpoints for login step 1, OTP verify step 2, and password change.
- **Endpoints**:
  - `POST /api/admin/auth/login` (Public): Validates credentials & triggers OTP email.
  - `POST /api/admin/auth/verify-otp` (Public): Validates OTP code & issues JWT token.
  - `POST /api/admin/auth/change-password` (Bearer JWT): Updates password and clears `isFirstLogin` flag.
- **Module Specifics**:
  - OTP rate-limited to **1 request per 60 seconds** per email.
  - OTP expires after **10 minutes**.
  - Seeded BCrypt hash for temporary default password `Admin@12345` is `$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te`.

---

### 4.2 Service Management Module
- **Database Table**: `service_items`
- **Key Files**:
  - `entity/ServiceCategory.java`: Enum (`VISIT_REQUIRED`, `ONLINE`).
  - `entity/ServiceItem.java`: Entity for CSC/Jan Seva Kendra services (`name_en`, `name_hi`, `description_en`, `description_hi`, `category`, `price`, `image_url`, `is_active`, `display_order`).
  - `repository/ServiceItemRepository.java`: JPA repository supporting paginated listings and public active/category queries.
  - `dto/request/ServiceItemRequest.java`: Admin creation/update request DTO.
  - `dto/response/ServiceItemResponse.java`: Admin full bilingual view response DTO.
  - `dto/response/PublicServiceResponse.java`: Public single-language view response DTO.
  - `service/ServiceItemService.java` / `impl/ServiceItemServiceImpl.java`: Admin CRUD, soft deletion (`isActive = false`), and language fallback.
  - `controller/AdminServiceItemController.java`: Protected admin CRUD endpoints (`/api/admin/services`).
  - `controller/PublicServiceItemController.java`: Unauthenticated public catalog endpoints (`/api/services`).
- **Endpoints**:
  - `POST /api/admin/services` (Bearer JWT): Create service item.
  - `PUT /api/admin/services/{id}` (Bearer JWT): Update service item.
  - `DELETE /api/admin/services/{id}` (Bearer JWT): Soft delete (sets `isActive = false`).
  - `GET /api/admin/services` (Bearer JWT): Paginated list of all active/inactive services.
  - `GET /api/admin/services/{id}` (Bearer JWT): Get admin service details.
  - `GET /api/services` (Public): Get active services with resolved language text (`lang=en/hi`).
  - `GET /api/services/{id}` (Public): Get single active service public detail view (`lang=en/hi`).
- **Module Specifics**:
  - **Soft Delete**: `DELETE` endpoint sets `is_active = false` so historical records remain in database.
  - **Language Fallback**: Handled entirely in `ServiceItemServiceImpl`. If `lang=hi` requested and `name_hi` or `description_hi` is blank/null, English fields are returned automatically.

---

### 4.3 Content Management Module
- **Database Table**: `content_blocks`
- **Key Files**:
  - `entity/ContentSection.java`: Enum (`HOME_BANNER`, `ABOUT_US`, `ANNOUNCEMENT`, `OFFER`).
  - `entity/ContentStatus.java`: Enum (`DRAFT`, `PUBLISHED`).
  - `entity/ContentBlock.java`: Entity for homepage text blocks (`section`, `title_en`, `title_hi`, `body_en`, `body_hi`, `linked_media_id`, `status`, `display_order`).
  - `repository/ContentBlockRepository.java`: JPA repository supporting status and section filtered queries.
  - `dto/request/ContentBlockRequest.java`: Request payload (defaults `status` to `DRAFT`).
  - `dto/response/ContentBlockResponse.java`: Admin response DTO.
  - `dto/response/PublicContentResponse.java`: Public single-language response DTO.
  - `service/ContentBlockService.java` / `impl/ContentBlockServiceImpl.java`: Admin CRUD, hard delete, `publish`/`unpublish` toggling, and language fallback.
  - `controller/AdminContentBlockController.java`: Protected endpoints under `/api/admin/content`.
  - `controller/PublicContentBlockController.java`: Public endpoints under `/api/content`.
- **Endpoints**:
  - `POST /api/admin/content` (Bearer JWT): Create content block (defaults to DRAFT).
  - `PUT /api/admin/content/{id}` (Bearer JWT): Update content block.
  - `DELETE /api/admin/content/{id}` (Bearer JWT): Hard delete content block.
  - `PATCH /api/admin/content/{id}/publish` (Bearer JWT): Flip status to `PUBLISHED`.
  - `PATCH /api/admin/content/{id}/unpublish` (Bearer JWT): Flip status to `DRAFT`.
  - `GET /api/admin/content` (Bearer JWT): Paginated list of all draft/published content blocks.
  - `GET /api/admin/content/{id}` (Bearer JWT): Get admin content detail.
  - `GET /api/content` (Public): Get published content blocks for a section.
  - `GET /api/content/{id}` (Public): Get single published content block (returns 404 if draft).
- **Module Specifics**:
  - **Draft Privacy Rule**: Public endpoints query `status = 'PUBLISHED'` strictly. DRAFT content blocks are never exposed publicly under any circumstance. Requesting a DRAFT ID via public detail endpoint returns HTTP 404.

---

### 4.4 Media Upload Module
- **Database Table**: `media_assets`
- **Key Files**:
  - `entity/MediaType.java`: Enum (`IMAGE`, `AUDIO`, `VIDEO`).
  - `entity/MediaAsset.java`: Entity for stored media metadata (`type`, `cloudinary_url`, `cloudinary_public_id`, `title`, `file_size_bytes`, `uploaded_by`).
  - `repository/MediaAssetRepository.java`: Paginated listing ordered by `uploaded_at` DESC.
  - `config/CloudinaryConfig.java`: Spring Bean configuration for Cloudinary Java SDK.
  - `service/CloudinaryService.java`: Encapsulates raw Cloudinary REST API upload and destroy calls.
  - `service/MediaAssetService.java` / `impl/MediaAssetServiceImpl.java`: Multipart validation, size limit checks, Cloudinary upload, and atomic deletion.
  - `controller/AdminMediaAssetController.java`: Protected endpoints under `/api/admin/media`.
- **Endpoints**:
  - `POST /api/admin/media/upload` (Bearer JWT): Upload multipart file to Cloudinary & record asset metadata.
  - `GET /api/admin/media` (Bearer JWT): Paginated media library listing (most recent first).
  - `DELETE /api/admin/media/{id}` (Bearer JWT): Destroy in Cloudinary and delete database record.
- **Module Specifics**:
  - **File Size Limits**: Max 10MB for images (`type=IMAGE`), max 50MB for audio (`type=AUDIO`) and video (`type=VIDEO`). Enforced in both Spring's multipart config and service layer.
  - **Atomic Deletion**: Deleting an asset calls `CloudinaryService.deleteFile(publicId)` first. If Cloudinary remote destruction fails, an exception is thrown, preventing orphaned database records.

---

### 4.5 Customer Enquiry Module
- **Database Table**: `enquiries`
- **Key Files**:
  - `entity/EnquiryStatus.java`: Enum (`NEW`, `CONTACTED`, `RESOLVED`).
  - `entity/Enquiry.java`: Entity for customer enquiry submissions (`name`, `phone`, `email`, `service_id`, `message`, `status`).
  - `repository/EnquiryRepository.java`: Queries supporting status filtering and rate limit counts (`countByPhoneAndCreatedAtAfter`).
  - `dto/request/EnquiryRequest.java`: Public submission DTO with phone format validation.
  - `dto/request/UpdateEnquiryStatusRequest.java`: Admin status update DTO.
  - `dto/response/EnquiryResponse.java`: Response DTO.
  - `service/EnquiryService.java` / `impl/EnquiryServiceImpl.java`: Rate limiting enforcement, default status assignment (`NEW`), and status updates.
  - `controller/PublicEnquiryController.java`: Unauthenticated submission endpoint (`POST /api/enquiries`).
  - `controller/AdminEnquiryController.java`: Protected admin management endpoints (`/api/admin/enquiries`).
- **Endpoints**:
  - `POST /api/enquiries` (Public): Submit customer enquiry (rate-limited to 5 per phone/hour).
  - `GET /api/admin/enquiries` (Bearer JWT): Paginated list of enquiries (optional `status` filter).
  - `GET /api/admin/enquiries/{id}` (Bearer JWT): Get enquiry details.
  - `PATCH /api/admin/enquiries/{id}/status` (Bearer JWT): Update enquiry status lifecycle.
- **Module Specifics**:
  - **Submission Rate Limit**: Max 5 submissions per phone number per hour. Exceeding returns `HTTP 429 Too Many Requests`.
  - **Phone Format Validation**: Enforces Indian 10-digit mobile (`^[6-9]\d{9}$`) or international formats (`^\+?[1-9]\d{1,14}$`).

---

## 5. Cross-Cutting Concerns

### 5.1 JWT Authentication Flow
1. Client sends request with header: `Authorization: Bearer <token>`.
2. `JwtAuthFilter` intercepts the request.
3. `JwtAuthFilter` extracts the Bearer token string and validates signature & expiration via `JwtTokenProvider`.
4. If valid, `JwtTokenProvider` extracts the user's email.
5. `CustomUserDetailsService` loads `CustomUserDetails` from database via `AdminUserRepository`.
6. `JwtAuthFilter` creates a `UsernamePasswordAuthenticationToken` and sets it in Spring Security's `SecurityContextHolder`.
7. Request proceeds to the controller with `@AuthenticationPrincipal CustomUserDetails userDetails` populated.

### 5.2 Global Exception Handling
All exceptions thrown anywhere in the application are caught by `GlobalExceptionHandler` (`@RestControllerAdvice`):
- `ResourceNotFoundException` $\rightarrow$ HTTP 404 Not Found
- `InvalidCredentialsException`, `InvalidOtpException`, `OtpExpiredException`, `BadCredentialsException` $\rightarrow$ HTTP 401 Unauthorized
- `AccessDeniedException` $\rightarrow$ HTTP 403 Forbidden
- `OtpRateLimitException` $\rightarrow$ HTTP 429 Too Many Requests
- `MaxUploadSizeExceededException` $\rightarrow$ HTTP 400 Bad Request
- `MethodArgumentNotValidException` $\rightarrow$ HTTP 400 Bad Request (Returns JSON list of field validation errors)
- `ApiException` $\rightarrow$ Dynamic HTTP status code set in exception
- `Exception` (Safety net) $\rightarrow$ HTTP 500 Internal Server Error

All responses follow the unified `ApiResponse<T>` JSON format:
```json
{
  "success": false,
  "message": "Error description here",
  "data": null,
  "errors": ["field: error details"]
}
```

### 5.3 Bilingual Fallback Pattern (`lang=en/hi`)
Public endpoints accept optional query param `lang` (default `"en"`):
- If `lang="hi"`: Service layer checks if Hindi field (`name_hi` / `title_hi`) exists and is not blank.
  - If Hindi field is present $\rightarrow$ returns Hindi field.
  - If Hindi field is null or empty $\rightarrow$ automatically falls back to English field (`name_en` / `title_en`).
- If `lang="en"` $\rightarrow$ returns English field.
- Implemented cleanly in `ServiceItemServiceImpl` and `ContentBlockServiceImpl`.

### 5.4 Environment Variables & `spring-dotenv`
Environment variables are loaded automatically from `.env` at root via `me.paulschwarz:spring-dotenv`:
- `SPRING_PROFILES_ACTIVE`: Profile (`dev` / `prod`).
- `PORT`: HTTP port (`8080`).
- `ALLOWED_ORIGINS`: Comma-separated CORS allowed origins (`http://localhost:5173,http://localhost:3000`).
- `DB_URL`: Neon PostgreSQL JDBC URL.
- `DB_USERNAME`: Database username.
- `DB_PASSWORD`: Database password.
- `JWT_SECRET`: 256-bit secret key for signing JWT tokens.
- `JWT_EXPIRATION_MS`: Token lifespan in milliseconds (86,400,000 ms = 24h).
- `RESEND_API_KEY`: API key for Resend OTP emails.
- `RESEND_FROM_EMAIL`: Sender email domain (`onboarding@resend.dev`).
- `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`: Cloudinary API credentials.

### 5.5 Flyway Database Migrations
Migrations live in `src/main/resources/db/migration/` and execute sequentially:
- `V1__init.sql`: Baseline empty initialization.
- `V2__create_admin_user_table.sql`: Creates `admin_users` table.
- `V3__seed_admin_users.sql`: Seeds 2 initial partner admin accounts.
- `V4__create_otp_token_table.sql`: Creates `otp_tokens` table.
- `V5__fix_admin_password_hash.sql`: Updates seeded password hash for `Admin@12345`.
- `V6__create_service_item_table.sql`: Creates `service_items` table.
- `V7__create_content_block_table.sql`: Creates `content_blocks` table.
- `V8__create_media_asset_table.sql`: Creates `media_assets` table.
- `V9__create_enquiry_table.sql`: Creates `enquiries` table.

> [!CAUTION]
> **Hard Rule**: Already-applied Flyway migrations (`V1` through `V9`) must **never** be edited. Any future schema modification must be created as a new file (e.g. `V10__...`).

---

## 6. "Where Do I Look When..." Debugging Guide

| If the problem / symptom is... | Look in file / location... | Key Reason / Mechanism |
|---|---|---|
| Admin Login or OTP verification fails | `security/JwtAuthFilter.java`, `service/impl/AuthServiceImpl.java`, `service/impl/OtpServiceImpl.java` | Check password matching order, OTP expiry (10m), or 60s rate limit. |
| DB Connection refused ("Connection to localhost:5432 refused") | `.env`, `application-dev.properties` | Environment variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` are missing or `.env` file is unreadable. |
| Flyway checksum mismatch error on startup | `resources/db/migration/` | An applied migration file (`V1`..`V9`) was modified after execution. Must create a new `V10__...` script or clean DB flyway history. |
| Seeded Admin password doesn't match `Admin@12345` | `db/migration/V5__fix_admin_password_hash.sql` | Hash must be `$2a$10$q.tpUDUy7cPa5xxRHPQlUOyVuUeJ0o8aNh8401C0tXrJK72fTq3te`. Check `BcryptTest.java`. |
| CORS blocking frontend API requests | `security/SecurityConfig.java`, `.env` | Origin is missing from `ALLOWED_ORIGINS` variable in `.env`. |
| Validation error (HTTP 400) not formatting properly | `exception/GlobalExceptionHandler.java` | `handleValidationExceptions` parses `@Valid` binding errors into `ApiResponse.error(...)`. |
| Field validation `@NotBlank` / `@Pattern` not triggering | Target `dto/request/` class & `controller/` method | Ensure `@Valid` is present on the `@RequestBody` parameter in controller. |
| HTTP 401 returned on public endpoint | `security/SecurityConfig.java` | Endpoint path is missing from `.permitAll()` matcher list in `SecurityConfig`. |
| Draft content appearing on public API | `service/impl/ContentBlockServiceImpl.java` | Check public query method; must filter `status = ContentStatus.PUBLISHED` strictly. |
| Public detail API returning draft item instead of 404 | `repository/ContentBlockRepository.java` | Public single query must use `findByIdAndStatus(id, PUBLISHED)`. |
| File upload exceeding size limit crashes with stack trace | `exception/GlobalExceptionHandler.java`, `application-dev.properties` | Check `MaxUploadSizeExceededException` handler and `spring.servlet.multipart.max-file-size=50MB`. |
| File type mismatch (e.g. video uploaded as image) | `service/impl/MediaAssetServiceImpl.java` | Check `validateFileTypeAndSize` extension set checking. |
| Deleted media asset leaving orphaned Cloudinary file | `service/CloudinaryService.java`, `service/impl/MediaAssetServiceImpl.java` | Cloudinary destroy call must execute before `mediaAssetRepository.delete(entity)`. |
| OTP emails not arriving in dev | `service/ResendEmailService.java` | Check console log for `LOCAL DEV FALLBACK: Generated OTP...` if `RESEND_API_KEY` is placeholder. |
| Public enquiry submission rejected with HTTP 429 | `service/impl/EnquiryServiceImpl.java`, `repository/EnquiryRepository.java` | Phone number submitted > 5 times in 1 hour window. |
| Swagger UI not accessible at `/swagger-ui.html` | `config/OpenApiConfig.java`, `security/SecurityConfig.java` | Check if `/swagger-ui/**` and `/v3/api-docs/**` are in `SecurityConfig.permitAll()`. |
