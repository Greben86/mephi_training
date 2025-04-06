package mephi.exercise.commands.category;

import mephi.exercise.commands.AbstractCommand;
import mephi.exercise.entity.Category;
import mephi.exercise.entity.Context;
import mephi.exercise.entity.Operation;
import mephi.exercise.repository.DataSourceDictionary;
import mephi.exercise.service.Printer;
import picocli.CommandLine;

import java.math.BigDecimal;

/**
 * Корневая команда управления категориями,
 * содержит подкоманды:
 *   add - добавление новой категории
 *   edit - редактирование категории
 *   remove - удаление категории
 *   list - показать все категории
 */
@CommandLine.Command(name = "category", description = "Управление категориями",
        subcommands = {CategoryAdd.class, CategoryEdit.class, CategoryRemove.class})
public class CategoryGroup extends AbstractCommand {

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    /**
     * Конструктор
     *
     * @param dictionary библиотека данных
     * @param context контекст
     * @param printer принтер
     */
    public CategoryGroup(DataSourceDictionary dictionary, Context context, Printer printer) {
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
     * Подкоманда отображения списка категорий
     */
    @CommandLine.Command(name = "list", description = "Показать все категории")
    public void list() {
        var operationDataSource = dictionary.getDataSource(Operation.class);
        var categoryDataSource = dictionary.getDataSource(Category.class);
        var categories = categoryDataSource.getAll().stream()
                .filter(category -> context.getUser().getId().equals(category.getUserId()))
                .toList();
        if (categories.isEmpty()) {
            printer.info("Список пуст");
            return;
        }

        printer.info("Категории расхода пользователя:");
        categories.forEach(category -> {
            var sum = operationDataSource.getAll().stream()
                    .filter(operation -> category.getId().equals(operation.getCategoryId()))
                    .map(Operation::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            printer.info(String.format(
                    "Категория \"%s\", бюджет \"%s\", оставшийся лимит \"%s\", всего операций на сумму \"%s\"",
                    category.getName(), category.getBudget(), category.getLimit(), sum));
        });
        printer.info();
    }
}
