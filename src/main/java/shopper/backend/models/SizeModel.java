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
@ToString(exclude = "sizeQuantities")
@NoArgsConstructor
@Table(name="sizes")
public class SizeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "size", cascade = CascadeType.ALL)
    private Set<SizeQuantityModel> sizeQuantities;

    public SizeModel(String name) {
        this.name = name;
    }
}
