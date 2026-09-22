# Business Requirement Analysis - Construction Equipment Management (POSCO MCI)

### 1. Extracted Entities & Attributes
- **WorkOrder (Phiếu công việc):** `id` (Long), `equipmentCode` (String), `description` (String), `priority` (Enum: LOW, MEDIUM, HIGH), `status` (Enum: OPEN, IN_PROGRESS, CLOSED), `createdAt` (Timestamp), `createdBy` (String).
- **Equipment (Thiết bị công trường):** `equipmentCode` (String/Unique Key), `name` (String), `location` (String).

### 2. Open Questions for Stakeholders (Gap Analysis)
- **Validation:** What is the exact format of `equipmentCode`? (e.g., Prefix like "EQ-XXXX").
- **Business Logic:** Does the system need to verify if the `equipmentCode` actually exists in the POSCO inventory system before creating the work order?
- **Workflow:** Who is allowed to update or close the work order after the technician creates it?
- **Priority Rules:** Are there SLA time limits strictly tied to each priority level (e.g., HIGH priority must be resolved within 4 hours)?

### 3. Architecture Component Mapping (UI / Data / API)

| Layer | Component Name / Description | Tech Stack / Specifications |
| :--- | :--- | :--- |
| **UI** | Work Order Registration Form | Form with fields: Equipment Code (Dropdown/Text), Description (Textarea), Priority (Radio buttons) |
| **API** | `POST /api/workorders` | Handled by `ScratchHandler`. Accepts JSON payload with validation. Returns HTTP 201. |
| **Data**| `work_orders` Table | PostgreSQL or MySQL table mapping the `WorkOrder` entity with foreign key indexing. |
