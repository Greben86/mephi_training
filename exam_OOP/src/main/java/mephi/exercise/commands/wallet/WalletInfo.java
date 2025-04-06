package mephi.exercise.commands.wallet;

import lombok.NoArgsConstructor;
import mephi.exercise.commands.converters.CategoriesConverter;
import mephi.exercise.entity.Category;
import mephi.exercise.entity.Operation;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Команда вывода отчета по кошельку
 * Например: {@code wallet info} - будет выведен отчет по кошельку
 * Параметр {@code "-F", "--full"} для вывода подробного отчета (со списком всех операций)
 * Параметр {@code "-C", "--category"} интерактивный и позволяет ввести список категорий, разделенных символом ";", по которым и будет выведен отчет
 */
@NoArgsConstructor
@CommandLine.Command(name = "info", description = "Показать отчет по кошельку")
public class WalletInfo implements Runnable {

    private static final String PRINT_LINE = "__________________________________________________";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    @CommandLine.ParentCommand
    private WalletGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(
            names = {"-F", "--full"},
            description = "Полный отчет"
    )
    private boolean full;

    @CommandLine.Option(
            names = {"-C", "--category"},
            description = "Список категорий",
            prompt = "Список категорий: ",
            interactive = true,
            converter = CategoriesConverter.class,
            echo = true)
    private List<String> categories;

    /**
     * Печать операций
     */
    @Override
    public void run() {
        parent.getPrinter().info(PRINT_LINE);
        parent.getPrinter().info(StringUtils.center("Отчет", PRINT_LINE.length()));

        var categoryDataSource = parent.getDictionary().getDataSource(Category.class);
        var userOperations = parent.getDictionary().getDataSource(Operation.class).getAll().stream()
                .filter(operation -> parent.getContext().getUser().getId().equals(operation.getUserId()))
                .filter(operation -> {
                    if (categories == null || categories.isEmpty()) {
                        return true;
                    }

                    return categoryDataSource.getAll().stream()
                            .filter(category -> categories.contains(category.getName()))
                            .map(Category::getId)
                            .anyMatch(operation.getCategoryId()::equals);
                })
                .sorted(Comparator.comparing(Operation::getTime))
                .toList();

        // Операции дохода
        var pushOperationList = userOperations.stream()
                .filter(operation -> BigDecimal.ZERO.compareTo(operation.getValue()) < 0)
                .toList();

        // Операции расхода
        var pullOperationList = userOperations.stream()
                .filter(operation -> BigDecimal.ZERO.compareTo(operation.getValue()) > 0)
                .toList();

        if (full) {
            parent.getPrinter().info(PRINT_LINE);
            parent.getPrinter().info("|  Дата операции  |        Сумма операции        |");
            parent.getPrinter().info(PRINT_LINE);
            pushOperationList.forEach(operation -> {
                var value = "+" + decimalFormat.format(operation.getValue());
                parent.getPrinter().info(
                        "|" + dateFormat.format(operation.getTime()) +
                        "|" + StringUtils.repeat(StringUtils.SPACE, 30-value.length()) + value + "|");
            });
            var groupedOperations = pullOperationList.stream()
                    .collect(groupingBy(operation -> {
                        if (categoryDataSource.contains(operation.getCategoryId())) {
                            return categoryDataSource.get(operation.getCategoryId()).getName();
                        }

                        return "<без категории>";
                    }, toList()));
            groupedOperations.forEach((category, operations) -> {
                parent.getPrinter().info(
                        StringUtils.center(category, PRINT_LINE.length()));
                operations.stream()
                        .sorted(Comparator.comparing(Operation::getTime))
                        .forEach(operation -> {
                            var value = decimalFormat.format(operation.getValue());
                            parent.getPrinter().info(
                                    "|" + dateFormat.format(operation.getTime()) +
                                    "|" + StringUtils.repeat(StringUtils.SPACE, 30-value.length()) + value + "|");
                        });
                var sum = operations.stream()
                        .map(Operation::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                parent.getPrinter().info(PRINT_LINE);
                parent.getPrinter().info("|Итог по категории|" + StringUtils.repeat(StringUtils.SPACE, 30-sum.toString().length()) + sum + "|");
                parent.getPrinter().info(PRINT_LINE);
                parent.getPrinter().info();
            });
        }

        var pushSum = pushOperationList.stream()
                .map(Operation::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var pullSum = pullOperationList.stream()
                .map(Operation::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var all = pushSum.add(pullSum);
        parent.getPrinter().info(PRINT_LINE);
        parent.getPrinter().info("|ВСЕГО ПРИШЛО     |" + StringUtils.repeat(StringUtils.SPACE, 30-pushSum.toString().length()) + pushSum + "|");
        parent.getPrinter().info(PRINT_LINE);
        parent.getPrinter().info("|ВСЕГО УШЛО       |" + StringUtils.repeat(StringUtils.SPACE, 30-pullSum.toString().length()) + pullSum + "|");
        parent.getPrinter().info(PRINT_LINE);
        parent.getPrinter().info("|ИТОГО            |" + StringUtils.repeat(StringUtils.SPACE, 30-all.toString().length()) + all + "|");
        parent.getPrinter().info(PRINT_LINE);
        parent.getPrinter().info();
    }

}
