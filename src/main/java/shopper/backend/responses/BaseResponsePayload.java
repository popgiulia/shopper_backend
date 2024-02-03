package shopper.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shopper.backend.enums.Severity;

@Getter
@AllArgsConstructor
public abstract class BaseResponsePayload {

    //Clasa abstractă care furnizează proprietăți de bază pentru toate răspunsurile din aplicație, cum ar fi mesajul și gravitatea.
    private final String message;
    private final Severity severity;
    public String getSeverity() {
        return severity.name().toLowerCase();
    }
}
