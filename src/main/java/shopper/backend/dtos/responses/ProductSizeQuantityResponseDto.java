package shopper.backend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductSizeQuantityResponseDto {
    private String size;
    private int quantity;
}
