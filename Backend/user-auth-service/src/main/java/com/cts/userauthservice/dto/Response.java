package com.cts.userauthservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class Response {
	private String status;
	private String message;
}
