package shopper.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;
import java.util.List;

@Getter
@Setter
@ToString
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String type;
    private Double price;
    private String category;
    private String description;

    //aici am aduagat asta
    private String image;



    private List<ProductSizeQuantityResponseDto> sizeQuantities;
}
