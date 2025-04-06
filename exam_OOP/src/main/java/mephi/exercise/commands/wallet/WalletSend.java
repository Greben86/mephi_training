package mephi.exercise.commands.wallet;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.User;
import picocli.CommandLine;

/**
 * Команда перевода денег другому пользователю
 * Например: {@code wallet send Viktor 500} - пользователю Viktor будет отправлен перевод 500
 */
@NoArgsConstructor
@CommandLine.Command(name = "send", description = "Положить деньги в кошелек")
public class WalletSend implements Runnable {

    @CommandLine.ParentCommand
    private WalletGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Parameters(
            description = "Получатель",
            paramLabel = "Получатель")
    private String user;

    @CommandLine.Mixin
    private WalletGroup.Parameters waletParameters;

    /**
     * Перевод денег другому пользователю
     */
    @Override
    public void run() {
        var currentUser = parent.getContext().getUser();
        var userDataSource = parent.getDictionary().getDataSource(User.class);
        var recipient = userDataSource.getAll().stream()
                .filter(u -> u.getLogin().equals(user))
                .findAny()
                .orElseThrow(() -> new CommandLine.ParameterException(spec.commandLine(), "Пользователь с логином \""+user+"\" не найден"));
        try {
            var pullCommand = new CommandLine(parent);
            pullCommand.execute("pull", parent.getAmount().toString());
            Boolean result = pullCommand.getExecutionResult();
            if (!result) {
                throw new CommandLine.ParameterException(spec.commandLine(), "Операция не выполнена");
            }
            parent.getContext().setUser(recipient);
            new CommandLine(parent).execute("push", parent.getAmount().toString());
            parent.getPrinter().info("Операция " + parent.getAmount().negate() + " выполнена");
            parent.getPrinter().info();
        } finally {
            parent.getContext().setUser(currentUser);
        }
    }
}
