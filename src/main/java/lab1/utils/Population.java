package lab1.utils;

import lab1.utils.Chromosome;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Population {

    private List<Chromosome> chromosomes;

    public Population(int size) {
        this.chromosomes = Stream.generate(Chromosome::new).limit(size).collect(Collectors.toList());
    }

    public int getSize() {
        return chromosomes.size();
    }

    public Chromosome getBestChromosome() {
        return chromosomes.get(0);
    }
}
