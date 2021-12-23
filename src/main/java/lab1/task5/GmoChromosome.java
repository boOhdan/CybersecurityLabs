package lab1.task5;

import lab1.utils.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GmoChromosome extends Chromosome {

	public static final List<GuessedKeySymbol> correctGenes = new ArrayList<>();
	public static boolean onlyCorrectGenes;

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
		if (onlyCorrectGenes) {
			int genesSize = genes.size();
			genes = new ArrayList<>();
			for (int i = 0; i < genesSize; i++) {
				genes.add(StringUtils.repeat("_", Utils.ALPHABET.length()));
			}
		}

		for (var correctGene : correctGenes) {
			int geneIndex = correctGene.getGene();
			String curGene = genes.get(geneIndex);
			int index = curGene.indexOf(correctGene.getSymbol());

			if (index != correctGene.getIndex()) {
				char[] chars = curGene.toCharArray();
				char temp = curGene.charAt(correctGene.getIndex());
				chars[correctGene.getIndex()] = correctGene.getSymbol();
				if (index > 0) {
					chars[index] = temp;
				}

				genes.set(geneIndex, new String(chars));
			}
		}
	}
}
