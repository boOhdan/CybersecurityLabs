package com.security.lab6.kms;

public interface IKmsService {
    String encryptDek(String dek);
    String decryptDek(String dek);
}
