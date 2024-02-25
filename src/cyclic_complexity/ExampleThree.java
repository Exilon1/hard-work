package cyclic_complexity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ExampleThree {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";

  public static final String COMPANY_ID = "company_id";
  public static final String RDM_ID = "rdm_id";
  public static final String CLIENT_ID = "client_id";

  private final Map<String, Integer> indexMap = new HashMap<>();

  // ЦС = 10
  private boolean requiredColumns(Sheet sheet) {
    Row headerRow = sheet.getRow(0);
    if (Objects.nonNull(headerRow)) {
      for (Cell cell : headerRow) {
        if (CellType.STRING == cell.getCellType()) {
          String cellName = cell.getStringCellValue().trim().toLowerCase();
          switch (cellName) {
            case (COMPANY_ID):
              if (indexMap.containsKey(cellName)) {
                System.out.println(
                    ANSI_RED + "Колонка 'company_id' дублируется. Конфликт данных." + ANSI_RESET);
                return false;
              }
              indexMap.put(cellName, cell.getColumnIndex());
              System.out.println("Колонка 'company_inn' найдена.");
              break;
            case (RDM_ID):
              if (indexMap.containsKey(cellName)) {
                System.out.println(
                    ANSI_RED + "Колонка 'rdm_id' дублируется. Конфликт данных." + ANSI_RESET);
                return false;
              }
              indexMap.put(cellName, cell.getColumnIndex());
              System.out.println("Колонка 'rdm_id' найдена.");
              break;
            case (CLIENT_ID):
              if (indexMap.containsKey(cellName)) {
                System.out.println(
                    ANSI_RED + "Колонка 'client_id' дублируется. Конфликт данных."
                        + ANSI_RESET);
                return false;
              }
              indexMap.put(cellName, cell.getColumnIndex());
              System.out.println("Колонка 'client_id' найдена.");
              break;
          }
        }
      }
    }
    return validateColumnSet();
  }

  // Исправленный метод. Применены: ad-hoc, убраны любые вложенности if, цикл убран из условия,
  // Option обёртка для null.
  // ЦС = 7
  private boolean requiredColumnsAfterRefactoring(Sheet sheet) {
    Row headerRow = sheet.getRow(0);
    boolean containsColumn;

    if (Objects.isNull(headerRow)) {
      return false;
    }

    for (Cell cell : headerRow) {
      if (CellType.STRING != cell.getCellType()) {
        return false;
      }

      String cellName = cell.getStringCellValue().trim().toLowerCase();
      ParentCase currentCase = convertStringToCase(cellName).get();
      if (currentCase instanceof CompanyCase) {
        containsColumn = containsColumn((CompanyCase) convertStringToCase(cellName).get(), cell);
      }
      if (currentCase instanceof RdmCase) {
        containsColumn = containsColumn((RdmCase) convertStringToCase(cellName).get(), cell);
      }
      if (currentCase instanceof ClientCase) {
        containsColumn = containsColumn((ClientCase) convertStringToCase(cellName).get(), cell);
      }
    }
    return containsColumn && validateColumnSet();
  }



  private boolean containsColumn(CompanyCase currentCase, Cell cell) {
    if (indexMap.containsKey(COMPANY_ID)) {
      System.out.println(
          ANSI_RED + "Колонка 'company_id' дублируется. Конфликт данных." + ANSI_RESET);
      return false;
    }
    indexMap.put(COMPANY_ID, cell.getColumnIndex());
    System.out.println("Колонка 'company_inn' найдена.");
    return true;
  }

  private boolean containsColumn(RdmCase currentCase, Cell cell) {
    if (indexMap.containsKey(RDM_ID)) {
      System.out.println(
          ANSI_RED + "Колонка 'rdm_id' дублируется. Конфликт данных." + ANSI_RESET);
      return false;
    }
    indexMap.put(RDM_ID, cell.getColumnIndex());
    System.out.println("Колонка 'rdm_id' найдена.");
    return true;
  }

  private boolean containsColumn(ClientCase currentCase, Cell cell) {
    if (indexMap.containsKey(cellName)) {
      System.out.println(
          ANSI_RED + "Колонка 'client_id' дублируется. Конфликт данных."
              + ANSI_RESET);
      return false;
    }
    indexMap.put(CLIENT_ID, cell.getColumnIndex());
    System.out.println("Колонка 'client_id' найдена.");
    return true;
  }

  private Optional<ParentCase> convertStringToCase(String string) {
    if (COMPANY_ID.equals(string)) {
      return Optional.of(new CompanyCase());
    }
    if (RDM_ID.equals(string)) {
      return Optional.of(new RdmCase());
    }
    if (CLIENT_ID.equals(string)) {
      return Optional.of(new ClientCase());
    }

    return Optional.empty();
  }
}

class ParentCase {}

class CompanyCase extends ParentCase {}
class RdmCase extends ParentCase {}
class ClientCase extends ParentCase {}
