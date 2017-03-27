package us.shiroyama.android.ormlitefields.internal.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * String Utility
 *
 * @author Fumihiko Shiroyama
 */

public class StringUtils {

    public static String camelToSnake(String from) {
        if (from == null) {
            throw new IllegalArgumentException("from is null");
        }

        List<String> chunks = new ArrayList<>();
        int lastUpperCase = 0;
        for (int i = 0; i < from.toCharArray().length; i++) {
            char c = from.charAt(i);
            if (i == from.toCharArray().length - 1) {
                chunks.add(from.substring(lastUpperCase).toLowerCase());
                break;
            }
            if (Character.isUpperCase(c)) {
                boolean hasNext = i + 1 <= from.toCharArray().length;
                boolean hasBefore = i - 1 >= 0;
                if (hasNext && hasBefore) {
                    char nextChar = from.charAt(i + 1);
                    char beforeChar = from.charAt(i - 1);
                    if (Character.isUpperCase(beforeChar) && Character.isUpperCase(nextChar)) {
                        continue;
                    }
                }
                if (lastUpperCase == i) {
                    continue;
                }
                String chunk = from.substring(lastUpperCase, i).toLowerCase();
                chunks.add(chunk);
                lastUpperCase = i;
            }
        }
        return String.join("_", chunks);
    }

    public static String stripFirstAndLastUnderscore(String from) {
        if (from == null) {
            throw new IllegalArgumentException("from is null");
        }

        StringBuilder sb = new StringBuilder(from);
        if (sb.charAt(0) == '_') {
            sb.deleteCharAt(0);
        }
        if (sb.charAt(sb.length() - 1) == '_') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
