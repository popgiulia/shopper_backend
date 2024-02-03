package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopper.backend.constants.CategoryConstants;
import shopper.backend.constants.ProductConstants;
import shopper.backend.constants.SizeConstants;
import shopper.backend.constants.TypeConstants;
import shopper.backend.dtos.constraints.ProductCreationDto;
import shopper.backend.dtos.constraints.SizeQuantityDto;
import shopper.backend.dtos.responses.ProductResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.exceptions.NotFoundException;
import shopper.backend.mappers.ProductMapper;
import shopper.backend.models.*;
import shopper.backend.repositories.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final TypeRepository typeRepository;
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SizeQuantityRepository sizeQuantityRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    //Creează un nou produs
    @Transactional
    public ProductResponseDto create(ProductCreationDto productCreationDto) {
        String name = productCreationDto.getName();

        this.logger.info("Attempt to create product with name {}", name);

        if(this.productRepository.findByName(name).isPresent()) {
            throw new ConflictException(ProductConstants.CREATED_ALREADY_EXISTS_BY_NAME);
        }

        TypeModel typeModel = this.typeRepository.findByName(productCreationDto.getType()).orElseThrow(
            () -> new NotFoundException(TypeConstants.NOT_FOUND_BY_NAME)
        );

        CategoryModel categoryModel = this.categoryRepository.findByName(productCreationDto.getCategory()).orElseThrow(
            () -> new NotFoundException(CategoryConstants.NOT_FOUND_BY_NAME)
        );

        ProductModel productModel = this.productMapper.toModel(productCreationDto);

        productModel.setType(typeModel);
        productModel.setCategory(categoryModel);

        ProductModel createdProductModel = this.productRepository.save(productModel);

        for(SizeQuantityDto sizeQuantityDto : productCreationDto.getSizeQuantities()) {
            SizeModel sizeModel = this.sizeRepository.findByName(sizeQuantityDto.getSize()).orElseThrow(
                    () -> new NotFoundException(SizeConstants.NOT_FOUND_BY_NAME)
            );

            this.sizeQuantityRepository.save(
                new SizeQuantityModel(
                    sizeModel,
                    sizeQuantityDto.getQuantity(),
                    createdProductModel
                )
            );
        }
        return this.productMapper.toDto(createdProductModel);
    }

    //Obține un model de produs pe baza numelui acestuia
    public Optional<ProductModel> getByNameModel(String name) {
        this.logger.info("Getting product with name {}", name);
        return this.productRepository.findByName(name);
    }

    //Obține DTO-ul unui produs pe baza identificatorului său unic.
    public ProductResponseDto getByIdDto(UUID id) {
        this.logger.info("Getting product with id {}", id);
        return this.productRepository.findById(id).map(this.productMapper::toDto).orElseThrow(
            () -> new NotFoundException(ProductConstants.NOT_FOUND_BY_ID)
        );
    }

    //Obține toate produsele grupate pe tip.
    public Map<String, Set<ProductResponseDto>> getAllGroupedByType(String category) {
        this.logger.info("Getting all products grouped by type");

        final String filterCategory = category == null ? this.categoryRepository.findAll().get(0).getName() : category.toLowerCase();

        return productRepository.findAll().stream()
            .filter(product -> product.getCategory().getName().toLowerCase().equals(filterCategory))
            .collect(Collectors.groupingBy(
                product -> product.getType().getName(),
                Collectors.mapping(productMapper::toDto, Collectors.toSet())
            ));
    }
}
