package lab1;

import lab1.config.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Task3 {

	private static final String SOURCE = Config.RESOURCES + "task3 source.txt";

	public static void main(String[] args) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
		try (PrintWriter writer = new PrintWriter(new FileWriter(Config.RESOURCES + "task3 I.C.txt"))) {
			Utils.countIndexesOfCoincidence(encryptedText, writer);
		}
	}
}
