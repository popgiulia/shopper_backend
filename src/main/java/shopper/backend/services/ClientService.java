package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopper.backend.constants.ClientConstants;
import shopper.backend.constants.RoleConstants;
import shopper.backend.dtos.constraints.ClientCreationDto;
import shopper.backend.dtos.responses.ClientResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.mappers.ClientMapper;
import shopper.backend.models.ClientModel;
import shopper.backend.models.RoleModel;
import shopper.backend.repositories.ClientRepository;
import shopper.backend.repositories.RoleRepository;
import shopper.backend.utils.GenerateUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientMapper clientMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    //Crearea unui nou client în sistem.
    @Transactional
    public ClientResponseDto create(ClientCreationDto clientCreationDto) {
        final String email = clientCreationDto.getEmail();

        this.logger.info("Attempt to create client with email {}", email);

        // Verificăm dacă există deja un client cu aceeași adresă de email în sistem
        if(this.clientRepository.findByUserEmail(email).isPresent()) {
            throw new ConflictException(ClientConstants.CREATED_ALREADY_EXISTS_BY_EMAIL);
        }
        // Generăm o parolă hash pentru client
        String hashedPassword = this.passwordEncoder.encode(clientCreationDto.getPassword());
        // Obținem modelul pentru rolul de client
        RoleModel roleModel = this.roleRepository.findByName(RoleConstants.Client)
            .orElseThrow(() -> new ConflictException(RoleConstants.NOT_FOUND_BY_NAME));
        // Convertim DTO-ul în model și setăm informațiile suplimentare
        ClientModel clientModel = this.clientMapper.toModel(clientCreationDto);

        clientModel.getUser().setRole(roleModel);
        clientModel.getUser().setPassword(hashedPassword);
        clientModel.getUser().setUsername(GenerateUtils.generateUsername(email));

        // Salvăm clientul în baza de date
        clientModel = this.clientRepository.save(clientModel);

        // Convertim modelul creat în DTO și îl returnăm
        return this.clientMapper.toDto(clientModel);
    }

    //O listă cu toți clienții sub formă de DTO-uri.
    public List<ClientResponseDto> getAll() {
        this.logger.info("Getting all clients");
        return this.clientRepository.findAll().stream()
            .map(this.clientMapper::toDto)
            .collect(Collectors.toList());
    }

    //Obținerea unui client după ID.
    public Optional<ClientResponseDto> getById(UUID id) {
        this.logger.info("Getting client with id {}", id);
        return this.clientRepository.findById(id)
            .map(this.clientMapper::toDto);
    }

    //Obținerea unui client după adresă de email.
    public Optional<ClientResponseDto> getByEmail(String email) {
        this.logger.info("Getting client with email {}", email);
        return this.clientRepository.findByUserEmail(email)
            .map(this.clientMapper::toDto);
    }
}
