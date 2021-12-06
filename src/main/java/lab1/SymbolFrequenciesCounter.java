package lab1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SymbolFrequenciesCounter {

	public static void count(String text, String pathToWrite) throws IOException {
		Map<Character, Integer> letterFrequencies = new TreeMap<>();
		char[] chars = text.toCharArray();
		for (int i = 0; i < text.toCharArray().length; i++) {
			letterFrequencies.compute(chars[i], (k, v) -> v != null ? v + 1 : 1);
		}


		Set<Map.Entry<Character, Integer>> entrySet = new TreeSet<>((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
		entrySet.addAll(letterFrequencies.entrySet());

		try (PrintWriter writer = new PrintWriter(new FileWriter(pathToWrite))) {
			entrySet.forEach(e -> writer.println(e.getKey() + " = " + e.getValue()));
		}
	}
}
