package com.gamenest.model.enums;

public enum RsaErrorCode {
    ALGORITHM_NOT_FOUND("Algorithm not found"),
    INVALID_PUBLIC_KEY("Invalid public key"),
    PADDING_SCHEME_NOT_SUPPORTED("Padding scheme not supported"),
    ILLEGAL_SIZE_ENCRYPTION("Illegal block size for encryption"),
    BAD_PADDING_ENCRYPTION("Bad padding during encryption"),
    ERROR_DURING_ENCRYPTION("Error during encryption");

    private final String message;

    RsaErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
