package com.security.lab6.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

@Component
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {

	private final Key key;
	private final Cipher cipher;

	public AttributeEncryptor(@Value("${secret.key}") String secretKey) throws Exception {
		String AES = "AES";
		key = new SecretKeySpec(secretKey.getBytes(), AES);
		cipher = Cipher.getInstance(AES);
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null) {
			return "";
		}

		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return "";
		}

		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			throw new IllegalStateException(e);
		}
	}
}