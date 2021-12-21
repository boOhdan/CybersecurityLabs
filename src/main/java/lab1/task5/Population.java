package lab1.task5;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Population {

	private List<Chromosome> chromosomes;

	public Population(int size, int keysNumber) {
		this.chromosomes = Stream.generate(() -> new Chromosome(keysNumber)).limit(size).collect(Collectors.toList());
	}

	public int getSize() {
		return chromosomes.size();
	}

	public Chromosome getBestChromosome() {
		return chromosomes.get(0);
	}
}
