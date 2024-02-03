package shopper.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = "products")
@NoArgsConstructor
@Table(name="categories")
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<ProductModel> products;

    public CategoryModel(String name) {
        this.name = name;
    }
}
