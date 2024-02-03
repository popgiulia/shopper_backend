package shopper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import shopper.backend.dtos.constraints.ProductCreationDto;
import shopper.backend.dtos.responses.ProductResponseDto;
import shopper.backend.dtos.responses.ProductSizeQuantityResponseDto;
import shopper.backend.models.ProductModel;
import shopper.backend.models.SizeQuantityModel;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "sizeQuantities", ignore = true)
    ProductModel toModel(ProductCreationDto productCreationDto);

    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "category", source = "category.name")
    @Mapping(source = "sizeQuantities", target = "sizeQuantities", qualifiedByName = "sizeQuantitiesToDtos")
    ProductResponseDto toDto(ProductModel productModel);

    @Named("sizeQuantitiesToDtos")
    default List<ProductSizeQuantityResponseDto> sizeQuantitiesToDtos(Set<SizeQuantityModel> sizeQuantities) {
        if (sizeQuantities == null) {
            return null;
        }
        List<String> orderedSizes = List.of("XS", "S", "M", "L", "XL"); // Define the order here
        return sizeQuantities.stream()
            .sorted(Comparator.comparingInt(sq -> orderedSizes.indexOf(sq.getSize().getName())))
            .map(sizeQuantityModel -> new ProductSizeQuantityResponseDto(
                sizeQuantityModel.getSize().getName(),
                sizeQuantityModel.getQuantity()
            ))
            .collect(Collectors.toList());
    }
}