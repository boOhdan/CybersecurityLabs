package lab1.task5;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class GuessedKeySymbol {
	private char symbol;
	private int index;
	private int gene;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GuessedKeySymbol that = (GuessedKeySymbol) o;
		return symbol == that.symbol && gene == that.gene;
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol, gene);
	}
}
