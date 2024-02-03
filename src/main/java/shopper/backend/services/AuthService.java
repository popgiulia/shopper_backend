package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopper.backend.constants.AuthConstants;
import shopper.backend.constants.ClientConstants;
import shopper.backend.constants.RoleConstants;
import shopper.backend.constants.UserConstants;
import shopper.backend.dtos.requests.AuthLoginRequestDto;
import shopper.backend.dtos.requests.AuthRegisterRequestDto;
import shopper.backend.dtos.responses.AuthLoginResponseDto;
import shopper.backend.dtos.responses.UserResponseDto;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.exceptions.NotFoundException;
import shopper.backend.exceptions.UnauthorizedException;
import shopper.backend.mappers.ClientMapper;
import shopper.backend.mappers.UserMapper;
import shopper.backend.models.ClientModel;
import shopper.backend.models.LoginTokenModel;
import shopper.backend.models.RoleModel;
import shopper.backend.models.UserModel;
import shopper.backend.repositories.ClientRepository;
import shopper.backend.repositories.RoleRepository;
import shopper.backend.repositories.UserRepository;
import shopper.backend.security.JwtService;
import shopper.backend.utils.GenerateUtils;
/**
 * Serviciu responsabil pentru gestionarea autentificării și înregistrării utilizatorilor în sistem.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginTokenService loginTokenService;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    //Înregistrarea unui nou utilizator în sistem.
    @Transactional
    public void register(AuthRegisterRequestDto authRegisterRequestDto) {
        final String email = authRegisterRequestDto.getEmail();

        this.logger.info("Attempt to register client with email {}", email);

        // Verificăm dacă există deja un client înregistrat cu această adresă de email
        if(this.clientRepository.findByUserEmail(email).isPresent()) {
            throw new ConflictException(ClientConstants.CREATED_ALREADY_EXISTS_BY_EMAIL);
        }

        // Obținem modelul pentru rolul "Client"
        RoleModel roleModel = this.roleRepository.findByName(RoleConstants.Client)
            .orElseThrow(() -> new NotFoundException(RoleConstants.NOT_FOUND_BY_NAME));

        // Creăm modelul de utilizator folosind informațiile din DTO
        UserModel userModel = this.userMapper.toModel(authRegisterRequestDto);

        // Setăm rolul și generăm un nume de utilizator unic
        userModel.setRole(roleModel);
        userModel.setUsername(GenerateUtils.generateUsername(email));
        // Criptăm parola și asociem modelul de client
        userModel.setPassword(this.passwordEncoder.encode(authRegisterRequestDto.getPassword()));
        ClientModel clientModel = new ClientModel(userModel);

        // Salvăm în baza de date
        this.clientRepository.save(clientModel);
    }

    //Procesul de autentificare a utilizatorului.
    @Transactional
    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        final String email = authLoginRequestDto.getEmail();

        this.logger.info("Attempt to login user with email {}", email);

        // Extragem informațiile din DTO
        final String password = authLoginRequestDto.getPassword();

        // Obținem modelul de utilizator asociat adresei de email
        UserModel userModel = this.userRepository.findByEmail(email).orElseThrow(() ->
            new NotFoundException(UserConstants.NOT_FOUND_BY_EMAIL)
        );

        //Verificăm dacă parola furnizată corespunde celei asociate utilizatorului
        if(!this.passwordEncoder.matches(password, userModel.getPassword())) {
            throw new UnauthorizedException(AuthConstants.INVALID_CREDENTIALS);
        }

        UserDetails userDetails = this.userMapper.toUserDetails(userModel);

        String token = this.jwtService.generateAccessToken(userDetails);

        // Salvăm token-ul de acces în baza de date și revocăm toate celelalte token-uri pentru același utilizator
        LoginTokenModel loginTokenModel = new LoginTokenModel(userModel, token);
        this.loginTokenService.revokeAllTokensByUser(userModel.getId());
        this.loginTokenService.create(loginTokenModel);

        // Generăm token-uri de acces și de refresh
        String accessToken = this.jwtService.generateAccessToken(userDetails);
        String refreshToken = this.jwtService.generateRefreshToken(userDetails);

        return new AuthLoginResponseDto(accessToken, refreshToken);
    }

    //Obținerea informațiilor despre utilizatorul autentificat.
    public UserResponseDto getLoggedUser() {
        this.logger.info("Getting logged in user");

        // Obținem principiul asociat autentificării curente
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Verificăm dacă principiul este o instanță a UserDetails
        if(!(principal instanceof UserDetails userDetails)) {
            throw new UnauthorizedException(AuthConstants.NOT_LOGGED_IN);
        }

        // Obținem modelul de utilizator asociat numelui de utilizator din UserDetails
        UserModel userModel = this.userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
            new NotFoundException(UserConstants.NOT_FOUND_BY_USERNAME)
        );

        // Convertim modelul în DTO și îl returnăm
        return userMapper.toDto(userModel);
    }
}
