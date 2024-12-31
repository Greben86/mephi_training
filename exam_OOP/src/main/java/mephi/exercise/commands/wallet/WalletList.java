package mephi.exercise.commands.wallet;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Category;
import mephi.exercise.entity.Operation;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Команда авторизации пользователя
 * Например: {@code login fbd14e52-14e7-4e95-9cc3-ff14a70d638e} - будет авторизован пользователь
 * с идентификатором fbd14e52-14e7-4e95-9cc3-ff14a70d638e
 */
@NoArgsConstructor
@CommandLine.Command(name = "list", description = "Показать отчет по кошельку")
public class WalletList implements Runnable {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    @CommandLine.ParentCommand
    private WalletGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        var operationDataSource = parent.getDictionary().getDataSource(Operation.class);
        var categoryDataSource = parent.getDictionary().getDataSource(Category.class);
        var groupedOperations = operationDataSource.getAll().stream()
                .filter(operation -> parent.getContext().getUser().getId().equals(operation.getUserId()))
                .sorted(Comparator.comparing(Operation::getTime))
                .collect(groupingBy(operation -> {
                    if (categoryDataSource.contains(operation.getCategoryId())) {
                        return categoryDataSource.get(operation.getCategoryId()).getName();
                    }

                    return "<без категории>";
                }, toList()));

        parent.getPrinter().info("__________________________________________________");
        parent.getPrinter().info("|                     Отчет                      |");
        parent.getPrinter().info("__________________________________________________");
        parent.getPrinter().info("|  Дата операции  |        Сумма операции        |");
        parent.getPrinter().info("__________________________________________________");
        groupedOperations.forEach((category, operations) -> {
            parent.getPrinter().info(
                    StringUtils.center(category, "__________________________________________________".length()));
            operations.stream()
                    .sorted(Comparator.comparing(Operation::getTime))
                    .forEach(operation -> {
                        var value = decimalFormat.format(operation.getValue());
                        parent.getPrinter().info(
                                "|" + dateFormat.format(operation.getTime()) +
                                "|" + StringUtils.repeat(StringUtils.SPACE, 30-value.length()) + value + "|");
            });
        });
        var all = operationDataSource.getAll().stream()
                .map(Operation::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        parent.getPrinter().info("_________________________________________________");
        parent.getPrinter().info("|ИТОГО            |" + StringUtils.repeat(StringUtils.SPACE, 30-all.toString().length()) + all + "|");
        parent.getPrinter().info("_________________________________________________");
    }

}
