package com.example.rijndaeldemo.controller;

import com.example.rijndaeldemo.model.DecryptionRequest;
import com.example.rijndaeldemo.model.EncryptionRequest;
import com.example.rijndaeldemo.model.RequestContainer;
import com.example.rijndaeldemo.service.RijndaelCipherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@SessionAttributes("requestContainer")
@Slf4j
public class RijndaelDemoController {
    private final RijndaelCipherService cipherService;
    private final RequestContainer requestContainer;
    @GetMapping
    public String home(Model model) {
        return "aes-demo";
    }

    @ModelAttribute("requestContainer")
    public RequestContainer createRequestcontainer() {
        return new RequestContainer();
    }

    @PostMapping("/encrypt")
    public String encrypt(@ModelAttribute("requestContainer") RequestContainer requestContainer, Model model) {
        EncryptionRequest request = requestContainer.getEncryptionRequest();
        try {
            String result = cipherService.encrypt(request);
            requestContainer.setEncryptionResult(result);
            requestContainer.setDecryptionError(null);
        } catch (Exception e) {
            log.error(String.format("Exception encrypting '%s' with key = '%s'. " + e.getMessage(),
                    request.getPlainText(), request.getSecretKey()));
            requestContainer.setEncryptionError(e.getMessage());
        }
        return "aes-demo";
    }

    @PostMapping("/decrypt")
    public String decrypt(@ModelAttribute("requestContainer") RequestContainer requestContainer, Model model) {
        DecryptionRequest request = requestContainer.getDecryptionRequest();
        try {
            String result = cipherService.decrypt(request);
            requestContainer.setDecryptionResult(result);
            requestContainer.setDecryptionError(null);
        } catch (Exception e) {
            log.error(String.format("Exception decrypting '%s' with key = '%s'. " + e.getMessage(),
                    request.getCipherText(), request.getSecretKey()));
            requestContainer.setDecryptionError(e.getMessage());
        }
        return "aes-demo";
    }
}
