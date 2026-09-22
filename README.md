# AI-Native Core Service

## 1. Project Purpose

This repository serves as the baseline foundation for the Core Service API.

It is structured according to an AI-Native SDLC approach, where GitHub Copilot and AI-assisted development are used under explicit repository governance, specification, testing, and human review processes.

The repository structure is designed to provide clear context for both human developers and AI coding assistants.

## 2. Technology Stack & Prerequisites

* **Language / Runtime:** Java 21
* **Framework:** Spring Boot
* **Database:** PostgreSQL 16
* **Build Tool:** Maven
* **Containerization:** Docker
* **AI Tooling:** GitHub Copilot
* **Version Control:** Git / GitHub

### Prerequisites

* JDK 21+
* Maven
* Git
* Docker
* GitHub account
* GitHub Copilot enabled

## 3. Repository Structure

```text
.github/
├── ISSUE_TEMPLATE/
├── PULL_REQUEST_TEMPLATE.md
└── workflows/

docs/
├── domain-model.md
├── api-spec.md
└── coding-rules.md

src/
tests/
├── unit/
└── integration/

scripts/

README.md
CONTRIBUTING.md
```

## 4. Getting Started

### Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/ai-native-core.git
cd ai-native-core
```

### Build the project

```bash
mvn clean test
```

### Run the application

```bash
mvn spring-boot:run
```

## 5. Repository Governance

* All feature additions must originate from an approved GitHub Issue.
* Development work must be performed on a dedicated branch.
* Pull Requests are required for changes to the main branch.
* AI-generated or AI-assisted code must be reviewed by a human developer.
* Automated tests and quality checks must pass before merging.
* Production secrets, credentials, API keys, and proprietary data must not be included in AI prompts or repository files.

## 6. AI Development Principles

GitHub Copilot may be used to assist with:

* Boilerplate code
* Unit test generation
* Documentation
* Code suggestions
* Refactoring assistance

Developers remain responsible for:

* Reviewing generated code
* Verifying dependencies
* Validating business logic
* Running tests
* Checking security implications
* Approving the final change

##

### Copilot Code Generation Scorecard

| No. | Evaluation Criteria | Source Rules File | Status | Notes / Evidence |
| :--- | :--- | :--- | :---: | :--- |
| 1 | Uses Java 17+ and Spring Boot 3.3+ syntax | coding-rules.md | **Pass** | Uses jakarta.validation package standard for Spring Boot 3+. |
| 2 | Follows Naming Conventions (PascalCase for classes, camelCase for methods/variables) | coding-rules.md | **Pass** | Evaluated names like ScratchHandler and createWorkOrder strictly follow the rule. |
| 3 | Avoids generic Exception or RuntimeException | coding-rules.md | **Pass** | Relies properly on Spring's built-in validation framework handling rather than raw catches. |
| 4 | Uses SLF4J logger with correct instantiation and logs no PII | coding-rules.md | **Pass** | Standard logger setup. Log message is generic: "Creating a new work order". |
| 5 | Implements Constructor Injection (No field @Autowired) | coding-rules.md | **Pass** | Declared private final WorkOrderService and injected via the constructor. |
| 6 | Scope management: No variables declared inside loops | coding-rules.md | **Pass** | No performance overhead; controller layer stays flat without local loop scopes. |
| 7 | Uses plural nouns for REST resources (/api/workorders) | api-rules.md | **Pass** | Correctly maps the endpoint to @RequestMapping("/api/workorders"). |
| 8 | Strict schema conformance: No invented JSON fields | api-rules.md | **Pass** | WorkOrderRequest body contains only requested title and description fields. |
| 9 | Request validation with @Valid and specific constraints | api-rules.md | **Pass** | Endpoint has @Valid annotation; fields use @NotBlank with messages. |
| 10 | Enforcement of Role-Based Access Control (RBAC) | security-rules.md | **Pass** | Secured using @PreAuthorize("hasRole('ROLE_TECHNICIAN')") constraint. |

**Final Evaluation:** **10/10 Criteria Passed.** The generated code perfectly conforms to the established ruleset context.

