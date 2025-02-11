package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private Long productId;

	@NotEmpty
	@Size(min = 3, max = 12)
	private String productCode;
	@NotEmpty
	@Size(min = 3, max = 100)
	private String productName;
	@NotEmpty
	@Size(min = 3, max = 50)
	private String productType;
	@NotNull(message = "Weight cannot be null")
	private Long weight;
	@NotEmpty
	private String size;
	private LocalDateTime regDate;
	private LocalDateTime modDate;
}
