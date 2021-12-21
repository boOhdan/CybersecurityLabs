package lab1.task5;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Result {

	private List<String> keys;
	private String text;

	@Override
	public String toString() {
		return keys + "\n" + text;
	}
}
