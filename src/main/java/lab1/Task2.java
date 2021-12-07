package lab1;

import lab1.config.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Task2 {

	private static final String SOURCE = Config.RESOURCES + "task2 source.txt";
	private static final String DESTINATION = Config.RESOURCES + "task2.txt";

	private static final int KEY = '7';

	public static void main(String[] args) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
		try (PrintWriter writer = new PrintWriter(new FileWriter(DESTINATION))) {
			String decryptedText = decrypt(encryptedText, KEY);
			writer.println(decryptedText);
		}
	}

	private static String decrypt(String text, int key) {
		char[] symbols = text.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < symbols.length; i += 2) {
			int value = Integer.parseInt("" + symbols[i] + symbols[i + 1], 16);
			builder.append((char) (value ^ key));
		}
		return builder.toString();
	}
}
