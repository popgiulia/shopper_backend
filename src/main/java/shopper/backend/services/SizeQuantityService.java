package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shopper.backend.constants.CategoryConstants;
import shopper.backend.dtos.constraints.CategoryCreationDto;
import shopper.backend.dtos.responses.CategoryResponseDto;
import shopper.backend.dtos.responses.SizeQuantityResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.mappers.SizeQuantityMapper;
import shopper.backend.models.CategoryModel;
import shopper.backend.models.SizeQuantityModel;
import shopper.backend.repositories.SizeQuantityRepository;

@Service
@RequiredArgsConstructor
public class SizeQuantityService {
    private final SizeQuantityMapper sizeQuantityMapper;
    private final SizeQuantityRepository sizeQuantityRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    //Creează o nouă entitate SizeQuantityModel în baza de date.
    public SizeQuantityResponseDto create(SizeQuantityModel sizeQuantityModel) {
        this.logger.info("Attempt to create size quantity");
        // Salvare entitate în baza de date
        SizeQuantityModel createSizeQuantityModel = this.sizeQuantityRepository.save(sizeQuantityModel);
        // Mapare entitate la DTO pentru a fi returnată
        return this.sizeQuantityMapper.toDto(createSizeQuantityModel);
    }
}
