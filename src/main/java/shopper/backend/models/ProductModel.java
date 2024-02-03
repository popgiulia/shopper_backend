package shopper.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = {"type", "category", "sizeQuantities"})
@NoArgsConstructor
@Table(name="products")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private TypeModel type;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel category;

    @OneToMany(mappedBy = "product")
    private Set<SizeQuantityModel> sizeQuantities;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String image;

    public ProductModel(String name, String description, CategoryModel category, Double price, String image) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
    }
}
