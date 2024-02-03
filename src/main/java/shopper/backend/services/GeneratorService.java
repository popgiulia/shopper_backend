package shopper.backend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shopper.backend.constants.*;
import shopper.backend.dtos.files.*;
import shopper.backend.enums.Severity;
import shopper.backend.models.*;
import shopper.backend.responses.MessageResponsePayload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneratorService {
    private final Logger logger = LoggerFactory.getLogger(GeneratorService.class);
    private final RoleService roleService;
    private final SizeService sizeService;
    private final TypeService typeService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public MessageResponsePayload generateRoles() {
        this.logger.info("Generating all roles from json file...");

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/roles.json")) {
            ObjectMapper mapper = new ObjectMapper();
            List<RoleSeedDto> roleSeedDtos = mapper.readValue(inputStream, new TypeReference<>() {});

            int newRolesCount = 0;

            for (RoleSeedDto roleSeedDto : roleSeedDtos) {
                Optional<RoleModel> optionalRoleModel = this.roleService.getByNameModel(roleSeedDto.getName());

                if(optionalRoleModel.isEmpty()) {
                    logger.info("Generating role with name {} from json file...", roleSeedDto.getName());
                    this.roleService.create(roleSeedDto);
                    newRolesCount++;
                }
            }

            if (newRolesCount > 0) {
                this.logger.info(String.format("Generated %d new roles from json file.", newRolesCount));
            } else {
                this.logger.info("No new roles found to generate from json file.");
            }

            return new MessageResponsePayload(
                String.format(RoleConstants.GENERATED_SUCCESS, newRolesCount),
                Severity.SUCCESS
            );
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageResponsePayload generateSizes() {
        this.logger.info("Generating all sizes from json file...");

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/sizes.json")) {
            ObjectMapper mapper = new ObjectMapper();
            List<SizeSeedDto> sizeSeedDtos = mapper.readValue(inputStream, new TypeReference<>() {});

            int newSizes = 0;

            for (SizeSeedDto sizeSeedDto : sizeSeedDtos) {
                Optional<SizeModel> optionalSizeModel = this.sizeService.getByNameModel(sizeSeedDto.getName());

                if(optionalSizeModel.isEmpty()) {
                    this.logger.info("Generating size with name {} from json file...", sizeSeedDto.getName());
                    this.sizeService.create(sizeSeedDto);
                    newSizes++;
                }
            }

            if (newSizes > 0) {
                this.logger.info(String.format("Generated %d new sizes from json file.", newSizes));
            } else {
                this.logger.info("No new sizes found to generate from json file.");
            }

            return new MessageResponsePayload(
                String.format(SizeConstants.GENERATED_SUCCESS, newSizes),
                Severity.SUCCESS
            );
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageResponsePayload generateTypes() {
        this.logger.info("Generating all types from json file...");

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/types.json")) {
            ObjectMapper mapper = new ObjectMapper();
            List<TypeSeedDto> typeSeedDtos = mapper.readValue(inputStream, new TypeReference<>() {});

            int newTypes = 0;

            for (TypeSeedDto typeSeedDto : typeSeedDtos) {
                Optional<TypeModel> optionalTypeModel = this.typeService.getByNameModel(typeSeedDto.getName());

                if(optionalTypeModel.isEmpty()) {
                    this.logger.info("Generating type with name {} from json file...", typeSeedDto.getName());
                    this.typeService.create(typeSeedDto);
                    newTypes++;
                }
            }

            if (newTypes > 0) {
                this.logger.info(String.format("Generated %d new types from json file.", newTypes));
            } else {
                this.logger.info("No new types found to generate from json file.");
            }

            return new MessageResponsePayload(
                String.format(TypeConstants.GENERATED_SUCCESS, newTypes),
                Severity.SUCCESS
            );
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageResponsePayload generateProducts() {
        this.logger.info("Generating all products from json file...");

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/products.json")) {
            ObjectMapper mapper = new ObjectMapper();
            List<ProductSeedDto> productSeedDtos = mapper.readValue(inputStream, new TypeReference<>() {});

            int newProducts = 0;

            for (ProductSeedDto productSeedDto : productSeedDtos) {
                Optional<ProductModel> optionalProductModel = this.productService.getByNameModel(productSeedDto.getName());

                if(optionalProductModel.isEmpty()) {
                    this.logger.info("Generating product with name {} from json file...", productSeedDto.getName());
                    this.productService.create(productSeedDto);
                    newProducts++;
                }
            }

            if (newProducts > 0) {
                this.logger.info(String.format("Generated %d new products from json file.", newProducts));
            } else {
                this.logger.info("No new products found to generate from json file.");
            }

            return new MessageResponsePayload(
                String.format(ProductConstants.GENERATED_SUCCESS, newProducts),
                Severity.SUCCESS
            );
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageResponsePayload generateCategories() {
        this.logger.info("Generating all categories from json file...");

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/categories.json")) {
            ObjectMapper mapper = new ObjectMapper();
            List<CategorySeedDto> categorySeedDtos = mapper.readValue(inputStream, new TypeReference<>() {});

            int newCategories = 0;

            for (CategorySeedDto categorySeedDto : categorySeedDtos) {
                Optional<CategoryModel> optionalCategoryModel = this.categoryService.getByNameModel(categorySeedDto.getName());

                if(optionalCategoryModel.isEmpty()) {
                    this.logger.info("Generating category with name {} from json file...", categorySeedDto.getName());
                    this.categoryService.create(categorySeedDto);
                    newCategories++;
                }
            }

            if (newCategories > 0) {
                this.logger.info(String.format("Generated %d new categories from json file.", newCategories));
            } else {
                this.logger.info("No new categories found to generate from json file.");
            }

            return new MessageResponsePayload(
                String.format(CategoryConstants.GENERATED_SUCCESS, newCategories),
                Severity.SUCCESS
            );
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
