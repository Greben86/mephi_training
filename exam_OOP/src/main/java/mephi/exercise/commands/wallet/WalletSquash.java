package mephi.exercise.commands.wallet;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Operation;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Команда сворачивания всех операций в одну
 * Например: {@code wallet squash} - все операции будут свернуты в одну, история операций будет утеряна
 */
@NoArgsConstructor
@CommandLine.Command(name = "squash", description = "Свернуть все операции в одну")
public class WalletSquash implements Runnable {

    @CommandLine.ParentCommand
    private WalletGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    /**
     * Операция сворачивания всех операций в одну, история операций будет утеряна
     */
    @Override
    public void run() {
        var operationDataSource = parent.getDictionary().getDataSource(Operation.class);
        var operationList = operationDataSource.getAll().stream()
                .filter(operation -> parent.getContext().getUser().getId().equals(operation.getUserId()))
                .toList();
        operationList.stream()
                .map(Operation::getId)
                .forEach(operationDataSource::remove);
        var sum = operationList.stream()
                .map(Operation::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var operation = Operation.builder()
                .setId(operationDataSource.getNextId())
                .setUserId(parent.getContext().getUser().getId())
                .setCategoryId(-1)
                .setTime(new Date())
                .setValue(sum)
                .build();
        operationDataSource.add(operation.getId(), operation);
        parent.getPrinter().info("Операция выполнена");
        parent.getPrinter().info();

        spec.parent().commandLine().setExecutionResult(Boolean.TRUE);
    }
}
