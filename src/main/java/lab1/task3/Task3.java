package lab1.task3;

import lab1.config.Config;
import lab1.utils.KeyGenerator;
import lab1.utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task3 {

	private static final String SOURCE = Config.RESOURCES + "task3 source.txt";
	private static final String DESTINATION = Config.RESOURCES + "task3.txt";
	private static final String DESTINATION_PATTERN = Config.RESOURCES + "task3 %d.txt";
	private static final String I_C = Config.RESOURCES + "task3 I.C.txt";
	private static final String OFFSETS = Config.RESOURCES + "task3 offsets.txt";

	private static final Pattern linePattern = Pattern.compile("^[\\p{Graph}\\p{Space}]+$");
	private static final Pattern wordPattern = Pattern.compile("[\\p{Alpha} ]+");
	private static final Set<Integer> availableDecryptedSymbols;
	private static final double MATCH_PERCENTAGE = 0.9;

	private static final long MAX_KEYS_COUNT = 20_000_000_000L;

	private static final String KEY = "L0l";

	static {
		availableDecryptedSymbols = IntStream.range(35, 128).boxed().collect(Collectors.toCollection(TreeSet::new));
	}

	public static void main(String[] args) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
		encryptedText = new String(Base64.getDecoder().decode(encryptedText.getBytes()));
		ForkJoinDecipher.encryptedText = encryptedText;

		long start = System.currentTimeMillis();
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		for (int i = 3; i <= 3; i++) {
			char[][] possibleKeySymbols = getPossibleKeySymbols(encryptedText, i);
			KeyGenerator keyGenerator = new KeyGenerator(possibleKeySymbols);
			if (keyGenerator.getKeysCount() > MAX_KEYS_COUNT) {
				continue;
			}
			ForkJoinDecipher.totalKeys = keyGenerator.getKeysCount();

			Map<String, String> result = forkJoinPool.invoke(new ForkJoinDecipher(keyGenerator));
			try (PrintWriter writer = new PrintWriter(new FileWriter(String.format(DESTINATION_PATTERN, i)))) {
				result.forEach((k, v) -> {
					writer.println(k);
					writer.println(v);
					writer.println();
				});
			}
		}
		long finish = System.currentTimeMillis();
		System.out.println(finish - start);
	}

	private static void bruteforceSearch(String encryptedText, int keyLength) throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(DESTINATION))) {
			bruteforceSearch(encryptedText, keyLength, writer);
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
				if (matchesSymbols(decryptedPart) && availableDecryptedSymbols.contains(j)) {
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

	public static String decrypt(String text, String key) {
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

	public static boolean matchesWords(String text) {
		var matcher = wordPattern.matcher(text);
		int matchLength = 0;
		while (matcher.find()) {
			matchLength += matcher.group().length();
		}

		return matchLength >= text.length() * MATCH_PERCENTAGE;
	}

	private static void countSymbolFrequencies(int keyLength) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
		try (PrintWriter writer = new PrintWriter(new FileWriter(OFFSETS))) {
			List<String> partsOfText = Utils.splitText(encryptedText, 8);
			partsOfText.forEach(t -> {
				Utils.countSymbolsFrequency(t, writer);
				writer.println();
				writer.println();
			});
		}
	}
}
