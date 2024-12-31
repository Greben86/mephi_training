package mephi.exercise.commands.wallet;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Operation;
import picocli.CommandLine;

import java.util.Date;
import java.util.Objects;

/**
 * Команда авторизации пользователя
 * Например: {@code login fbd14e52-14e7-4e95-9cc3-ff14a70d638e} - будет авторизован пользователь
 * с идентификатором fbd14e52-14e7-4e95-9cc3-ff14a70d638e
 */
@NoArgsConstructor
@CommandLine.Command(name = "pull", description = "Взять деньги из кошелька")
public class WalletPull implements Runnable {

    @CommandLine.ParentCommand
    private WalletGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Mixin
    private WalletGroup.Parameters waletParameters;

    @Override
    public void run() {
        if (parent.getContext().getUser().getAmount() == null
                || parent.getAmount().compareTo(parent.getContext().getUser().getAmount()) > 0) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Запрошенная сумма превышает сумму в кошельке пользователя");
        }

        Integer categoryId = -1;
        if (Objects.nonNull(parent.getCategory())) {
            if (Objects.nonNull(parent.getCategory().getLimit())) {
                if (parent.getAmount().compareTo(parent.getCategory().getLimit()) > 0) {
                    throw new CommandLine.ParameterException(spec.commandLine(), "Превышен лимит по категории \"" + parent.getCategory() + "\", операция отменена");
                }

                parent.getCategory().setLimit(parent.getCategory().getLimit().subtract(parent.getAmount()));
            }

            categoryId = parent.getCategory().getId();
        }

        var operationDataSource = parent.getDictionary().getDataSource(Operation.class);
        var operation = Operation.builder()
                .setId(operationDataSource.getNextId())
                .setUserId(parent.getContext().getUser().getId())
                .setCategoryId(categoryId)
                .setTime(new Date())
                .setValue(parent.getAmount().negate())
                .build();
        operationDataSource.add(operation.getId(), operation);
        parent.getContext().getUser().setAmount(parent.getContext().getUser().getAmount().subtract(parent.getAmount()));
    }
}
