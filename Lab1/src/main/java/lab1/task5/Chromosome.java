package lab1.task5;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static lab1.utils.Utils.ALPHABET;

@Data
@NoArgsConstructor
public class Chromosome implements Comparable<Chromosome> {

	protected List<String> genes = new ArrayList<>();
	protected double fitness;

	public Chromosome(int keysNumber) {
		this(true, keysNumber);
	}

	public Chromosome(boolean generateRandomKey, int keysNumber) {
		for (int i = 0; i < keysNumber; i++) {
			if (generateRandomKey) {
				List<Character> gene = ALPHABET.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
				Collections.shuffle(gene);
				this.genes.add(gene.stream().map(Objects::toString).collect(Collectors.joining()));
			}
		}
	}

	public void improveGenes() {}

	@Override
	public int compareTo(Chromosome o) {
		return Double.compare(fitness, o.getFitness());
	}
}
