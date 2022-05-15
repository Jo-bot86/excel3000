package de.materna.mini_excel.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.function.IntSupplier;
import java.util.stream.IntStream;

public class MathUtils {

    private static final IntSupplier supplier =
            new IntSupplier() {
                int current = 1;

                @Override
                public int getAsInt() {
                    return current++;
                }
            };

    public static final BiMap<Character, Integer> alphabet = IntStream.rangeClosed('a', 'z')
            .boxed()
            .reduce(
                    new ImmutableBiMap.Builder<Character, Integer>(),
                    (builder, integer) -> builder.put((char) integer.intValue(), supplier.getAsInt()),
                    (builder1, builder2) -> builder1.putAll(builder2.build()))
            .build();

    /**
     * Determines the int representation of an Excel column.
     * A=1,...,Z=26,AA=27,..AZ=52,BA=53...
     *
     * @param chars the column code like AA
     * @return the int representation of an Excel column
     */
    public static int getColValue(String chars) {
        int colCount = 0;
        for (int i = chars.length() - 1; i >= 0; i--) {
            colCount += (Character.getNumericValue(chars.charAt(i)) - 9) * Math.pow(26, chars.length() - 1 - i);
        }
        return colCount;
    }

    public static String cellOf(int rowIndex, int colIndex) {
        StringBuilder sb = new StringBuilder();

        int count = colIndex - 1;
        while (count >= 0) {
            int charValue = (count % 26) + 1;
            sb.append(alphabet.inverse().get(charValue));
            count = (count / 26) - 1;
        }

        return sb.reverse().append(rowIndex).toString().toUpperCase();
    }


}
