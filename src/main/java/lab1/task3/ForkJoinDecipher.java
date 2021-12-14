package lab1.task3;

import lab1.utils.KeyGenerator;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import static lab1.task3.Task3.decrypt;
import static lab1.task3.Task3.matchesWords;

public class ForkJoinDecipher extends RecursiveTask<Map<String, String>> {

	static String encryptedText;
	private final KeyGenerator keyGenerator;

	private static final int THRESHOLD = 100_000;

	public static long totalKeys;
	private static long processed = 0;

	public ForkJoinDecipher(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	@Override
	protected Map<String, String> compute() {
		if (keyGenerator.getKeysCount() <= THRESHOLD) {
			Map<String, String> result = new HashMap<>();

			for (String key : keyGenerator) {
				try {
					String decryptedText = decrypt(encryptedText, key);
					if (matchesWords(decryptedText)) {
						result.put(key, decryptedText);
					}
				} catch (Exception e) {}
			}

			processed += keyGenerator.getKeysCount();
			System.out.println(processed + " of " + totalKeys + " processed");
			return result;
		}

		char[][] symbols = keyGenerator.getKeySymbols();
		char[][] firstPart = new char[symbols.length][];
		char[][] secondPart = new char[symbols.length][];
		for (int i = 0; i < symbols.length; i++) {
			firstPart[i] = symbols[i];
			secondPart[i] = symbols[i];
		}

		for (int i = 0; i < symbols.length; i++) {
			int length = symbols[i].length;
			if (length >= 2) {
				firstPart[i] = Arrays.copyOfRange(symbols[i], 0, (length + 1) / 2);
				secondPart[i] = Arrays.copyOfRange(symbols[i], (length + 1) / 2, length);
				break;
			}
		}

		var firstTask = new ForkJoinDecipher(new KeyGenerator(firstPart));
		var secondTask = new ForkJoinDecipher(new KeyGenerator(secondPart)).fork();

		Map<String, String> result = new HashMap<>();
		result.putAll(firstTask.compute());
		result.putAll(secondTask.join());

		return result;
	}
}
