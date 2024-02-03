package shopper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopper.backend.models.ProductModel;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    Optional<ProductModel> findByName(String name);
}