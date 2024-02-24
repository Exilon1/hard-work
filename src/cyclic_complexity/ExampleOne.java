package cyclic_complexity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExampleOne {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";

  private final Map<String, Integer> indexMap = new HashMap<>();

  // ЦС = 8
  public boolean hasNecessaryColumn(Sheet sheet, String fileName, String columnName) {
    Row headerRow = sheet.getRow(0);
    if (Objects.nonNull(headerRow)) {
      for (Cell cell : headerRow) {
        if (CellType.STRING == cell.getCellType()) {
          String cellName = cell.getStringCellValue().trim().toLowerCase();
          if (columnName.equals(cellName) && indexMap.containsKey(cellName)) {
            System.out.printf(
                ANSI_RED + "Колонка '%s' дублируется в файле '%s'. Конфликт данных.%n" + ANSI_RESET,
                columnName, fileName);
            return false;
          }

          if (columnName.equals(cellName)) {
            indexMap.put(cellName, cell.getColumnIndex());
          }
        }
      }
    }

    if (indexMap.containsKey(columnName)) {
      System.out.printf("Колонка '%s' найдена в файле '%s'.%n", columnName, fileName);
      return true;
    } else {
      System.out.printf(ANSI_RED + "Колонка '%s' не найдена в файле '%s'.%n" + ANSI_RESET,
          columnName,
          fileName);
      return false;
    }
  }

  // Исправленный метод. Применены: избавление от else, обработка в if одного условия вместо двух,
  // убраны любые вложенности if, цикл убран из условия.
  // ЦС = 5
  public boolean hasNecessaryColumnAfterRefactoring(Sheet sheet, String fileName,
      String columnName) {
    Row headerRow = sheet.getRow(0);
    if (Objects.nonNull(headerRow)) {
      return false;
    }

    for (Cell cell : headerRow) {
      if (CellType.STRING != cell.getCellType()) {
        continue;
      }

      String cellName = cell.getStringCellValue().trim().toLowerCase();
      if (checkCellExistsAndColumnNameMatch(cellName, columnName)) {
        System.out.printf(
            ANSI_RED + "Колонка '%s' дублируется в файле '%s'. Конфликт данных.%n" + ANSI_RESET,
            columnName, fileName);
        return false;
      }

      if (columnName.equals(cellName)) {
        indexMap.put(cellName, cell.getColumnIndex());
      }
    }

    if (indexMap.containsKey(columnName)) {
      System.out.printf("Колонка '%s' найдена в файле '%s'.%n", columnName, fileName);
      return true;
    }

    System.out.printf(ANSI_RED + "Колонка '%s' не найдена в файле '%s'.%n" + ANSI_RESET, columnName,
        fileName);
    return false;
  }



  private boolean checkCellExistsAndColumnNameMatch(String cellName, String columnName) {
    return columnName.equals(cellName) && indexMap.containsKey(cellName);
  }

}

