package lab1.task5;

import lab1.config.Config;
import lab1.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Task5 {

	private static final String SOURCE = Config.RESOURCES + "task5/source.txt";

	public static void main(String[] args) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
		try (PrintWriter writer = new PrintWriter(Config.RESOURCES + "task5/I.C.txt")) {
			Utils.countIndexesOfCoincidence(encryptedText, false, writer);
		}
	}
}
