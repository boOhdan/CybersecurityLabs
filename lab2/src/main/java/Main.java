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

	private static final String WORD = "The ";
	private static final String DESTINATION = "src/main/resources/decrypted.txt";

	private static final String KEY_PART = "";

	public static void main(String[] args) throws IOException {
		List<String> ciphers = Files.lines(Path.of("src/main/resources/encrypted.txt"))
				.map(Main::hexStringToNormal)
				.collect(Collectors.toList());

		Map<Double, String> decryptedWords = new HashMap<>();

		for (int i = 0; i < ciphers.size(); i++) {
			for (int j = i + 1; j < ciphers.size(); j++) {
				String cipher1 = ciphers.get(i);
				String cipher2 = ciphers.get(j);

				int minLength = Math.min(cipher1.length(), cipher2.length());
				cipher1 = cipher1.substring(0, minLength);
				cipher2 = cipher2.substring(0, minLength);

				String xoredCiphers = xor(cipher1, cipher2);

				String decrypted = xor(xoredCiphers, WORD);
				double wordPercentage = wordPercentage(decrypted);

				decryptedWords.put(wordPercentage, decrypted);

				System.out.println("Source line: " + i + "; Result = " + decrypted);
			}
		}

		var sortedList = decryptedWords.entrySet().stream()
				.sorted(Map.Entry.<Double, String>comparingByKey().reversed())
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());

		try (PrintWriter writer = new PrintWriter(new FileWriter(DESTINATION))) {
			for (String output : sortedList) {
				writer.println(output);
				writer.println();
			}
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

	public static String stringToHex(String input) {
		StringBuilder result = new StringBuilder();
		for (char c : input.toCharArray()) {
			result.append(Integer.toString(c, 16));
		}
		return result.toString();
	}

	public static String xor(String encrypted, String key) {
		byte[] bytes = xor(encrypted.getBytes(), key.getBytes());
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append((char) b);
		}

		return result.toString();
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

	public static String getKeyPart(String encrypted, String decrypted) {
		return xor(encrypted, decrypted);
	}
}
