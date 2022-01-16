package com.security.lab6.kms;

import org.springframework.stereotype.Service;

@Service
public class FakeKmsService {
    private String kek = "fTjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v";

    public String encryptDek(String dek) {
        return "";
    }

    public String decryptDek(String dek) {
        return "";
    }
}
