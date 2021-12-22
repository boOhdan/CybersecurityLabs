package lab1.task5;

import lab1.utils.Utils;

import java.util.*;

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

	public static List<GuessedKeySymbol> getKeysPart(String encryptedText, String decryptedPart, int numberOfKeys) {
		Set<GuessedKeySymbol> result = new HashSet<>();
		for (int i = 0; i < decryptedPart.length(); i++) {
			var guessedSymbol = new GuessedKeySymbol()
					.setSymbol(decryptedPart.charAt(i))
					.setIndex(Utils.ALPHABET.indexOf(encryptedText.charAt(i)))
					.setGene(i % numberOfKeys);
			result.add(guessedSymbol);
		}

		var list = new ArrayList<>(result);
		list.sort(Comparator.comparingInt(GuessedKeySymbol::getGene).thenComparing(GuessedKeySymbol::getSymbol));
		return list;
	}
}
