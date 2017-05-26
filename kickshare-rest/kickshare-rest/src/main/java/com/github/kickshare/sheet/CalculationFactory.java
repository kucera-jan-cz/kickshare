package com.github.kickshare.sheet;

import java.text.MessageFormat;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jan.Kucera
 * @since 25.5.2017
 */
@AllArgsConstructor
public class CalculationFactory implements Supplier<Workbook> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationFactory.class);
    private static final int INFINITY = 65536;
    private static final int COLUMN_SIZE = 100;

    private final PledgeCalculationMetadata meta;

    @Override
    public Workbook get() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("calculation");

        final int groupSize = meta.getBackers().size();
        emptyRows(sheet, groupSize + 1, COLUMN_SIZE);
        //@TODO insert rewards

        String[] names = meta.getBackers().stream().map(b -> b.getBacker().getName() + " " + b.getBacker().getSurname()).toArray(String[]::new);
        //@TODO - place there delivery address -> permanent vs reachable
        String[] cities = meta.getBackers().stream().map(b -> b.getAddress().getCity()).toArray(String[]::new);
        String[] usualColumns = {"Name", "City", "Paid", MessageFormat.format("Total({0})", meta.getFromCurrency()), MessageFormat.format("Total ({0})", meta.getToCurrency())};
        String[] rewards = meta.getRewards().stream().map(r -> MessageFormat.format("{0} ({1}{2})", r.getName(), r.getPrice(), r.getCurrency().getCurrencyCode())).toArray(String[]::new);
        String[] columnNames = ArrayUtils.addAll(usualColumns, rewards);

        //sheet, row, column
        fillRow(sheet, 0, 0, "Metadata:");
        fillRow(sheet, 1, 0, "Conversion:", meta.getConversionRate());
        fillRow(sheet, 4, 0, columnNames);
        fillTextColumn(sheet, 5, 0, names);
        fillTextColumn(sheet, 5, 1, cities);
        fillBooleanColumn(sheet, 5, 2, new boolean[groupSize]);
        fillFormulaColumn(sheet, 5, 3, (Integer i) -> sum(i, 5, i, COLUMN_SIZE), groupSize);
        fillFormulaColumn(sheet, 5, 4, (Integer i) -> MessageFormat.format("D{0} * $B$2", i + 1), groupSize);

        if(meta.getFromCurrency().equals(meta.getToCurrency())) {
            sheet.setColumnHidden(4, true);
        }
        for(int i = 0; i < columnNames.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return wb;
    }

    private void fillTextColumn(Sheet sheet, int startingRowIndex, int columnIndex, String... values) {
        for (int i = 0; i < values.length; i++) {
            Cell cell = getOrCreate(sheet, startingRowIndex + i, columnIndex);
            cell.setCellValue(values[i]);
        }
    }

    private void fillFormulaColumn(Sheet sheet, int startingRowIndex, int columnIndex, Function<Integer, String> formulaFunction, int size) {
        for (int i = 0; i < size; i++) {
            Cell cell = getOrCreate(sheet, startingRowIndex + i, columnIndex);
            cell.setCellType(CellType.FORMULA);
            String formula = formulaFunction.apply(startingRowIndex + i);
            cell.setCellFormula(formula);
        }
    }

    private void fillBooleanColumn(Sheet sheet, int startingRowIndex, int columnIndex, boolean... values) {
        for (int i = 0; i < values.length; i++) {
            Cell cell = getOrCreate(sheet, startingRowIndex + i, columnIndex);
            cell.setCellType(CellType.BOOLEAN);
            cell.setCellValue(values[i]);
        }
    }

    private void fillRow(Sheet sheet, int rowIndex, int colIndex, Object... values) {
        Row row = getRow(sheet, rowIndex);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.getCell(colIndex + i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Object value = values[i];
            if (String.class.isInstance(value)) {
                cell.setCellValue((String) value);
            } else if (Number.class.isInstance(value)) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(((Number) value).doubleValue());
            } else if (Boolean.class.isInstance(value)) {
                cell.setCellType(CellType.BOOLEAN);
                cell.setCellValue((Boolean) value);
            } else {
                throw new IllegalArgumentException("Unsupported type: " + value.getClass());
            }
        }
    }

    private Cell getOrCreate(Sheet sheet, int rowIndex, int startingColIndex) {
        Row row = getRow(sheet, rowIndex);
        return row.getCell(startingColIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    private String sum(int leftRowIndex, int leftColIndex, int rightRowIndex, int rightColIndex) {
        return MessageFormat.format("SUM({0}:{1})",
                new CellReference(leftRowIndex, leftColIndex, false, false).formatAsString(),
                new CellReference(rightRowIndex, rightColIndex, false, false).formatAsString()
        );
    }

    private void emptyRows(Sheet sheet, int rowSize, int colSize) {
        for (int i = 0; i < rowSize; i++) {
            emptyRow(sheet, i, colSize);
        }
    }

    private void emptyRow(Sheet sheet, int rowIndex, int size) {
        Row row = getRow(sheet, rowIndex);
        for (int i = 0; i < size; i++) {
            row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        }
    }

    private Row getRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        row = row == null ? sheet.createRow(rowIndex) : row;
        return row;
    }
}
