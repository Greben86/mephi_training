package mephi.exercise.commands.wallet;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Operation;
import picocli.CommandLine;

import java.util.Date;

/**
 * Команда добавления денег в кошелек
 * Например: {@code wallet push 500} - будет выполнена операция добавления 500 в кошелек
 */
@NoArgsConstructor
@CommandLine.Command(name = "push", description = "Положить деньги в кошелек")
public class WalletPush implements Runnable {

    @CommandLine.ParentCommand
    private WalletGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Mixin
    private WalletGroup.Parameters waletParameters;

    @Override
    public void run() {
        var operationDataSource = parent.getDictionary().getDataSource(Operation.class);
        var operation = Operation.builder()
                .setId(operationDataSource.getNextId())
                .setUserId(parent.getContext().getUser().getId())
                .setCategoryId(-1)
                .setTime(new Date())
                .setValue(parent.getAmount())
                .build();
        operationDataSource.add(operation.getId(), operation);
        parent.getContext().getUser().setAmount(parent.getContext().getUser().getAmount().add(parent.getAmount()));
        parent.getPrinter().info("Операция +" + operation.getValue() + " выполнена");
        parent.getPrinter().info();

        spec.parent().commandLine().setExecutionResult(Boolean.TRUE);
    }
}
