import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) throws IOException {
		List<String> ciphers = Files.lines(Path.of("src/main/resources/encrypted.txt"))
				.map(Main::hexStringToNormal)
				.collect(Collectors.toList());
	}

	public static String hexStringToNormal(String hex) {
		StringBuilder result = new StringBuilder();
		String[] hexBytes = hex.split("(?<=\\G.{2})");
		Arrays.stream(hexBytes)
				.map(b -> (char) Integer.parseInt(b, 16))
				.forEach(result::append);

		return result.toString();
	}

	public static byte[] xor(byte[] encrypted, byte[] key) {
		byte[] decrypted = new byte[encrypted.length];

		for (int i = 0; i < decrypted.length; i++) {
			decrypted[i] = (byte)(encrypted[i] ^ key[i % key.length]);
		}

		return decrypted;
	}


}
