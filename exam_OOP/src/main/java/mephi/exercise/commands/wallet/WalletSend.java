package mephi.exercise.commands.wallet;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Operation;
import picocli.CommandLine;

import java.util.Date;

/**
 * Команда авторизации пользователя
 * Например: {@code login fbd14e52-14e7-4e95-9cc3-ff14a70d638e} - будет авторизован пользователь
 * с идентификатором fbd14e52-14e7-4e95-9cc3-ff14a70d638e
 */
@NoArgsConstructor
@CommandLine.Command(name = "send", description = "Положить деньги в кошелек")
public class WalletSend implements Runnable {

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
                .setCategoryId(parent.getCategory() != null ? parent.getCategory().getId() : null)
                .setTime(new Date())
                .setValue(parent.getAmount())
                .build();
        operationDataSource.add(operation.getId(), operation);
    }
}
