# Developer Contribution Guidelines

Welcome! This repository follows an AI-Native development workflow.

The goal is to enable productive collaboration between developers and AI coding assistants while maintaining code quality, security, and human accountability.

## 1. Branch Naming Conventions

### Features

```text
feature/WO-<issue_id>-<short-description>
```

Example:

```text
feature/WO-201-create-work-order
```

### Bug Fixes

```text
fix/WO-<issue_id>-<short-description>
```

### Spikes / Research

```text
spike/WO-<issue_id>-<short-description>
```

## 2. Pull Request Rules

1. Never push application changes directly to `main`.
2. Every feature or bug fix must be associated with a GitHub Issue.
3. Pull Requests must clearly describe the changes.
4. Automated CI checks must pass before merging.
5. At least one human developer must review the Pull Request.
6. AI-assisted changes must be reviewed by a human before merging.

## 3. Mandatory AI Output Policy

### Untrusted Code Policy

All code produced or suggested by GitHub Copilot or other AI tools is considered **UNTRUSTED** until reviewed and verified by a human developer.

### Verify AI Output

Developers must verify:

* Imported packages actually exist.
* APIs and methods actually exist.
* Generated logic matches the specification.
* Generated code follows project coding rules.
* Unit and integration tests pass.

### No Secrets in AI Prompts

Never include the following in AI prompts:

* Production passwords
* API keys
* Database credentials
* Private certificates
* Access tokens
* Customer confidential information
* Other sensitive production data

## 4. Testing Requirements

Before creating a Pull Request:

```bash
mvn clean test
```

All tests must pass.

## 5. Commit Convention

Use descriptive commit messages.

Examples:

```text
feat: add work order creation
fix: validate work order title
docs: update work order specification
test: add work order validation tests
```

## 6. Human-in-the-Loop Principle

AI can assist development, but the final responsibility remains with the human developer.

The developer must understand and verify the code before it is merged.
