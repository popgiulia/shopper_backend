package shopper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopper.backend.models.TypeModel;

import java.util.Optional;
import java.util.UUID;

public interface TypeRepository extends JpaRepository<TypeModel, UUID> {
    Optional<TypeModel> findByName(String name);
}
