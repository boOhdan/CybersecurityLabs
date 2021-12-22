package lab1.task5;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GmoChromosome extends Chromosome {

	public final static List<GuessedKeySymbol> correctGenes = new ArrayList<>();

	public GmoChromosome(int keysNumber) {
		super(keysNumber);
	}

	public GmoChromosome(boolean generateRandomKey, int keysNumber) {
		super(generateRandomKey, keysNumber);
		if (generateRandomKey) {
			improveGenes();
		}
	}

	@Override
	public void improveGenes() {
		for (var correctGene : correctGenes) {
			int geneIndex = correctGene.getGene();
			String curGene = genes.get(geneIndex);
			int index = curGene.indexOf(correctGene.getSymbol());

			if (index != correctGene.getIndex()) {
				char[] chars = curGene.toCharArray();
				char temp = curGene.charAt(correctGene.getIndex());
				chars[correctGene.getIndex()] = correctGene.getSymbol();
				chars[index] = temp;

				genes.set(geneIndex, new String(chars));
			}
		}
	}
}
