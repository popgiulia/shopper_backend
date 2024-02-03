package shopper.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@NoArgsConstructor
@Table(name="login_tokens")
public class LoginTokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isRevoked = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    public LoginTokenModel(UserModel user, String token) {
        this.user = user;
        this.token = token;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
