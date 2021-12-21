package lab1.task4;

import lab1.config.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Task4 {

	private static final String SOURCE = Config.RESOURCES + "/task4/source.txt";
	private static final String DESTINATION = Config.RESOURCES + "/task4/result.txt";

	public static void main(String[] args) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
		long start = System.currentTimeMillis();
		try (PrintWriter writer = new PrintWriter(new FileWriter(DESTINATION))) {
			var result = new GeneticAlgorithm(500, 500).decrypt(encryptedText);
			writer.println(result);
			writer.println();
		}
		long finish = System.currentTimeMillis();
		System.out.println(finish - start);
	}
}
