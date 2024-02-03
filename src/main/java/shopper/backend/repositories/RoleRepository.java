package shopper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopper.backend.models.RoleModel;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findByName(String name);
}