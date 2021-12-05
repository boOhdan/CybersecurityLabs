package com.kpi;

import com.kpi.config.Config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Task1 {

	private static final String SOURCE = Config.RESOURCES + "/source.txt";
	private static final String TARGET = Config.RESOURCES + "/task1.txt";

	public static void main(String[] args) throws Exception {
		String bits = new String(Files.readAllBytes(Path.of(SOURCE)));
		String[] bytes = bits.split("(?<=\\G.{8})");
		String text = Arrays.stream(bytes)
				.mapToInt(b -> Integer.parseInt(b, 2))
				.mapToObj(i -> (char) i)
				.map(String::valueOf)
				.collect(Collectors.joining());

		Files.writeString(Path.of(TARGET), text);
	}
}
