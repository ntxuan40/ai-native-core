package com.example.demo;

import java.net.URI;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Service;

@RestController
@RequestMapping("/api/workorders")
public class ScratchHandler {
	private final WorkOrderService workOrderService;

	public ScratchHandler(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_TECHNICIAN')")
	public ResponseEntity<WorkOrderResponse> createWorkOrder(
			@Valid @RequestBody WorkOrderRequest request) {
		WorkOrderResponse response = workOrderService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}

@Service
final class WorkOrderService {
	WorkOrderResponse create(WorkOrderRequest request) {
		return new WorkOrderResponse(UUID.randomUUID(), request.title(), request.description());
	}
}

@JsonIgnoreProperties(ignoreUnknown = false)
record WorkOrderRequest(
		@NotBlank(message = "title must not be blank") String title,
		@NotBlank(message = "description must not be blank") String description) {
}

record WorkOrderResponse(UUID id, String title, String description) {
}

@RestControllerAdvice
final class WorkOrderProblemHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException exception) {
		String detail = exception.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(error -> error.getField() + " " + error.getDefaultMessage())
				.orElse("Request validation failed");
		return problemDetail(HttpStatus.BAD_REQUEST, "Invalid request", detail);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<ProblemDetail> handleUnreadableMessage(HttpMessageNotReadableException exception) {
		return problemDetail(HttpStatus.BAD_REQUEST, "Invalid request", "Request body must contain only title and description");
	}

	private ResponseEntity<ProblemDetail> problemDetail(HttpStatus status, String title, String detail) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setType(URI.create("https://example.com/problems/invalid-work-order"));
		problemDetail.setTitle(title);
		return ResponseEntity.status(status).body(problemDetail);
	}
}
