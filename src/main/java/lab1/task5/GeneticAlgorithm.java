package lab1.task5;

import lab1.config.Config;
import lab1.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GeneticAlgorithm {

	private final Population population;
	private final int generationCount;
	private final int keysNumber;
	private final int survivalCount;

	private static final double SURVIVAL_PERCENTAGE = 0.2;

	private static Map<String, Double> letterFrequencies = new HashMap<>();
	private static final Map<String, Double> bigrams = new HashMap<>();
	private static final Map<String, Double> trigrams = new HashMap<>();
	private static final Map<String, Double> quadgrams = new HashMap<>();
	private static final Map<String, Double> quintgrams = new HashMap<>();
	private static Map<String, Double> words = new HashMap<>();

	static {
		try {
			readNGrams("bigrams.txt", bigrams);
			readNGrams("trigrams.txt", trigrams);
			readNGrams("quadgrams.txt", quadgrams);
			readNGrams("quintgrams.txt", quintgrams);
			/*readNGrams("words.txt", words, 2000);
			words = words.entrySet().stream()
					.filter(w -> w.getKey().length() > 1)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readNGrams(String file, Map<String, Double> destination) throws IOException {
		readNGrams(file, destination, 10_000);
	}

	private static void readNGrams(String file, Map<String, Double> destination, int limit) throws IOException {
		List<String> lines = Files.readAllLines(Path.of(Config.RESOURCES + "ngrams/" + file));
		if (limit == -1) {
			limit = lines.size();
		}

		long sum = lines.stream()
				.limit(limit)
				.map(str -> str.split(" ")[1])
				.mapToLong(Long::parseLong)
				.sum();

		lines.stream()
				.limit(limit)
				.map(str -> str.split(" "))
				.forEach(str -> destination.put(str[0], Double.parseDouble(str[1]) / sum));
	}

	public GeneticAlgorithm(int populationSize, int generationCount, int keysNumber) {
		this.population = new Population(populationSize, keysNumber);
		this.generationCount = generationCount;
		this.keysNumber = keysNumber;
		survivalCount = (int) (populationSize * SURVIVAL_PERCENTAGE);
	}

	public Result decrypt(String encryptedText) {
		evaluate(encryptedText);

		for (int i = 0; i < generationCount; i++) {
			nextGeneration();
			evaluate(encryptedText);
			System.out.println((i + 1) + " / " + generationCount);
			System.out.printf("fitness = %s\n", population.getBestChromosome().getFitness());
		}

		var result = new Result(population.getBestChromosome().getGenes(),
				SubstitutionDecoder.decrypt(encryptedText, population.getBestChromosome().getGenes()));
		result.setFitness(fitnessFunction(result.getText()));
		return result;
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
		fitness += fitnessFunction(Utils.getNgrams(decryptedText, 4), quadgrams);
		fitness += fitnessFunction(Utils.getNgrams(decryptedText, 5), quintgrams);
//		fitness += fitnessFunction(Utils.countWords(decryptedText, words.keySet()), words);

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

	private List<Chromosome> crossover(Chromosome parent1, Chromosome parent2) {
		var child1 = new GmoChromosome(false, keysNumber);
		var child2 = new GmoChromosome(false, keysNumber);
		for (int i = 0; i < keysNumber; i++) {
			String gene1 = parent1.getGenes().get(i);
			String gene2 = parent2.getGenes().get(i);
			Random random = new Random();
			int end = random.nextInt(gene1.length()) + 1;
			int start = random.nextInt(end);

			child1.getGenes().add(crossoverForOneChild(gene1, gene2, start, end));
			child2.getGenes().add(crossoverForOneChild(gene2, gene1, start, end));
		}
		child1.improveGenes();
		child2.improveGenes();

		return List.of(child1, child2);
	}

	private String crossoverForOneChild(String parent1, String parent2, int start, int end) {
		for (int i = start; i < end; i++) {
			char charToInsert = parent1.charAt(i);
			char charAtPosition = parent2.charAt(i);
			int position = parent2.indexOf(charToInsert);

			parent2 = Utils.replaceChar(parent2, position, charAtPosition);
			parent2 = Utils.replaceChar(parent2, i, charToInsert);
		}

		return parent2;
	}
}
