package com.kpi;

import com.kpi.config.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SymbolFrequenciesCounter {

	private static final String SOURCE = Config.RESOURCES + "/task1.txt";
	private static final String DESTINATION = Config.RESOURCES + "/symbol frequencies.txt";

	public static void main(String[] args) throws IOException {
		count();
	}

	public static void count() throws IOException {
		Map<Character, Integer> letterFrequencies = new TreeMap<>();
		String text = new String(Files.readAllBytes(Path.of(SOURCE)));
		char[] chars = text.toCharArray();
		for (int i = 0; i < text.toCharArray().length; i++) {
			letterFrequencies.compute(chars[i], (k, v) -> v != null ? v + 1 : 1);
		}


		Set<Map.Entry<Character, Integer>> entrySet = new TreeSet<>((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
		entrySet.addAll(letterFrequencies.entrySet());

		PrintWriter writer = new PrintWriter(new FileWriter(DESTINATION));
		entrySet.forEach(e -> writer.println(e.getKey() + " = " + e.getValue()));
		writer.close();
	}
}
