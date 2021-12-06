package lab1;

import lab1.config.Config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Task1 {

	private static final String SOURCE = Config.RESOURCES + "/lab1/source.txt";
	private static final String TARGET = Config.RESOURCES + "/lab1/task1.txt";

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
