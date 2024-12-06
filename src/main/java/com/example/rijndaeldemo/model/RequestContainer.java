package com.example.rijndaeldemo.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RequestContainer {
    private EncryptionRequest encryptionRequest = EncryptionRequest.builder().build();
    private DecryptionRequest decryptionRequest = DecryptionRequest.builder().build();

    private String encryptionResult;
    private String decryptionResult;

    private String encryptionError;
    private String decryptionError;
}
