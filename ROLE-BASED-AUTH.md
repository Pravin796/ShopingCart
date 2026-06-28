# Role-Based Authentication

## Overview

Spring Boot + JWT project with two roles: `USER` (default) and `ADMIN`.

## Architecture

- Role stored as `role` column on `users` table (`entities/User.java:38`)
- Role enum: `entities/Role.java` — `USER`, `ADMIN`
- No `@PreAuthorize` / method-level security — only URL pattern + programmatic checks

## Authentication Flow

### 1. Registration (`controller/UserController.java:83`)
```java
user.setRole(Role.USER);  // always USER
```
No endpoint to register as ADMIN.

### 2. Login (`controller/AuthController.java:34-64`)
- Authenticates credentials via `AuthenticationManager`
- `JwtService:40` embeds `"role"` claim in JWT access token

### 3. Request Filtering (`filters/JwtAuthenticationFilter.java:22-53`)
- Extracts `Authorization: Bearer <token>` header
- Parses JWT, reads `role` claim
- Creates `UsernamePasswordAuthenticationToken` with `SimpleGrantedAuthority("ROLE_" + role)`
- Sets authentication in `SecurityContextHolder`

### 4. Authorization (`config/SecurityConfig.java:54-88`)
```java
.requestMatchers("/admin/**").hasRole("ADMIN")  // requires ROLE_ADMIN
.anyRequest().authenticated()                   // any authenticated user
```

### Public endpoints (no auth required):
- `POST /users` — registration
- `POST /auth/login`
- `POST /auth/refresh`
- `POST /checkout/webhook` — Stripe webhook
- `/carts/**`

### 5. Programmatic Checks (`services/OrderService.java:30`)
```java
var user = authService.getCurrentUser();
if (!order.isPlacedBy(user)) throw new AccessDeniedException("...");
```
`AuthService.getCurrentUser()` reads principal (user ID) from `SecurityContextHolder`.

## Key Files

| File | Purpose |
|---|---|
| `entities/Role.java` | `USER` / `ADMIN` enum |
| `entities/User.java:38` | `@Enumerated(EnumType.STRING) Role role` |
| `config/SecurityConfig.java:67` | `hasRole("ADMIN")` on `/admin/**` |
| `config/JwtConfig.java` | JWT secret, expiration config |
| `filters/JwtAuthenticationFilter.java:45` | Sets `ROLE_` authority from JWT claim |
| `filters/LogginFilter.java` | Request logging (no auth impact) |
| `services/JwtService.java:40` | Embeds role in JWT |
| `services/Jwt.java:29` | `getRole()` reads role from JWT claim |
| `services/AuthService.java` | Gets current user from `SecurityContextHolder` |
| `services/UserService.java` | `UserDetailsService` for login (no roles in UserDetails) |
| `controller/AuthController.java` | Login endpoint |
| `controller/UserController.java:83` | Registration: always `Role.USER` |
| `controller/AdminController.java` | Only accessible with `ROLE_ADMIN` |
| `controller/OrderController.java` | Handles `AccessDeniedException` -> 403 |
| `services/OrderService.java` | Programmatic ownership check |
| `payments/CheckOutService.java` | Uses `AuthService.getCurrentUser()` |
| `db/migration/V5__Add_role_user_table.sql` | Adds `role` column with default `'USER'` |
| `application.yaml` | JWT secret + expiration config |

## Notable Details

- **`UserDetailsService` returns zero authorities** — role info comes from JWT token, not DB lookup
- **Cart endpoints are public** — no auth required for cart operations
- **No ADMIN promotion endpoint** — must be done manually via SQL
- **Access token**: 2 hours expiry; **Refresh token**: 7 days (HttpOnly cookie)
- **401** returned when unauthenticated; **403** when authenticated but lacks role
