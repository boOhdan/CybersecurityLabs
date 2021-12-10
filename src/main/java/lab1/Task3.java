package lab1;

import lab1.config.Config;
import lab1.utils.KeyGenerator;
import lab1.utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task3 {

	private static final String SOURCE = Config.RESOURCES + "task3 source.txt";
	private static final String DESTINATION = Config.RESOURCES + "task3.txt";
	private static final String DESTINATION_PATTERN = Config.RESOURCES + "task3 %d.txt";
	private static final String I_C = Config.RESOURCES + "task3 I.C.txt";
	private static final String OFFSETS = Config.RESOURCES + "task3 offsets.txt";

	private static final String KEY = "";

	private static final char[][] possibleKeySymbols = {
			{1, 32, 33, 34, 40, 45},
			{15, 16},
			{0, 1},
			{0, 1}
	};

	//	private static final Pattern wordPattern = Pattern.compile("^[\\s\\w\\d?><;,{}\\[\\]\\-_+=!@#$%^&*|']*$");
//	private static final Pattern wordPattern = Pattern.compile("^[\\p{Alnum}\\p{Space} !\"#'()*+,./:;<=>_`{|}~]+$");
	private static final Pattern linePattern = Pattern.compile("^[\\p{Graph}\\p{Space}]+$");
	private static final Pattern wordPattern = Pattern.compile("\\p{Alpha}+");
	private static final Set<Integer> availableDecryptedSymbols;
	private static final Set<Integer> englishSymbols;
	private static final double MATCH_PERCENTAGE = 0.85;

	static {
		availableDecryptedSymbols = IntStream.range(35, 127).boxed().collect(Collectors.toCollection(TreeSet::new));
		englishSymbols = new TreeSet<>();
		IntStream.range(65, 91).boxed().forEach(englishSymbols::add);
		IntStream.range(97, 123).boxed().forEach(englishSymbols::add);
	}

	public static void main(String[] args) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
//		encryptedText = new String(Base64.getDecoder().decode(encryptedText.getBytes()));
//		bruteforceSearch(encryptedText, 16);
	}

	private static void bruteforceSearch(String encryptedText, int keyLength) throws IOException {
		char[][] possibleKeySymbols = getPossibleKeySymbols(encryptedText, keyLength);
		KeyGenerator keyGenerator = new KeyGenerator(possibleKeySymbols);

		try (PrintWriter writer = new PrintWriter(new FileWriter(DESTINATION))) {
			for (String key : keyGenerator) {
				String decryptedText = decrypt(encryptedText, key);
				if (matchesWords(decryptedText)) {
					writer.println(key);
					writer.println(decryptedText);
					writer.println();
				}
			}
		}
	}

	private static void bruteforceSearch(String encryptedText, int keyLength, PrintWriter writer) {
		char[][] possibleKeySymbols = getPossibleKeySymbols(encryptedText, keyLength);
		KeyGenerator keyGenerator = new KeyGenerator(possibleKeySymbols);

		for (String key : keyGenerator) {
			String decryptedText = decrypt(encryptedText, key);
			if (matchesWords(decryptedText)) {
				writer.println(key);
				writer.println(decryptedText);
				writer.println();
			}
		}
	}

	private static char[][] getPossibleKeySymbols(String encryptedText, int keyLength) {
		char[][] possibleKeySymbols = new char[keyLength][];
		List<String> partsOfText = Utils.splitText(encryptedText, keyLength);
		for (int i = 0; i < keyLength; i++) {
			Set<Character> possiblePartKeys = new HashSet<>();
			for (int j = 0; j < 256; j++) {
				String decryptedPart = decrypt(partsOfText.get(i), "" + (char) j);
				if (matchesSymbols(decryptedPart)) {
					possiblePartKeys.add((char) j);
				}
			}

			List<Character> listOfSymbols = possiblePartKeys.stream()
					.filter(s -> availableDecryptedSymbols.contains((int) s))
					.collect(Collectors.toList());

			if (listOfSymbols.isEmpty()) {
				listOfSymbols.add((char) 0);
			}
			possibleKeySymbols[i] = Utils.toCharArray(listOfSymbols);
		}
		return possibleKeySymbols;
	}

	private static String decrypt(String text, String key) {
		StringBuilder decryptedText = new StringBuilder();
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			decryptedText.append((char) (chars[i] ^ key.charAt(i % key.length())));
		}

		return decryptedText.toString();
	}

	private static boolean matchesSymbols(String text) {
		return linePattern.matcher(text).matches();
	}

	private static boolean matchesWords(String text) {
		var matcher = wordPattern.matcher(text);
		int matchLength = 0;
		while (matcher.find()) {
			matchLength += matcher.group().length();
		}

		return matchLength >= text.length() * MATCH_PERCENTAGE;
	}

	private static void countSymbolFrequencies(int keyLength) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
		try (PrintWriter writer = new PrintWriter(new FileWriter(Config.RESOURCES + "task3 symbol frequencies.txt"))) {
			List<String> partsOfText = Utils.splitText(encryptedText, 8);
			partsOfText.forEach(t -> {
				Utils.countSymbolsFrequency(t, writer);
				writer.println();
				writer.println();
			});
		}
	}
}
