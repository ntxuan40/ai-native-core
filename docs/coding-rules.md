# Java Coding & Logging Rules (WorkOrder Module)

1. **Language & Framework:** Use Java 17+ and Spring Boot 3.3+.
2. **Naming Conventions:** Use `PascalCase` for classes, `camelCase` for methods and variables, `UPPER_SNAKE_CASE` for constants.
3. **Exception Handling:** Never throw generic `RuntimeException` or `Exception`. Use specific custom exceptions or standard ones like `IllegalArgumentException`.
4. **Logging Standards:** 
   - DO NOT log sensitive data or PII (Personally Identifiable Information like raw passwords, emails, phone numbers).
   - Use SLF4J logger instantiated as `private static final Logger log = LoggerFactory.getLogger(ClassName.class);`.
5. **Dependency Injection:** Always use constructor injection. Avoid field injection (`@Autowired` on fields).
6. **Code Simplicity:** Avoid over-engineering, design patterns factories unless requested. Write clean, flat vertical slices.
7. **Variable Declaration Scope:** DO NOT declare variables inside loops. All loop-related or temporary variables must be declared outside the loop block to reduce overhead.

#### Example [GOOD]:
```java
public class WorkOrderService {
    private final WorkOrderRepository repository;
    public WorkOrderService(WorkOrderRepository repository) {
        this.repository = repository;
    }
}
```
