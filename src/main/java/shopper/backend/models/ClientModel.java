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
@ToString(exclude = "user")
@NoArgsConstructor
@Table(name="clients")
public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @MapsId
    @JoinColumn(name = "user_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserModel user;

    public ClientModel(UserModel user) {
        this.user = user;
    }
}