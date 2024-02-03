package shopper.backend.responses;

import shopper.backend.enums.Severity;

public class MessageResponsePayload extends BaseResponsePayload {
    public MessageResponsePayload(String message, Severity severity) {
        super(message, severity);
    }

    public static MessageResponsePayload success(String message) {
        return new MessageResponsePayload(message, Severity.SUCCESS);
    }

    public static MessageResponsePayload info(String message) {
        return new MessageResponsePayload(message, Severity.INFO);
    }
}
