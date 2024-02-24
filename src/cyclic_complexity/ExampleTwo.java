package cyclic_complexity;

public class ExampleTwo {

  // ЦС = 10
  public void createAch() {
    Application application = applicationDs.getItem();
    StringBuilder errorMsg = new StringBuilder();
    try {
      List<TransactionDetail> transactionInfo = applicationService.findTransactions(application.getId(), "transaction-view");

      if(transactionInfo.stream().anyMatch(transactionDetail -> TransactionStatus.PENDING.equals(transactionDetail.getStatus()))) {
        List<AchResponse> achResponses = onboardService.createACH(application);
        if (!transactionInfo.isEmpty() && !achResponses.isEmpty()) {
          for (AchResponse ar : achResponses) {
            transactionInfo.stream().filter(tr -> (tr.getProductAccNum().equals(ar.getAccountNumber())) && ar.isSuccess()).forEach(td -> td.setStatus(TransactionStatus.SUCCESS));
            transactionInfo.stream().filter(tr -> (tr.getProductAccNum().equals(ar.getAccountNumber())) && !ar.isSuccess()).forEach(td -> td.setStatus(TransactionStatus.FAILED));
          }
          transactionInfo.stream().forEach(tx -> applicationService.mergeTransactionDetail(tx));
          achResponses.stream().filter(achResponse -> achResponse.getError() != null)
              .forEach(response -> {
                errorMsg.append("success: ").append(response.isSuccess()).append("\n");
                errorMsg.append("customerId: ").append(response.getCustomerId()).append("\n");
                errorMsg.append("accountNumber: ").append(response.getAccountNumber()).append("\n");
                errorMsg.append("Error: ").append(response.getError()).append("\n");
                errorMsg.append("Action: ").append(response.getAction()).append("\n\n");
              });

          if (!Strings.isNullOrEmpty(errorMsg.toString())) {
            onboardService.initialFundingErrorNotification(application, errorMsg.toString());
          }
          achResponses.stream().filter(achResponse -> achResponse.isSuccess() && achResponse.getCustomerId() != null)
              .forEach(response -> {
                errorMsg.append("success: ").append(response.isSuccess()).append("\n");
                errorMsg.append("customerId: ").append(response.getCustomerId()).append("\n");
                errorMsg.append("accountNumber: ").append(response.getAccountNumber()).append("\n");
                errorMsg.append("Action: ").append(response.getAction()).append("\n\n");
              });
          setTaskDesc(errorMsg.toString());
          showMessageDialog("ACH Created", errorMsg.toString(), MessageType.CONFIRMATION);

        } else {
          showMessageDialog("Products are not eligible for ACH creation", "Account Number not Found", MessageType.CONFIRMATION);
        }
      }else {
        showMessageDialog("Products are not eligible for ACH creation", "No pending transactions found", MessageType.CONFIRMATION);
      }
    } catch (Exception ex) {
      setTaskDesc(ex.getMessage());
      showMessageDialog("Errors during ACH Creation", ex.getMessage(), MessageType.CONFIRMATION);
    }
  }

  // Исправленный метод. Применены: избавление от else, обработка в if одного условия вместо двух,
  // убраны любые вложенности if, цикл убран из условия.
  // ЦС = 5
  public void createAchAfterRefactoring() {
    Application application = applicationDs.getItem();
    StringBuilder errorMsg = new StringBuilder();
    try {
      List<TransactionDetail> transactionInfo = applicationService.findTransactions(application.getId(), "transaction-view");

      if(transactionInfo.stream().nonMatch(transactionDetail -> TransactionStatus.PENDING.equals(transactionDetail.getStatus()))) {
        showMessageDialog("Products are not eligible for ACH creation", "No pending transactions found", MessageType.CONFIRMATION);
        return;
      }

      List<AchResponse> achResponses = onboardService.createACH(application);
      if (dataIsEmpty(transactionInfo, achResponses)) {
        showMessageDialog("Products are not eligible for ACH creation", "Account Number not Found", MessageType.CONFIRMATION);
        return;
      }

      for (AchResponse ar : achResponses) {
        transactionInfo.stream().filter(tr -> (tr.getProductAccNum().equals(ar.getAccountNumber())) && ar.isSuccess()).forEach(td -> td.setStatus(TransactionStatus.SUCCESS));
        transactionInfo.stream().filter(tr -> (tr.getProductAccNum().equals(ar.getAccountNumber())) && !ar.isSuccess()).forEach(td -> td.setStatus(TransactionStatus.FAILED));
      }
      transactionInfo.stream().forEach(tx -> applicationService.mergeTransactionDetail(tx));
      achResponses.stream().filter(achResponse -> achResponse.getError() != null)
          .forEach(response -> {
            errorMsg.append("success: ").append(response.isSuccess()).append("\n");
            errorMsg.append("customerId: ").append(response.getCustomerId()).append("\n");
            errorMsg.append("accountNumber: ").append(response.getAccountNumber()).append("\n");
            errorMsg.append("Error: ").append(response.getError()).append("\n");
            errorMsg.append("Action: ").append(response.getAction()).append("\n\n");
          });

      if (!Strings.isNullOrEmpty(errorMsg.toString())) {
        onboardService.initialFundingErrorNotification(application, errorMsg.toString());
      }
      achResponses.stream().filter(achResponse -> achResponse.isSuccess() && achResponse.getCustomerId() != null)
          .forEach(response -> {
            errorMsg.append("success: ").append(response.isSuccess()).append("\n");
            errorMsg.append("customerId: ").append(response.getCustomerId()).append("\n");
            errorMsg.append("accountNumber: ").append(response.getAccountNumber()).append("\n");
            errorMsg.append("Action: ").append(response.getAction()).append("\n\n");
          });
      setTaskDesc(errorMsg.toString());
      showMessageDialog("ACH Created", errorMsg.toString(), MessageType.CONFIRMATION);
    } catch (Exception ex) {
      setTaskDesc(ex.getMessage());
      showMessageDialog("Errors during ACH Creation", ex.getMessage(), MessageType.CONFIRMATION);
    }
  }



  private boolean dataIsEmpty(List<TransactionDetail> transactionInfo, List<AchResponse> achResponses) {
    return transactionInfo.isEmpty() || achResponses.isEmpty();
  }
}
