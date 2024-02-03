package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopper.backend.constants.RoleConstants;
import shopper.backend.constants.UserConstants;
import shopper.backend.dtos.constraints.UserCreationDto;
import shopper.backend.dtos.responses.UserResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.exceptions.NotFoundException;
import shopper.backend.mappers.UserMapper;
import shopper.backend.models.ClientModel;
import shopper.backend.models.RoleModel;
import shopper.backend.models.UserModel;
import shopper.backend.repositories.RoleRepository;
import shopper.backend.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    //Creează un nou utilizator în baza de date.
    public UserResponseDto create(UserCreationDto userCreationDto) {
        final String email = userCreationDto.getEmail();

        this.logger.info("Attempt to create user with email {}", email);
        // Verificare dacă există deja un utilizator cu același email în baza de date
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException(UserConstants.CREATED_ALREADY_EXISTS_BY_EMAIL);
        }
        // Găsirea rolului corespunzător
        RoleModel roleModel = this.roleRepository.findByName(userCreationDto.getRole())
            .orElseThrow(() -> new NotFoundException(RoleConstants.NOT_FOUND_BY_NAME));
        // Criptarea parolei
        String hashedPassword = this.passwordEncoder.encode(userCreationDto.getPassword());
        // Convertirea din DTO în entitate
        UserModel userModel = this.userMapper.toModel(userCreationDto);

        userModel.setRole(roleModel);
        userModel.setPassword(hashedPassword);

        userModel.setClient(new ClientModel(userModel));
        //// Salvare utilizator în baza de date
        userModel = this.userRepository.save(userModel);
        // Convertirea din entitate în DTO pentru a fi returnată
        return this.userMapper.toDto(userModel);
    }

    //Creează un nou utilizator în baza de date.
    @Transactional
    public UserModel create(UserModel userModel) {
        this.logger.info("Creating user with email {} and role {}", userModel.getEmail(), userModel.getRole().getName());
        userModel.setPassword(this.passwordEncoder.encode(userModel.getPassword()));
        userModel = this.userRepository.save(userModel);
        return userModel;
    }

    public List<UserResponseDto> getAll() {
        this.logger.info("Getting all users");
        return this.userRepository.findAll().stream()
                .map(this.userMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDto> getById(UUID id) {
        this.logger.info("Getting user with id {}", id);
        return this.userRepository.findById(id).map(this.userMapper::toDto);
    }

    public Optional<UserResponseDto> getByEmailDto(String email) {
        this.logger.info("Getting user with email {}", email);
        return this.getByEmailModel(email).map(this.userMapper::toDto);
    }

    public Optional<UserModel> getByEmailModel(String email) {
        this.logger.info("Getting user with email {}", email);
        return this.userRepository.findByEmail(email);
    }

    public Optional<UserModel> getByUsernameModel(String username) {
        this.logger.info("Getting user with username {}", username);
        return this.userRepository.findByUsername(username);
    }

    public UserModel save(UserModel userModel) {
        this.logger.info("Saving user with email {}", userModel.getEmail());
        return this.userRepository.save(userModel);
    }
}
