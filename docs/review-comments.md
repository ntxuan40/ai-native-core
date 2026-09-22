# Review Comments

Reviewed against `docs/coding-rules.md`, `docs/api-rules.md`, and `docs/security-rules.md`.

> `docs/api-rules.md` is not present in the current workspace. API findings below are based on the stated work-order requirements and the visible controller behavior.

## Spec Delta

- **Blocker - compilation:** The added `create` method references `Map` and `jdbcTemplate` without available imports, declarations, or constructor-injected dependencies. The source cannot compile as written. See `src/com/example/demo/ScratchHandler.java:35` and `src/com/example/demo/ScratchHandler.java:38`.
- **High - contract violation:** `POST /api/workorders/create` accepts `code`, `desc`, and `password` through an untyped map instead of accepting only the mandatory `title` and `description` fields. See `src/com/example/demo/ScratchHandler.java:34`.
- **High - response violation:** The added endpoint returns `200 OK` with the string `Done`, rather than the typed creation response and `201 Created` behavior of the contract. See `src/com/example/demo/ScratchHandler.java:39`.
- **High - validation violation:** The added endpoint has no `@Valid` request body or `@NotBlank` constraints, so blank or malformed work-order input is not handled through the required validation path. See `src/com/example/demo/ScratchHandler.java:35`.

## Security

- **Critical - SQL injection:** Request values are concatenated directly into an SQL statement. Use a repository or a parameterized query with bound parameters. See `src/com/example/demo/ScratchHandler.java:37`.
- **Critical - sensitive-data exposure:** A password is written to standard output. Remove the statement; passwords and other sensitive values must never be logged. See `src/com/example/demo/ScratchHandler.java:36`.
- **High - missing RBAC:** The added endpoint has no authorization annotation, so it does not enforce the required `ROLE_TECHNICIAN` access policy. See `src/com/example/demo/ScratchHandler.java:34`.
- **High - role-expression mismatch:** In standard Spring Security, `hasRole` adds the `ROLE_` prefix. `hasRole('ROLE_TECHNICIAN')` can therefore produce an invalid double prefix or fail validation. Use `hasRole('TECHNICIAN')`, or use `hasAuthority('ROLE_TECHNICIAN')` when the authority is already prefixed. See `src/com/example/demo/ScratchHandler.java:44`.

## Testing

- No unit or integration tests are present for this handler. Add coverage for technician authorization, unauthorized access, blank `title`, blank `description`, unknown JSON fields, malformed JSON, RFC 7807 fields, successful `201 Created`, and parameterized persistence.
- No Maven or Gradle build file is present, so compilation and executable Spring tests cannot currently be run.

## Complexity

- **High - duplicate creation paths:** The controller contains two POST creation endpoints with different payloads, security behavior, status codes, and persistence behavior. Remove the faulty `/create` path and retain one contract-compliant creation flow. See `src/com/example/demo/ScratchHandler.java:34` and `src/com/example/demo/ScratchHandler.java:43`.
- The untyped `Map` request and raw `ResponseEntity` remove compile-time guarantees and make the API contract harder to enforce. See `src/com/example/demo/ScratchHandler.java:35`.

## Style

- `System.out.println` is inconsistent with the repository logging rule, which requires SLF4J where logging is needed. More importantly, this particular log must be removed because it contains sensitive data. See `src/com/example/demo/ScratchHandler.java:36`.
- The added code uses inconsistent indentation and raw types, unlike the typed, constructor-injected method already present in the class. See `src/com/example/demo/ScratchHandler.java:27` and `src/com/example/demo/ScratchHandler.java:35`.
- `ScratchHandler` is not a descriptive production controller name, and controller, service, DTO, and advice types are all placed in one file. Split them into appropriately named classes when promoting this from scratch code.
