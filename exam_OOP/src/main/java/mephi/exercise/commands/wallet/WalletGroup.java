package mephi.exercise.commands.wallet;

import lombok.Getter;
import lombok.Setter;
import mephi.exercise.commands.AbstractCommand;
import mephi.exercise.entity.Context;
import mephi.exercise.repository.DataSourceDictionary;
import mephi.exercise.service.Printer;
import picocli.CommandLine;

import java.math.BigDecimal;

/**
 * Корневая команда управления кошельком,
 * содержит подкоманды:
 *   push - Положить деньги в кошелек
 *   pull - Взять деньги из кошелька
 *   send - Отправить деньги другому пользователю
 *   squash - Свернуть все операции в одну
 *   info - Показать отчет по кошельку
 */
@CommandLine.Command(name = "wallet", description = "Управление кошельком",
        subcommands = {WalletPush.class, WalletPull.class, WalletSend.class, WalletSquash.class, WalletInfo.class})
public class WalletGroup extends AbstractCommand {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Setter
    @Getter
    private BigDecimal amount;

    /**
     * Конструктор
     *
     * @param dictionary библиотека данных
     * @param context контекст
     * @param printer принтер
     */
    public WalletGroup(DataSourceDictionary dictionary, Context context, Printer printer) {
        super(dictionary, context, printer);
    }

    /**
     * Корневая команда не может быть вызвана без подкоманд
     */
    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Эта команда не может быть вызвана без параметров");
    }

    /**
     * Общий параметр - количество денег
     */
    public static class Parameters {
        @CommandLine.ParentCommand
        private WalletGroup parent;

        @CommandLine.Spec
        private CommandLine.Model.CommandSpec spec;

        @CommandLine.Parameters(
                description = "Количество денег",
                paramLabel = "Количество")
        private void setAmount(BigDecimal amount) {
            if (amount == null || BigDecimal.ZERO.equals(amount)) {
                throw new CommandLine.ParameterException(spec.commandLine(), "Количество денег не может быть 0");
            }

            if (amount.longValue() < 0) {
                throw new CommandLine.ParameterException(spec.commandLine(), "Количество денег не может быть меньше 0");
            }

            parent.setAmount(amount);
        }
    }
}
