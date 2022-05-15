package de.materna.mini_excel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a parser
 */
public class Parser {

    /**
     * contains a pattern for detection of cells
     */
    protected final static String CELL_PATTERN = "([a-zA-Z]+)([1-9][0-9]*)";

    /**
     * contains a pattern for detection of row index
     */
    protected final static String ROW_PATTERN = "[1-9][0-9]*";

    /**
     * contains a pattern for detection of colum code
     */
    protected final static String COL_PATTERN = "[a-zA-Z]+";

    private Pattern pattern;

    private Matcher matcher;

    /**
     * Extracts the variables from the specified string
     * @param content string to extract from
     * @return a list with all variables contained in content
     */
    public Set<String> getVariableNames(String content) {
        Set<String> variables = new HashSet<>();
        pattern = Pattern.compile("\\$(" + CELL_PATTERN + ")");
        if (content.contains("=")) {
            matcher = pattern.matcher(content);
            while (matcher.find()) {
                variables.add(matcher.group(1));
            }
        }
        return variables;
    }


}
