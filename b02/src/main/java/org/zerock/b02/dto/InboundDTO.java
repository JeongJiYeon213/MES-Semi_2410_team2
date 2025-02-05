package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.b02.domain.Inbound;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InboundDTO {
    private Long inboundId;
    @NotEmpty
    private Long productId;
    @NotEmpty
    private Long supplierId;
    @NotEmpty
    private Long quantity;

    private LocalDateTime inboundDate;
    private String description;
    @NotEmpty
    private Status inboundStatus;

    public enum Status{
        PENDING,
        COMPLETED,
        CANCELED
    }
}
