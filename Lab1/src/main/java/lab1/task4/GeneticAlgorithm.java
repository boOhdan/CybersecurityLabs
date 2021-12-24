package lab1.task4;

import lab1.config.Config;
import lab1.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GeneticAlgorithm {

	private final Population population;
	private final int generationCount;
	private final int survivalCount;

	private static final double SURVIVAL_PERCENTAGE = 0.2;

	private static Map<String, Double> letterFrequencies = new HashMap<>();
	private static final Map<String, Double> bigrams = new HashMap<>();
	private static final Map<String, Double> trigrams = new HashMap<>();

	static {
		try {
			readNGrams(Config.RESOURCES + "ngrams/bigrams.txt", bigrams);
			readNGrams(Config.RESOURCES + "ngrams/trigrams.txt", trigrams);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readNGrams(String sourcePath, Map<String, Double> destination) throws IOException {
		List<String> lines = Files.readAllLines(Path.of(sourcePath));
		long sum = lines.stream()
				.map(str -> str.split(" ")[1])
				.mapToLong(Long::parseLong)
				.sum();

		lines.stream()
				.map(str -> str.split(" "))
				.forEach(str -> destination.put(str[0], Double.parseDouble(str[1]) / sum));
	}

	public GeneticAlgorithm(int populationSize, int generationCount) {
		this.population = new Population(populationSize);
		this.generationCount = generationCount;
		survivalCount = (int) (populationSize * SURVIVAL_PERCENTAGE);
	}

	public Result decrypt(String encryptedText) {
		evaluate(encryptedText);

		for (int i = 0; i < generationCount; i++) {
			nextGeneration();
			evaluate(encryptedText);
		}

		return new Result(population.getBestChromosome().getGenes(),
				SubstitutionDecoder.decrypt(encryptedText, population.getBestChromosome().getGenes()));
	}

	private void evaluate(String encryptedText) {
		for (int i = 0; i < population.getSize(); i++) {
			Chromosome chromosome = population.getChromosomes().get(i);
			if (chromosome.getFitness() == 0) {
				String decryptedText = SubstitutionDecoder.decrypt(encryptedText, chromosome.getGenes());
				chromosome.setFitness(fitnessFunction(decryptedText));
			}
		}

		Collections.sort(population.getChromosomes());
	}

	private double fitnessFunction(String decryptedText) {
		double fitness = 0;
		fitness += fitnessFunction(Utils.getNgrams(decryptedText, 2), bigrams);
		fitness += fitnessFunction(Utils.getNgrams(decryptedText, 3), trigrams);

		return fitness;
	}

	private double fitnessFunction(Map<String, Integer> textNGrams, Map<String, Double> ngrams) {
		double fitness = 0;
		for (String key : textNGrams.keySet()) {
			double count = textNGrams.get(key);
			double frequencyInEnglish = ngrams.containsKey(key) ? ngrams.get(key) : 0;
			fitness += (count - frequencyInEnglish);
		}
		return fitness;
	}

	private void nextGeneration() {
		List<Chromosome> newChromosomes = new ArrayList<>(population.getSize());
		newChromosomes.addAll(population.getChromosomes().subList(0, survivalCount));

		for (int i = survivalCount; i < population.getSize(); i += 2) {
			Chromosome firstPatentChromosome = getRandomChromosome(population);
			Chromosome secondPatentChromosome = getRandomChromosome(population);

			newChromosomes.addAll(crossover(firstPatentChromosome, secondPatentChromosome));
		}

		population.setChromosomes(newChromosomes);
	}

	private Chromosome getRandomChromosome(Population population) {
		int index = new Random().nextInt(population.getChromosomes().size());
		return population.getChromosomes().get(index);
	}

	public List<Chromosome> crossover(Chromosome parent1, Chromosome parent2) {
		Random random = new Random();
		int end = random.nextInt(parent1.getGenes().length()) + 1;
		int start = random.nextInt(end);

		return List.of(
				crossoverForOneChild(parent1.getGenes(), parent2.getGenes(), start, end),
				crossoverForOneChild(parent2.getGenes(), parent1.getGenes(), start, end)
		);
	}

	public Chromosome crossoverForOneChild(String parent1, String parent2, int start, int end) {
		for (int i = start; i < end; i++) {
			char charToInsert = parent1.charAt(i);
			char charAtPosition = parent2.charAt(i);
			int position = parent2.indexOf(charToInsert);

			parent2 = Utils.replaceChar(parent2, position, charAtPosition);
			parent2 = Utils.replaceChar(parent2, i, charToInsert);
		}

		return new Chromosome(parent2);
	}
}

