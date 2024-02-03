package shopper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopper.backend.models.SizeModel;

import java.util.Optional;
import java.util.UUID;

public interface SizeRepository extends JpaRepository<SizeModel, UUID> {
    Optional<SizeModel> findByName(String name);
}
