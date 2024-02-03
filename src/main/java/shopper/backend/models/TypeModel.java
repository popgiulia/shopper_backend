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
@ToString(exclude = "products")
@NoArgsConstructor
@Table(name="types")
public class TypeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "type")
    private Set<ProductModel> products;

    public TypeModel(String name) {
        this.name = name;
    }
}
