package mephi.exercise.commands.wallet;

import lombok.Getter;
import lombok.Setter;
import mephi.exercise.commands.AbstractCommand;
import mephi.exercise.entity.Category;
import mephi.exercise.entity.Context;
import mephi.exercise.repository.DataSourceDictionary;
import mephi.exercise.service.Printer;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.math.BigDecimal;

/**
 * Команда редактирования ссылки пользователя
 * Например: {@code edit ltnrhhcmdd -C 10 -E 1d} - будет обновлена короткая ссылка с ключом ltnrhhcmdd,
 *   по ссылке можно будет перейти 10 раз, и ссылка будет доступна 1 день
 */
@CommandLine.Command(name = "wallet", description = "Управление кошельком",
        subcommands = {WalletPush.class, WalletPull.class, WalletSend.class, WalletList.class})
public class WalletGroup extends AbstractCommand {

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @Setter
    @Getter
    private Category category;
    @Setter
    @Getter
    private BigDecimal amount;

    public WalletGroup(DataSourceDictionary dictionary, Context context, Printer printer) {
        super(dictionary, context, printer);
    }

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Эта команда не может быть вызвана без параметров");
    }

    public static class Parameters {
        @CommandLine.ParentCommand
        private WalletGroup parent;

        @CommandLine.Spec
        private CommandLine.Model.CommandSpec spec;

        @CommandLine.Option(
                names = {"-C", "--category"},
                description = "Категория",
                prompt = "Категория: ",
                interactive = true)
        private void setCategory(String[] categoryNameParts) {
            parent.setCategory(null);
            var categoryName = StringUtils.join(categoryNameParts, StringUtils.SPACE);
            if (StringUtils.isNotBlank(categoryName)) {
                var categoryDataSource = parent.getDictionary().getDataSource(Category.class);
                categoryDataSource.getAll().stream()
                        .filter(category -> category.getName().equals(categoryName))
                        .findAny()
                        .ifPresent(parent::setCategory);
            }
        }

        @CommandLine.Parameters(
                description = "Количество денег",
                paramLabel = "Количество")
        private void setAmount(BigDecimal amount) {
            if (amount == null || BigDecimal.ZERO.equals(amount)) {
                throw new IllegalStateException("Количество денег не может быть 0");
            }

            if (amount.longValue() < 0) {
                throw new IllegalStateException("Количество денег не может быть меньше 0");
            }

            parent.setAmount(amount);
        }
    }
}
