package shopper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopper.backend.models.CategoryModel;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {
    Optional<CategoryModel> findByName(String name);
}
