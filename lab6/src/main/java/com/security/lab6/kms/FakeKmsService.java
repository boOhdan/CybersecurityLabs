package com.security.lab6.kms;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

@Service
public class FakeKmsService implements IKmsService {
    private final SecureRandom secureRandom = new SecureRandom();
    private static final int GCM_IV_LENGTH = 12;
    private final int tLen = 128;

    private static final String AES = "AES/GCM/NoPadding";
    private String kek = "fTjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v";

    @SneakyThrows
    public String encryptDek(String dek) {
        if (dek == null) {
            return "";
        }

        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);
        var parameterSpec = new GCMParameterSpec(tLen, iv);

        var cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kek.getBytes(), "AES"), parameterSpec);

        byte[] cipherText = cipher.doFinal(dek.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);

        return new String(Base64.getEncoder().encode(byteBuffer.array()));
    }

    @SneakyThrows
    public String decryptDek(String dek) {
        if (dek == null) {
            return "";
        }

        byte[] cipherText = Base64.getDecoder().decode(dek);
        AlgorithmParameterSpec gcmIv = new GCMParameterSpec(tLen, cipherText, 0, GCM_IV_LENGTH);

        var cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kek.getBytes(), "AES"), gcmIv);

        byte[] plainText = cipher.doFinal(cipherText, GCM_IV_LENGTH, cipherText.length - GCM_IV_LENGTH);
        return new String(plainText);
    }
}
