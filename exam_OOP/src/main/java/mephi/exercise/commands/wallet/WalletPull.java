package mephi.exercise.commands.wallet;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Category;
import mephi.exercise.entity.Operation;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.Date;

/**
 * Команда изымания денег из кошелька
 * Например: {@code wallet pull 500} - будет выполнена операция изъятия 500 из кошелька
 * Параметр {@code "-C", "--category"} интерактивный, если его указать при выполнении операции будет запрошена категория
 */
@NoArgsConstructor
@CommandLine.Command(name = "pull", description = "Взять деньги из кошелька")
public class WalletPull implements Runnable {

    @CommandLine.ParentCommand
    private WalletGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(
            names = {"-C", "--category"},
            description = "Категория",
            prompt = "Категория: ",
            interactive = true,
            echo = true)
    private String categoryName;

    @CommandLine.Mixin
    private WalletGroup.Parameters waletParameters;

    /**
     * Операция изъятия денег из кошелька
     */
    @Override
    public void run() {
        spec.parent().commandLine().setExecutionResult(Boolean.FALSE);
        if (parent.getContext().getUser().getAmount() == null
                || parent.getAmount().compareTo(parent.getContext().getUser().getAmount()) > 0) {
            throw new CommandLine.ParameterException(spec.commandLine(),
                    "Запрошенная сумма превышает сумму в кошельке пользователя");
        }

        var category = readCategory();
        var operationDataSource = parent.getDictionary().getDataSource(Operation.class);
        var operation = Operation.builder()
                .setId(operationDataSource.getNextId())
                .setUserId(parent.getContext().getUser().getId())
                .setCategoryId(category != null ? category.getId() : -1)
                .setTime(new Date())
                .setValue(parent.getAmount().negate())
                .build();
        operationDataSource.add(operation.getId(), operation);
        if (category != null && category.getLimit() != null) {
            category.setLimit(category.getLimit().subtract(parent.getAmount()));
        }
        parent.getContext().getUser().setAmount(parent.getContext().getUser().getAmount().subtract(parent.getAmount()));
        parent.getPrinter().info("Операция " + operation.getValue() + " выполнена");
        parent.getPrinter().info();

        spec.parent().commandLine().setExecutionResult(Boolean.TRUE);
    }

    /**
     * Чтение категории и проверка лимита
     *
     * @return объект категории
     */
    private Category readCategory() {
        if (StringUtils.isBlank(categoryName)) {
            return null;
        }

        var categoryName = StringUtils.join(this.categoryName, StringUtils.SPACE);
        var categoryDataSource = parent.getDictionary().getDataSource(Category.class);
        Category category = categoryDataSource.getAll().stream()
                .filter(c -> c.getName().equals(categoryName.trim()))
                .findAny()
                .orElseThrow(() -> new CommandLine.ParameterException(spec.commandLine(),
                        "Категория с названием \""+categoryName+"\" не существует"));

        if (category.getLimit() != null) {
            if (parent.getAmount().compareTo(category.getLimit()) > 0) {
                throw new CommandLine.ParameterException(spec.commandLine(),
                        "Превышен лимит по категории \"" + category + "\", операция отменена");
            }
        }

        return category;
    }
}
