package com.example.rijndaeldemo.service;

import com.example.rijndaeldemo.model.DecryptionRequest;
import com.example.rijndaeldemo.model.EncryptionRequest;
import java.util.Base64;
import org.digitalleague.cipher.Cipher;
import org.digitalleague.cipher.impl.CipherImpl;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Service
public class RijndaelCipherService {
    public String encrypt(EncryptionRequest encryptionRequest) {
        byte[] keyBytes = encryptionRequest.getSecretKey().getBytes(StandardCharsets.UTF_8);
        if (encryptionRequest.getKeySize() != keyBytes.length * 8) {
            throw new IllegalArgumentException(String.format("Expected key length: %s bits, actual key length: %s bits",
                    encryptionRequest.getKeySize(), keyBytes.length * 8));
        }
        Cipher cipher = CipherImpl.createInstance(keyBytes);
        byte[] plainTextBytes = encryptionRequest.getPlainText().getBytes(StandardCharsets.UTF_8);
        byte[] result = cipher.encrypt(plainTextBytes);
        switch (encryptionRequest.getOutputFormat()) {
            case Base64 -> {
                return Base64.getEncoder().encodeToString(result);
            }
            case Hex -> {
                return HexFormat.of().formatHex(result);
            }
        }
        return null;
    }

    public String decrypt(DecryptionRequest decryptionRequest) {
        byte[] keyBytes = decryptionRequest.getSecretKey().getBytes(StandardCharsets.UTF_8);
        byte[] decrypted;

        if (decryptionRequest.getKeySize() != keyBytes.length * 8) {
            throw new IllegalArgumentException(String.format("Expected key length: %s bits, actual key length: %s bits",
                            decryptionRequest.getKeySize(), keyBytes.length * 8));
        }

        Cipher cipher = CipherImpl.createInstance(keyBytes);

        try {
            if (decryptionRequest.getCipherText().matches("-?[0-9a-fA-F]+")) {
                byte[] hexBytes = HexFormat.of().parseHex(decryptionRequest.getCipherText());
                decrypted = cipher.decrypt(hexBytes);
            } else {
                byte[] base64decodedBytes = Base64.getDecoder().decode(decryptionRequest.getCipherText());
                decrypted = cipher.decrypt(base64decodedBytes);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal ciphertext: " + decryptionRequest.getCipherText());
        }

        switch (decryptionRequest.getOutputFormat()) {
            case Base64 -> {
                return Base64.getEncoder().encodeToString(decrypted);
            }
            case Plaintext -> {
                return new String(decrypted, StandardCharsets.UTF_8);
            }
        }
        return null;
    }
}
