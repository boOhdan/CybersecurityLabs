package lab1;

import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Utils {

	public static void countSymbolsFrequency(String text, PrintWriter writer) {
		Map<Character, Integer> letterFrequencies = new TreeMap<>();
		char[] chars = text.toCharArray();
		for (int i = 0; i < text.toCharArray().length; i++) {
			letterFrequencies.compute(chars[i], (k, v) -> v != null ? v + 1 : 1);
		}


		Set<Map.Entry<Character, Integer>> entrySet = new TreeSet<>((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
		entrySet.addAll(letterFrequencies.entrySet());

		entrySet.forEach(e -> writer.println(e.getKey() + " = " + e.getValue()));
	}

	public static void countIndexesOfCoincidence(String text, PrintWriter writer) {
		Map<Integer, Double> indexOfCoincidenceMap = new TreeMap<>();

		for (int i = 1; i <= 20; i++) {
			indexOfCoincidenceMap.put(i, countIndexOfCoincidence(text, i));
		}

		indexOfCoincidenceMap.forEach((k, v) -> writer.println("I.C.(" + k + ") = " + v));
	}

	private static double countIndexOfCoincidence(String text, int keyLength) {
		List<String> partsOfText = splitText(text, keyLength);

		List<Double> indexesOfCoincidence = new ArrayList<>();
		partsOfText.forEach(t -> indexesOfCoincidence.add(countIndexOfCoincidence(t)));

		return indexesOfCoincidence.stream().mapToDouble(i -> i).sum() / indexesOfCoincidence.size();
	}

	private static double countIndexOfCoincidence(String text) {
		Set<Character> symbols = new HashSet<>();
		for (char symbol : text.toCharArray()) {
			symbols.add(symbol);
		}

		int symbolsCount = 0;
		for (char symbol : symbols) {
			int count = StringUtils.countMatches(text, symbol);
			if (count >= 2) {
				symbolsCount += count * (count - 1);
			}
		}

		return (double) symbolsCount / (text.length() * (text.length() - 1));
	}

	public static List<String> splitText(String text, int offset) {
		List<String> partsOfText = new ArrayList<>();
		for (int i = 0; i < offset; i++) {
			StringBuilder partOfText = new StringBuilder();
			for (int j = i; j < text.length(); j += offset) {
				partOfText.append(text.charAt(j));
			}
			partsOfText.add(partOfText.toString());
		}
		return partsOfText;
	}
}
