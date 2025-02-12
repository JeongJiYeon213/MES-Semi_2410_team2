package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    @Id
    @Size(min = 1, max = 100)
    @NotNull(message = "ID cannot be null")
    private String customerId;

    @NotEmpty
    @Size(min = 1, max = 10)
    private String customerName;

    @NotEmpty
    @Size(min = 1, max = 8)
    private String customerInfo;

    private String customerSize;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
