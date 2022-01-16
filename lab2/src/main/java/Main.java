import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

	private static final Pattern wordPattern = Pattern.compile("[\\p{Alpha} ]+");

	private static final String WORD = "When";
	private static final String DESTINATION = "src/main/resources/decrypted.txt";

	public static void main(String[] args) throws IOException {
		List<String> ciphers = Files.lines(Path.of("src/main/resources/encrypted.txt"))
				.map(Main::hexStringToNormal)
				.collect(Collectors.toList());

		Map<Double, String> decryptedWords = new HashMap<>();

		for (int i = 0; i < ciphers.size(); i++) {
			for (int j = i + 1; j < ciphers.size(); j++) {
				String cipher1 = ciphers.get(i);
				String cipher2 = ciphers.get(j);

				String xoredCiphers = xor(cipher1, cipher2);

				String decrypted = xor(xoredCiphers, WORD);
				double wordPercentage = wordPercentage(decrypted);

				decryptedWords.put(wordPercentage, decrypted);
			}
		}

		var sortedList = decryptedWords.entrySet().stream()
				.sorted(Map.Entry.<Double, String>comparingByKey().reversed())
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());

		try (PrintWriter writer = new PrintWriter(new FileWriter(DESTINATION))) {
			for (String output : sortedList) {
				writer.println(output);
			}
			writer.flush();
		}
	}

	public static String hexStringToNormal(String hex) {
		StringBuilder result = new StringBuilder();
		String[] hexBytes = hex.split("(?<=\\G.{2})");
		Arrays.stream(hexBytes)
				.map(b -> (char) Integer.parseInt(b, 16))
				.forEach(result::append);

		return result.toString();
	}

	public static String xor(String encrypted, String key) {
		return new String(xor(encrypted.getBytes(), key.getBytes()));
	}

	public static byte[] xor(byte[] encrypted, byte[] key) {
		byte[] decrypted = new byte[encrypted.length];

		for (int i = 0; i < decrypted.length; i++) {
			decrypted[i] = (byte) (encrypted[i] ^ key[i % key.length]);
		}

		return decrypted;
	}

	public static double wordPercentage(String text) {
		var matcher = wordPattern.matcher(text);
		int matchLength = 0;
		while (matcher.find()) {
			matchLength += matcher.group().length();
		}

		return (double) matchLength / text.length();
	}
}
