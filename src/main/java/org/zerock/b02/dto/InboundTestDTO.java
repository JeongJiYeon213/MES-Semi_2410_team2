package org.zerock.b02.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InboundTestDTO {
	private Long inboundId;
	private Long stockId;
	private String productCode;
	private Long plus;
}
