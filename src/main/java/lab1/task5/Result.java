package lab1.task5;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Result implements Comparable<Result> {

	private final List<String> keys;
	private final String text;
	private double fitness;

	@Override
	public int compareTo(Result o) {
		return Double.compare(fitness, o.getFitness());
	}

	@Override
	public String toString() {
		return keys + "\n" + fitness + "\n" + text;
	}
}
