package lab1.task5;

import lab1.config.Config;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task5 {

	private static final String SOURCE = Config.RESOURCES + "task5/source.txt";
	private static final String DESTINATION = Config.RESOURCES + "task5/result.txt";

	private static final String DECRYPTED_PART = "CONGRATULATIONS";
	private static final int KEYS_NUMBER = 4;

	public static void main(String[] args) throws IOException {
		String encryptedText = new String(Files.readAllBytes(Path.of(SOURCE)));
		var correctGenes = SubstitutionDecoder.getKeysPart(encryptedText, DECRYPTED_PART, KEYS_NUMBER);
		GmoChromosome.correctGenes.addAll(correctGenes);

		long start = System.currentTimeMillis();
		computeParallel(encryptedText);
		long finish = System.currentTimeMillis();
		System.out.println(finish - start);
	}

	@SneakyThrows
	private static void computeParallel(String encryptedText) {
		int threadCount = 10;
		int n = 1;
		var executor = Executors.newFixedThreadPool(threadCount);
		List<Future<Result>> futures = new ArrayList<>();
		for (int i = 0; i < threadCount * n; i++) {
			var future = executor.submit(() -> new GeneticAlgorithm(500, 400, KEYS_NUMBER).decrypt(encryptedText));
			futures.add(future);
		}

		List<Result> results = new ArrayList<>();
		for (int i = 0; i < futures.size(); i++) {
			results.add(futures.get(i).get());
			System.out.println((i + 1) + " / " + futures.size());
		}
		Collections.sort(results);

		try (var writer = new PrintWriter(new FileWriter(DESTINATION))) {
			for (var result : results) {
				writer.println(result);
				writer.println();
			}
		}
		executor.shutdown();
	}
}
