package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shopper.backend.constants.SizeConstants;
import shopper.backend.dtos.constraints.SizeCreationDto;
import shopper.backend.dtos.responses.SizeResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.mappers.SizeMapper;
import shopper.backend.models.SizeModel;
import shopper.backend.repositories.SizeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SizeService {
    private final SizeMapper sizeMapper;
    private final SizeRepository sizeRepository;
    private final Logger logger = LoggerFactory.getLogger(SizeService.class);

    //Creează o nouă entitate SizeModel în baza de date.
    public SizeResponseDto create(SizeCreationDto sizeCreationDto) {
        String name = sizeCreationDto.getName();

        this.logger.info("Attempt to create size with name {}", name);

        // Verificare dacă există deja o entitate cu același nume în baza de date
        if(this.sizeRepository.findByName(name).isPresent()) {
            throw new ConflictException(SizeConstants.CREATED_ALREADY_EXISTS_BY_NAME);
        }

        SizeModel sizeModel = this.sizeMapper.toModel(sizeCreationDto);
        SizeModel createdSizeModel = this.sizeRepository.save(sizeModel);

        return this.sizeMapper.toDto(createdSizeModel);
    }

    //Obține o entitate SizeModel după nume.
    public Optional<SizeModel> getByNameModel(String name) {
        this.logger.info("Getting size with name {}", name);
        return this.sizeRepository.findByName(name);
    }
}
