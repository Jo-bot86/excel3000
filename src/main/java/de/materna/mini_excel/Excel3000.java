package de.materna.mini_excel;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.materna.mini_excel.utils.MathUtils;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a Spreadsheet program like excel. Alows
 */
public class Excel3000 {

    /**
     * Represents all variables and their associated values
     */
    private Map<String, Double> variables = new HashMap<>();

    /**
     * Represents an Excel table
     */
    private Table<Integer, Integer, String> table = HashBasedTable.create();

    /**
     * Represents a parser
     */
    private Parser parser;


    /**
     * initializes a parser object
     *
     * @param parser
     */
    public Excel3000(Parser parser) {
        this.parser = parser;
    }

    /**
     * evaluates every cell in table
     */
    public Excel3000 evaluate() {
        Set<String> markedCells = new HashSet<>();
        Excel3000 excel = new Excel3000(parser);
        System.out.println(variables);
        for (Integer rowIndex : table.rowKeySet()) {
            for (Integer colIndex : table.columnKeySet()) {
                evaluateCell(rowIndex, colIndex, markedCells);
            }
        }
        for (Integer rowIndex : table.rowKeySet()) {
            for (Integer colIndex : table.columnKeySet()) {
                excel.setCellAt(rowIndex, colIndex, String.valueOf(variables.get(MathUtils.cellOf(rowIndex,colIndex))));
            }
        }

        return excel;
    }

    public void evaluateCell(int rowIndex, int colIndex, Set<String> markedCells) {
        String cell = MathUtils.cellOf(rowIndex, colIndex);
        if (variables.get(cell) != null) {
            return;
        }
        String content = table.get(rowIndex, colIndex);
        if (isFormula(content)) {
            if (markedCells.contains(cell)) {
                throw new IllegalStateException("Found circle");
            }
            markedCells.add(cell);
            Set<String> vars = parser.getVariableNames(content);
            for (String var : vars) {
                int row = Integer.valueOf(var.split(Parser.COL_PATTERN)[1]);
                int col = MathUtils.getColValue(var.split(Parser.ROW_PATTERN)[0]);
                evaluateCell(row, col, markedCells);
            }
            content = content.replaceAll("\\$|=", "");
            double result = new ExpressionBuilder(content)
                    .variables(vars)
                    .build()
                    .setVariables(variables)
                    .evaluate();
            variables.put(MathUtils.cellOf(rowIndex, colIndex), result);
        }

    }

    public boolean isFormula(String expression) {
        if (expression.trim().startsWith("=")) {
            return true;
        }
        return false;
    }

    /**
     * Sets the specified content into the specified cell in table
     *
     * @param cell    the cell to be set
     * @param content the content to set
     */
    public void setCell(String cell, String content) {
        if (cell.matches(Parser.CELL_PATTERN)) {
            String row = cell.split(Parser.COL_PATTERN)[1];
            String col = cell.split(Parser.ROW_PATTERN)[0];
            table.put(Integer.valueOf(row), MathUtils.getColValue(col), content);
            if (!isFormula(content)) {
                variables.put(cell, Double.valueOf(content));
            }
        } else {
            throw new IllegalArgumentException("No legal cell");
        }
    }

    public void setCellAt(int row, int col, String content) {
        table.put(row, col, content);
    }

    /**
     * gets the cell with the specified row and colum index from table
     *
     * @param row
     * @param col
     * @return
     */
    public String getCellAt(int row, int col) {
        return table.get(row, col);
    }

    /**
     * Gets the specified cell from table
     *
     * @param cell
     * @return
     */
    public String getCell(String cell) {
        String row = cell.split(Parser.COL_PATTERN)[1];
        String col = cell.split(Parser.ROW_PATTERN)[0];
        return table.get(Integer.valueOf(row), MathUtils.getColValue(col));
    }

    /**
     * exports table in a xlsx file
     *
     * @throws IOException
     */
    public void exportFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (Integer rowKey : table.rowKeySet()) {
            Row row = sheet.createRow(rowKey - 1);
            for (Integer colKey : table.columnKeySet()) {
                Cell cell = row.createCell(colKey - 1);
                cell.setCellValue(getCellAt(rowKey, colKey));
            }
        }
        OutputStream out =  Files.newOutputStream(Paths.get("content.xlsx"));
        workbook.write(out);
    }

}


