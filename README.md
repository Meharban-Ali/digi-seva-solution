# Digi Seva Solution - Backend Service

Production-grade Spring Boot 3 backend REST API service for **Digi Seva Solution** (Common Service Center / Jan Seva Kendra).

---

## Technical Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.3.x
- **Security**: Spring Security (Stateless JWT Authentication & Authorization)
- **Environment Management**: `spring-dotenv` (Automatic `.env` file loader at startup)
- **Email & OTP Delivery**: Resend REST API Integration
- **Media Storage**: Cloudinary Java SDK Integration (`com.cloudinary:cloudinary-http44`)
- **API Documentation**: Springdoc OpenAPI / Swagger UI (`org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0`)
- **Database**: Neon PostgreSQL
- **ORM & Migrations**: Spring Data JPA + Flyway
- **Build System**: Apache Maven
- **Base Package**: `com.digisevasolution`

---

## Interactive API Documentation (Swagger UI)

When the backend application is running, interactively explore and test all API endpoints via Swagger UI:

- **Swagger UI Web Interface**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI v3 JSON Specification**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

> [!TIP]
> To test protected `/api/admin/**` endpoints in Swagger UI, click the **Authorize** button at the top right of the page and paste your Bearer JWT token obtained from `/api/admin/auth/verify-otp`.

---

## Pre-Seeded Admin Accounts (Local Dev Only)

> [!CAUTION]
> The database migration (`V3` & `V5`) seeds 2 partner admin accounts with a temporary default password. **Change these passwords immediately upon initial login**.

- **Admin Account 1**: `pashamr303@gmail.com`
- **Admin Account 2**: `sahanealam07860@gmail.com`
- **Temporary Password**: `Admin@12345`

---

## Environment Variables Configuration

Copy `.env.example` to `.env` in the project root and configure your credentials:

| Variable | Description | Default / Example Value |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Active Spring Boot Profile | `dev` |
| `PORT` | HTTP Server Port | `8080` |
| `ALLOWED_ORIGINS` | Comma-separated CORS allowed origins | `http://localhost:5173,http://localhost:3000` |
| `DB_URL` | Neon PostgreSQL JDBC Connection URL | `jdbc:postgresql://<neon-host>/<dbname>?sslmode=require` |
| `DB_USERNAME` | Neon Database Username | `neondb_owner` |
| `DB_PASSWORD` | Neon Database Password | `your_actual_neon_password` |
| `JWT_SECRET` | 256-bit Secret Key for signing JWTs | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` |
| `JWT_EXPIRATION_MS` | JWT Expiration Time in Milliseconds | `86400000` (24 hours) |
| `RESEND_API_KEY` | Resend API Key for sending OTP emails | `re_123456789_placeholder` |
| `RESEND_FROM_EMAIL` | Verified Sender Email Domain | `onboarding@resend.dev` |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary Cloud Name | `your_cloudinary_cloud_name` |
| `CLOUDINARY_API_KEY` | Cloudinary API Key | `your_cloudinary_api_key` |
| `CLOUDINARY_API_SECRET` | Cloudinary API Secret | `your_cloudinary_api_secret` |

---

## Master API Endpoint Inventory

Below is the complete list of all 22 REST endpoints provided by the backend service across all 6 business modules.

### 1. Actuator & System Health

| Method | Endpoint | Authorization | Description |
|---|---|---|---|
| `GET` | `/actuator/health` | Public | System health check (UP/DOWN) |
| `GET` | `/actuator/info` | Public | Application metadata |

---

### 2. Admin Authentication Module (`/api/admin/auth`)

| Method | Endpoint | Authorization | Description |
|---|---|---|---|
| `POST` | `/api/admin/auth/login` | Public | Step 1: Validate email/password & trigger 6-digit OTP email |
| `POST` | `/api/admin/auth/verify-otp` | Public | Step 2: Verify 6-digit OTP code & return Bearer JWT token |
| `POST` | `/api/admin/auth/change-password` | Bearer JWT | Update admin password & clear temporary password flag |

---

### 3. Service Management Module (`/api/admin/services` & `/api/services`)

| Method | Endpoint | Authorization | Description |
|---|---|---|---|
| `POST` | `/api/admin/services` | Bearer JWT | Create a new Jan Seva Kendra service item |
| `PUT` | `/api/admin/services/{id}` | Bearer JWT | Update an existing service item by ID |
| `DELETE` | `/api/admin/services/{id}` | Bearer JWT | Soft delete service item (sets `isActive = false`) |
| `GET` | `/api/admin/services` | Bearer JWT | Paginated list of all active/inactive service items |
| `GET` | `/api/admin/services/{id}` | Bearer JWT | Get full service item details (admin view) |
| `GET` | `/api/services` | Public | Get active services with resolved single-language view (`lang=en/hi`) |
| `GET` | `/api/services/{id}` | Public | Get single active service public detail view (`lang=en/hi`) |

---

### 4. Content Management Module (`/api/admin/content` & `/api/content`)

| Method | Endpoint | Authorization | Description |
|---|---|---|---|
| `POST` | `/api/admin/content` | Bearer JWT | Create content block (defaults to `DRAFT` status) |
| `PUT` | `/api/admin/content/{id}` | Bearer JWT | Update existing content block by ID |
| `DELETE` | `/api/admin/content/{id}` | Bearer JWT | Permanently delete content block |
| `PATCH` | `/api/admin/content/{id}/publish` | Bearer JWT | Convenience endpoint to publish content (`status = PUBLISHED`) |
| `PATCH` | `/api/admin/content/{id}/unpublish` | Bearer JWT | Convenience endpoint to unpublish content (`status = DRAFT`) |
| `GET` | `/api/admin/content` | Bearer JWT | Paginated list of draft & published content blocks |
| `GET` | `/api/admin/content/{id}` | Bearer JWT | Get full content block details (admin view) |
| `GET` | `/api/content` | Public | Get published content blocks for a section (`HOME_BANNER`, etc.) |
| `GET` | `/api/content/{id}` | Public | Get single published content block public detail (404 if draft) |

---

### 5. Media Upload Module (`/api/admin/media`)

| Method | Endpoint | Authorization | Description |
|---|---|---|---|
| `POST` | `/api/admin/media/upload` | Bearer JWT | Upload file (IMAGE/AUDIO/VIDEO) to Cloudinary & save asset record |
| `GET` | `/api/admin/media` | Bearer JWT | Paginated listing of media library assets (most recent first) |
| `DELETE` | `/api/admin/media/{id}` | Bearer JWT | Atomically destroy asset in Cloudinary and delete database record |

---

### 6. Customer Enquiry Module (`/api/admin/enquiries` & `/api/enquiries`)

| Method | Endpoint | Authorization | Description |
|---|---|---|---|
| `POST` | `/api/enquiries` | Public | Submit customer enquiry (rate limited to 5 per phone/hour) |
| `GET` | `/api/admin/enquiries` | Bearer JWT | Paginated list of customer enquiries (optional `status` filter) |
| `GET` | `/api/admin/enquiries/{id}` | Bearer JWT | Get single customer enquiry details |
| `PATCH` | `/api/admin/enquiries/{id}/status` | Bearer JWT | Update enquiry status lifecycle (`NEW` -> `CONTACTED` -> `RESOLVED`) |

---

## Building and Running Locally

```bash
# 1. Compile and build the application executable
mvn clean package

# 2. Run Spring Boot application locally
mvn spring-boot:run
```

The application will start on `http://localhost:8080` and connect to your Neon PostgreSQL database, automatically running all Flyway migrations (`V1` through `V12`).
