package lab1.task5;

import lab1.utils.Utils;

import java.util.List;

public class SubstitutionDecoder {

	public static String decrypt(String encryptedText, List<String> keys) {
		StringBuilder decryptedText = new StringBuilder();
		char[] chars = encryptedText.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			int index = Utils.ALPHABET.indexOf(chars[i]);
			decryptedText.append(keys.get(i % keys.size()).charAt(index));
		}
		return decryptedText.toString();
	}
}
