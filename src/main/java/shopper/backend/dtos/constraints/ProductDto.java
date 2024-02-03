package shopper.backend.dtos.constraints;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class ProductDto {
    protected String name;
    protected String description;
    protected String type;
    protected String category;
    protected float price;
    protected String image;
    protected List<SizeQuantityDto> sizeQuantities;
}
