package shopper.backend.responses;

import shopper.backend.enums.Severity;

public class ErrorResponsePayload extends BaseResponsePayload{
    public ErrorResponsePayload(String message) {
        super(message, Severity.ERROR);
    }
}
