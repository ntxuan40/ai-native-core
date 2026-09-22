# WO-201: Create Work Order Domain Capabilities

Source: [GitHub Issue #2](https://github.com/ntxuan40/ai-native-core/issues/2)

This document is a direct technical decomposition of the Issue. It does not add UI behavior, API behavior, database fields, integrations, or implementation decisions that are not stated in the Issue.

## 1. Scope

### In scope

- Basic work-order creation flow.
- Client-side validation.
- Initialize the work-order status as `DRAFT`.

### Out of scope

- Dispatching algorithms.
- Third-party vendor synchronization.

## 2. UI Design

### 2.1 Form fields

| Field | Type | Required | Issue-defined rule |
| --- | --- | ---: | --- |
| `title` | Text | Yes | Must contain at least 5 characters. |
| `description` | Text | Not specified | No additional rule is defined. |
| `priority` | Enum | Not specified | Allowed values are `LOW`, `MED`, and `HIGH`; defaults to `MED`. |
| `customer_id` | UUID | Not specified | No additional UI rule is defined. |

The Issue does not define a specific widget, layout, placeholder, maximum client-side length, loading behavior, success screen, retry behavior, or exact error-message wording. Those details must not be invented in this design.

### 2.2 UI validation table

| Condition stated by the Issue | Expected UI behavior stated by the Issue |
| --- | --- |
| Required input is invalid or missing | Display a validation error. |
| `title` contains fewer than 5 characters | Apply the client-side validation rule for the minimum length. |
| `priority` is omitted | Use the default value `MED`. |

The Issue does not define validation behavior for `description`, UUID formatting, maximum title length, whitespace normalization, or unknown fields.

## 3. Data Design

### 3.1 PostgreSQL table design

The Issue defines one table, `work_orders`, with the following seven columns. This is a design reference only; it is not an implementation or migration script.

| Column | PostgreSQL type | Issue-defined constraint or meaning |
| --- | --- | --- |
| `id` | `UUID` | Primary key. |
| `title` | `VARCHAR(255)` | Work-order title. |
| `description` | `TEXT` | Work-order description. |
| `priority` | `VARCHAR(20)` | Work-order priority. |
| `status` | `VARCHAR(20)` | Initial value is `DRAFT`. |
| `customer_id` | `UUID` | Classified as internal PII. |
| `created_at` | `TIMESTAMPTZ` | Creation timestamp. |

### 3.2 DDL design reference

```sql
CREATE TABLE work_orders (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    priority VARCHAR(20),
    status VARCHAR(20),
    customer_id UUID,
    created_at TIMESTAMPTZ
);
```

The Issue does not define `NOT NULL`, `DEFAULT`, `CHECK`, foreign-key, index, trigger, or database-generated-value requirements. No such constraints are added here.

### 3.3 PII handling

- `customer_id` is classified as internal PII.
- Audit logging must not expose unnecessary sensitive information.

The Issue does not define a specific masking format, retention policy, audit event schema, or access-control implementation.

## 4. REST API Design

### 4.1 Endpoint and security

| Item | Issue-defined requirement |
| --- | --- |
| Method and path | `POST /api/v1/work-orders` |
| Authentication | OAuth2 Bearer Token |
| Required scope | `workorders:write` |
| Required response statuses | `201 Created`, `400 Bad Request`, `422 Unprocessable Entity` |

The Issue does not define additional endpoints or separate authentication failure contracts.

### 4.2 Request payload contract

The request payload contains only the form fields defined by the Issue:

```json
{
  "title": "string",
  "description": "string",
  "priority": "LOW | MED | HIGH",
  "customer_id": "UUID"
}
```

| Property | Type | Issue-defined rule |
| --- | --- | --- |
| `title` | string | Required; at least 5 characters. |
| `description` | string | No additional rule defined. |
| `priority` | string | `LOW`, `MED`, or `HIGH`; defaults to `MED` when omitted. |
| `customer_id` | UUID | No additional rule defined. |

The Issue does not define whether omitted and `null` are different, whether extra properties are rejected, or whether `status`, `id`, and `created_at` are accepted in a request. Those decisions remain unspecified.

### 4.3 Response contract

The Issue defines the following observable outcomes only:

| Scenario | Required result |
| --- | --- |
| Valid work-order inputs | Persist the work order with status `DRAFT`; return `201 Created`. |
| Missing `title` | Return `422 Unprocessable Entity` with a field-level validation error. |
| `title` shorter than 5 characters | Return `422 Unprocessable Entity`. |
| No `priority` provided | Default priority to `MED`. |

The Issue does not define a response-body schema, response headers, error JSON shape, field-error property names, or generated identifier/timestamp response behavior. No response payload is invented here.

## 5. Acceptance Mapping

| Issue acceptance criterion | Design mapping |
| --- | --- |
| Valid inputs produce a persisted work order with `DRAFT` status and HTTP `201 Created`. | API create operation and data status field. |
| Missing `title` produces HTTP `422` with a field-level validation error. | UI validation table and API validation outcome. |
| Title shorter than 5 characters produces HTTP `422`. | Title minimum-length rule. |
| Missing `priority` defaults to `MED`. | UI/request default rule. |

## 6. Explicitly Unspecified

The following are intentionally not designed because the Issue does not specify them:

- Exact UI layout and control widgets.
- Exact validation-message text.
- Maximum client-side title length or whitespace rules.
- Response JSON structure and headers.
- Error JSON structure and error codes.
- `401`/`403` response behavior.
- Database nullability, defaults, indexes, foreign keys, and triggers.
- Audit event structure and PII masking implementation.
- Update, read, dispatch, or status-transition APIs.