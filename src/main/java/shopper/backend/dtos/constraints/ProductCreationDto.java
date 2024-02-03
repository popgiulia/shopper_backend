package shopper.backend.dtos.constraints;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreationDto extends ProductDto{
    private String image;
}
