package org.zerock.b02.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboundTestDTO {
	private Long outboundId;
	private Long stockId;
	private String productCode;
	private Long minus;
}
