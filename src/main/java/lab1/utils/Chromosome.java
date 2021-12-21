package lab1.utils;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static lab1.utils.Utils.ALPHABET;

@Data
public class Chromosome implements Comparable<Chromosome> {

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
