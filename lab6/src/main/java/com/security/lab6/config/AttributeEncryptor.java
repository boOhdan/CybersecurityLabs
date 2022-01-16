package com.security.lab6.config;

import com.security.lab6.kms.FakeKmsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

@Component
@Converter
@RequiredArgsConstructor
public class AttributeEncryptor implements AttributeConverter<String, String> {

	private final Key key;
	private final SecureRandom secureRandom = new SecureRandom();
	private static final int GCM_IV_LENGTH = 12;
	private final int tLen = 128;

	private static final String AES = "AES/GCM/NoPadding";
	@Value("${secret.key.dek}")
	private String dek;
	private final FakeKmsService fakeKmsService;

	@SneakyThrows
	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null) {
			return "";
		}

		byte[] iv = new byte[GCM_IV_LENGTH];
		secureRandom.nextBytes(iv);
		var parameterSpec = new GCMParameterSpec(tLen, iv);

		var cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

		byte[] cipherText = cipher.doFinal(attribute.getBytes());
		ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
		byteBuffer.put(iv);
		byteBuffer.put(cipherText);

		return new String(Base64.getEncoder().encode(byteBuffer.array()));
	}

	@SneakyThrows
	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return "";
		}

		byte[] cipherText = Base64.getDecoder().decode(dbData);
		AlgorithmParameterSpec gcmIv = new GCMParameterSpec(tLen, cipherText, 0, GCM_IV_LENGTH);

		var cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.DECRYPT_MODE, key, gcmIv);

		byte[] plainText = cipher.doFinal(cipherText, GCM_IV_LENGTH, cipherText.length - GCM_IV_LENGTH);
		return new String(plainText);
	}
}
