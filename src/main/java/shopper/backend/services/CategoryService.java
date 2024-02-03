package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shopper.backend.constants.CategoryConstants;
import shopper.backend.dtos.constraints.CategoryCreationDto;
import shopper.backend.dtos.responses.CategoryResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.mappers.CategoryMapper;
import shopper.backend.models.CategoryModel;
import shopper.backend.repositories.CategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    //Crearea unei noi categorii în sistem.
    public CategoryResponseDto create(CategoryCreationDto categoryCreationDto) {
        String name = categoryCreationDto.getName();

        this.logger.info("Attempt to create category with name {}", name);

        // Verificăm dacă există deja o categorie cu același nume în sistem
        if(this.categoryRepository.findByName(name).isPresent()) {
            throw new ConflictException(CategoryConstants.CREATED_ALREADY_EXISTS_BY_NAME);
        }
        // Convertim DTO-ul în model și salvăm în baza de date
        CategoryModel categoryModel = this.categoryMapper.toModel(categoryCreationDto);
        CategoryModel createCategoryModel = this.categoryRepository.save(categoryModel);
        // Convertim modelul creat în DTO și îl returnăm
        return this.categoryMapper.toDto(createCategoryModel);
    }

    //Obținerea unei categorii după nume.
    public Optional<CategoryModel> getByNameModel(String name) {
        this.logger.info("Getting category with name {}", name);
        return this.categoryRepository.findByName(name);
    }
}
