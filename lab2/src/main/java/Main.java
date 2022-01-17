import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

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

	private static final String WORD = "Is sickling ";
	private static final String DESTINATION = "src/main/resources/decrypted.txt";

	private static final double MATCH_PERCENTAGE = 0.6;

	public static void main(String[] args) throws IOException {
		List<byte[]> ciphers = Files.lines(Path.of("src/main/resources/encrypted.txt"))
				.map(Main::hexStringToNormal)
				.collect(Collectors.toList());

		List<String> decryptedWords = new ArrayList<>();

		for (int i = 0; i < ciphers.size(); i++) {
			for (int j = 0; j < ciphers.size(); j++) {
				if (i == j) continue;

				byte[] cipher1 = ciphers.get(i);
				byte[] cipher2 = ciphers.get(j);

				int minLength = Math.min(cipher1.length, cipher2.length);
				byte[] cipher1Copy = new byte[minLength];
				byte[] cipher2Copy = new byte[minLength];
				System.arraycopy(cipher1, 0, cipher1Copy, 0, minLength);
				System.arraycopy(cipher2, 0, cipher2Copy, 0, minLength);

				byte[] xoredCiphers = xor(cipher1Copy, cipher2Copy);

				String decrypted = new String(xor(xoredCiphers, WORD.getBytes()));

				if (wordMatches(decrypted)) {
					decryptedWords.add("Source lines: " + i + " " + j + "; Result = " + decrypted);
				}
			}
		}

		try (PrintWriter writer = new PrintWriter(new FileWriter(DESTINATION))) {
			for (String output : decryptedWords) {
				writer.println(output);
				writer.println();
			}
		}
	}

	public static byte[] hexStringToNormal(String hex) {
		try {
			return Hex.decodeHex(hex);
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] xor(byte[] encrypted, byte[] key) {
		byte[] decrypted = new byte[encrypted.length];

		for (int i = 0; i < decrypted.length; i++) {
			decrypted[i] = (byte) (encrypted[i] ^ key[i % key.length]);
		}

		return decrypted;
	}

	public static boolean wordMatches(String text) {
		var matcher = wordPattern.matcher(text);
		int matchLength = 0;
		while (matcher.find()) {
			matchLength += matcher.group().length();
		}

		return (double) matchLength / text.length() >= MATCH_PERCENTAGE;
	}
}
