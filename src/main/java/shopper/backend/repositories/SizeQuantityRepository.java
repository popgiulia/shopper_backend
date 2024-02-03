package shopper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopper.backend.models.SizeQuantityModel;

import java.util.UUID;

public interface SizeQuantityRepository extends JpaRepository<SizeQuantityModel, UUID> {
}