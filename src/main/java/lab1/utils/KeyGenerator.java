package lab1.utils;

import java.util.Iterator;

public class KeyGenerator implements Iterator<String>, Iterable<String> {

	private final char[][] keySymbols;
	private final long keysCount;
	private final int[] indexes;

	private long generatedKeys;

	public KeyGenerator(char[][] keySymbols) {
		this.keySymbols = keySymbols;

		long keysCount = 1;
		for (char[] symbol : keySymbols) {
			keysCount *= symbol.length;
		}
		this.keysCount = keysCount;

		indexes = new int[keySymbols.length];

	}

	@Override
	public Iterator<String> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return keysCount > generatedKeys;
	}

	@Override
	public String next() {
		StringBuilder key = new StringBuilder();
		for (int i = 0; i < indexes.length; i++) {
			key.append(keySymbols[i][indexes[i]]);
		}
		incrementIndexes();

		return key.toString();
	}

	private void incrementIndexes() {
		generatedKeys++;
		for (int i = indexes.length - 1; i >= 0; i--) {
			indexes[i]++;
			if (indexes[i] >= keySymbols[i].length) {
				indexes[i] = 0;
			} else {
				break;
			}
		}
	}
}
