package lab1.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static void countSymbolsFrequency(String text, PrintWriter writer) {
		List<Map.Entry<Character, Integer>> sortedList = countSymbolsFrequency(text);
		sortedList.forEach(e -> writer.println(e.getKey() + " = " + e.getValue()));
	}

	public static List<Map.Entry<Character, Integer>> countSymbolsFrequency(String text) {
		Map<Character, Integer> letterFrequencies = new HashMap<>();
		char[] chars = text.toCharArray();
		for (int i = 0; i < text.toCharArray().length; i++) {
			letterFrequencies.compute(chars[i], (k, v) -> v != null ? v + 1 : 1);
		}

		List<Map.Entry<Character, Integer>> sortedList = new ArrayList<>(letterFrequencies.entrySet());
		sortedList.sort((e1, e2) -> {
			int value = e1.getValue().compareTo(e2.getValue());
			return value != 0 ? value : e1.getKey().compareTo(e2.getKey());
		});
		Collections.reverse(sortedList);
		return sortedList;
	}

	public static void countIndexesOfCoincidence(String text, boolean sort, PrintWriter writer) {
		Map<Integer, Double> indexOfCoincidenceMap = new TreeMap<>();

		for (int i = 1; i <= 100; i++) {
			indexOfCoincidenceMap.put(i, countIndexOfCoincidence(text, i));
		}

		if (sort) {
			List<Map.Entry<Integer, Double>> sortedList = new ArrayList<>(indexOfCoincidenceMap.entrySet());
			sortedList.sort((e1, e2) -> {
				int value = e1.getValue().compareTo(e2.getValue());
				return value != 0 ? value : e1.getKey().compareTo(e2.getKey());
			});
			Collections.reverse(sortedList);
			sortedList.forEach(e -> writer.println("I.C.(" + e.getKey() + ") = " + e.getValue()));
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

	public static List<String> generateKeys(char[][] keySymbols) {
		List<String> keys = new ArrayList<>();
		int[] indexes = new int[keySymbols.length];

		long keysCount = 1;
		for (char[] symbol : keySymbols) {
			keysCount *= symbol.length;
		}

		for (int i = 0; i < keysCount; i++) {
			StringBuilder key = new StringBuilder();
			for (int j = 0; j < indexes.length; j++) {
				key.append(keySymbols[j][indexes[j]]);
			}
			for (int j = indexes.length - 1; j >= 0; j--) {
				indexes[j]++;
				if (indexes[j] >= keySymbols[j].length) {
					indexes[j] = 0;
				} else {
					break;
				}
			}
			keys.add(key.toString());
		}

		return keys;
	}

	public static void calculateOffsetMatches(String text, PrintWriter writer) {
		String offsetText = text;
		for (int i = 1; i <= text.length(); i++) {
			offsetText = offsetText.charAt(text.length() - 1) + StringUtils.chop(offsetText);
			int matches = 0;
			for (int j = 0; j < text.length(); j++) {
				if (text.charAt(j) == offsetText.charAt(j)) {
					matches++;
				}
			}
			writer.println("Offset " + i + " = " + matches);
		}
	}

	public static char[] toCharArray(Collection<Character> characters) {
		char[] chars = new char[characters.size()];
		int i = 0;
		for (char symbol : characters) {
			chars[i++] = symbol;
		}
		return chars;
	}

	public static String swap(String source, int index1, int index2) {
		char[] chars = source.toCharArray();
		char temp = chars[index1];
		chars[index1] = chars[index2];
		chars[index2] = temp;

		return new String(chars);
	}

	public static Map<String, Double> sort(Map<String, Double> map) {
		List<Map.Entry<String, Double>> sortedList = new ArrayList<>(map.entrySet());
		sortedList.sort((e1, e2) -> {
			int value = e1.getValue().compareTo(e2.getValue());
			return value != 0 ? value : e1.getKey().compareTo(e2.getKey());
		});
		Collections.reverse(sortedList);

		Map<String, Double> result = new LinkedHashMap<>();
		sortedList.forEach(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}

	public static Map<String, Integer> getNgrams(String text, int n) {
		Map<String, Integer> ngrams = new HashMap<>();
		for (int i = 0; i < text.length() - n + 1; i++) {
			ngrams.merge(text.substring(i, i + n), 1,  Integer::sum);
		}
		return ngrams;
	}
	
	public static Map<String, Integer> countWords(String text, Set<String> words) {
		Map<String, Integer> wordsCount = new HashMap<>();
		for (String word : words) {
			wordsCount.put(word, StringUtils.countMatches(text, word));
		}
		return wordsCount;
	}

	public static String replaceChar(String str, int position, char c) {
		char[] chars = str.toCharArray();
		chars[position] = c;
		return String.valueOf(chars);
	}
}
