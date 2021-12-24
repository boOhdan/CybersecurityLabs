package lab1.task4;

import lab1.utils.Utils;

public class SubstitutionDecoder {

	public static String decrypt(String encryptedText, String key) {
		StringBuilder decryptedText = new StringBuilder();
		for (char symbol : encryptedText.toCharArray()) {
			int index = Utils.ALPHABET.indexOf(symbol);
			decryptedText.append(key.charAt(index));
		}
		return decryptedText.toString();
	}
}
