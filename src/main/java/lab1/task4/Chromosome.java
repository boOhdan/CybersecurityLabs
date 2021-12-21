package lab1.task4;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class Chromosome implements Comparable<Chromosome> {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String genes;
    private double fitness;

    public Chromosome() {
        this(true);
    }

    public Chromosome(boolean generateRandomKey) {
        if (generateRandomKey) {
            List<Character> genes = ALPHABET.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            Collections.shuffle(genes);
            this.genes = genes.stream().map(Objects::toString).collect(Collectors.joining());
        } else {
            genes = StringUtils.repeat((char) 0, ALPHABET.length());
        }
    }

    public Chromosome(String genes) {
        this.genes = genes;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(fitness, o.getFitness());
    }
}
