package com.example.rijndaeldemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DecryptionRequest {

    private String cipherText;
    private String secretKey;

    @Builder.Default
    private OutputFormat outputFormat = OutputFormat.Plaintext;

    @Builder.Default
    private int keySize = 128;

    public enum OutputFormat {
        Base64, Plaintext
    }
}
