package shopper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import shopper.backend.models.LoginTokenModel;

import java.util.Optional;
import java.util.UUID;

public interface LoginTokenRepository extends JpaRepository<LoginTokenModel, UUID> {
    Optional<LoginTokenModel> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE LoginTokenModel lt SET lt.isRevoked = true WHERE lt.user.id = :userId")
    void revokeAllTokensByUserId(UUID userId);
}
