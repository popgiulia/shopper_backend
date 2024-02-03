package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shopper.backend.constants.TypeConstants;
import shopper.backend.dtos.constraints.TypeCreationDto;
import shopper.backend.dtos.responses.TypeResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.mappers.TypeMapper;
import shopper.backend.models.TypeModel;
import shopper.backend.repositories.TypeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeService {
    private final TypeMapper typeMapper;
    private final TypeRepository typeRepository;
    private final Logger logger = LoggerFactory.getLogger(TypeService.class);

    //Creează o nouă entitate TypeModel în baza de date.
    public TypeResponseDto create(TypeCreationDto typeCreationDto) {
        String name = typeCreationDto.getName();

        this.logger.info("Attempt to create type with name {}", name);
        // Verificare dacă există deja o entitate cu același nume în baza de date
        if(this.typeRepository.findByName(name).isPresent()) {
            throw new ConflictException(TypeConstants.CREATED_ALREADY_EXISTS_BY_NAME);
        }
        // Convertirea din DTO în entitate
        TypeModel typeModel = this.typeMapper.toModel(typeCreationDto);
        // Salvare entitate în baza de date
        TypeModel createdTypeModel = this.typeRepository.save(typeModel);
        // Convertirea din entitate în DTO pentru a fi returnată
        return this.typeMapper.toDto(createdTypeModel);
    }

    //Obține o entitate TypeModel după nume.
    public Optional<TypeModel> getByNameModel(String name) {
        this.logger.info("Getting type with name {}", name);
        return this.typeRepository.findByName(name);
    }
}
