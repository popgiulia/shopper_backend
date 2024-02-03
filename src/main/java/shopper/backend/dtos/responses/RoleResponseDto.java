package shopper.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class RoleResponseDto {
    private UUID id;
    private String name;
}