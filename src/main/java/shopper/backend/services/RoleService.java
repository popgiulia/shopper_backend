package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shopper.backend.constants.RoleConstants;
import shopper.backend.dtos.constraints.RoleCreationDto;
import shopper.backend.dtos.responses.RoleResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.exceptions.NotFoundException;
import shopper.backend.mappers.RoleMapper;
import shopper.backend.models.RoleModel;
import shopper.backend.repositories.RoleRepository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleResponseDto create(RoleCreationDto roleCreationDto) {
        String name = roleCreationDto.getName();

        this.logger.info("Attempt to create role with name {}", name);

        if(this.roleRepository.findByName(name).isPresent()) {
            throw new ConflictException(RoleConstants.CREATED_ALREADY_EXISTS_BY_NAME);
        }

        RoleModel roleModel = roleMapper.toModel(roleCreationDto);
        RoleModel createdRoleModel = this.roleRepository.save(roleModel);

        return roleMapper.toDto(createdRoleModel);
    }

    public Optional<RoleResponseDto> getByNameDto(String name) {
        this.logger.info("Getting role with name {}", name);
        return this.roleRepository.findByName(name).map(this.roleMapper::toDto);
    }

    public Optional<RoleModel> getByNameModel(String name) {
        this.logger.info("Getting role with name {}", name);
        return this.roleRepository.findByName(name);
    }

    public List<RoleResponseDto> getAll() {
        this.logger.info("Getting all roles");
        return this.roleRepository.findAll().stream()
            .map(this.roleMapper::toDto)
            .collect(Collectors.toList());
    }

    public Optional<RoleResponseDto> getById(UUID id) {
        this.logger.info("Getting role with id {}", id);
        return this.roleRepository.findById(id).map(this.roleMapper::toDto);
    }

    public void deleteById(UUID id) {
        this.logger.info("Attempt to delete role with id {}", id);

        if(!this.roleRepository.existsById(id)) {
            throw new NotFoundException(RoleConstants.NOT_FOUND_BY_ID);
        }

        this.roleRepository.deleteById(id);
    }

    public void deleteAll() {
        this.logger.info("Deleting all roles");
        this.roleRepository.deleteAll();
    }
}
