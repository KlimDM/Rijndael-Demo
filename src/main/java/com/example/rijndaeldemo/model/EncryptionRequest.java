package com.example.rijndaeldemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncryptionRequest {

    private String plainText;

    @Builder.Default
    private int keySize = 128;

    private String secretKey;

    @Builder.Default
    private OutputFormat outputFormat = OutputFormat.Base64;

    public enum OutputFormat {
        Hex, Base64
    }
}
