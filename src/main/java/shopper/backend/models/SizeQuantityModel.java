package shopper.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = {"size", "product"})
@NoArgsConstructor
@Table(name="size_quantities")
public class SizeQuantityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private SizeModel size;

    @Column(nullable = false)
    Integer quantity;

    public SizeQuantityModel(SizeModel size, Integer quantity, ProductModel product) {
        this.size = size;
        this.quantity = quantity;
        this.product = product;
    }
}
