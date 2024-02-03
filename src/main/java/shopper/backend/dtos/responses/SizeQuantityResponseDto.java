package shopper.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class SizeQuantityResponseDto {
    private UUID id;
    private String size;
    private int quantity;
    private String product;
}
