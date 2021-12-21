package lab1.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

    private String key;
    private String text;

    @Override
    public String toString() {
        return key + "\n" + text;
    }
}
