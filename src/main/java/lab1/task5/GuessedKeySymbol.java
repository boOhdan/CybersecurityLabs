package lab1.task5;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GuessedKeySymbol {
	private char symbol;
	private int index;
	private int gene;
}
