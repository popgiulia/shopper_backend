package shopper.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopper.backend.models.LoginTokenModel;
import shopper.backend.repositories.LoginTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginTokenService {
    private final LoginTokenRepository loginTokenRepository;
    private final Logger logger = LoggerFactory.getLogger(LoginTokenService.class);

    public LoginTokenModel create(LoginTokenModel loginTokenModel) {
        this.logger.info("Creating login token for user with email {}", loginTokenModel.getUser().getEmail());
        return this.loginTokenRepository.save(loginTokenModel);
    }

    public Optional<LoginTokenModel> getByToken(String token) {
        this.logger.info("Getting login token with token {}", token);
        return this.loginTokenRepository.findByToken(token);
    }

    @Transactional
    public void revokeToken(String token) {
        this.logger.info("Revoking token {}", token);
        Optional<LoginTokenModel> loginToken = this.getByToken(token);
        loginToken.ifPresent(tokenModel -> {
            tokenModel.setIsRevoked(true);
            this.loginTokenRepository.save(tokenModel);
        });
    }

    public void revokeAllTokensByUser(UUID userId) {
        this.logger.info("Revoking all tokens for user with id {}", userId);
        this.loginTokenRepository.revokeAllTokensByUserId(userId);
    }
}
