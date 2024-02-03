package shopper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopper.backend.models.ClientModel;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientModel, UUID> {
    Optional<ClientModel> findByUserEmail(String email);
}
