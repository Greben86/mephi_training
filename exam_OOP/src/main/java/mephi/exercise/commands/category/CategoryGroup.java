package mephi.exercise.commands.category;

import mephi.exercise.commands.AbstractCommand;
import mephi.exercise.entity.Category;
import mephi.exercise.entity.Context;
import mephi.exercise.repository.DataSourceDictionary;
import mephi.exercise.service.Printer;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Команда редактирования ссылки пользователя
 * Например: {@code edit ltnrhhcmdd -C 10 -E 1d} - будет обновлена короткая ссылка с ключом ltnrhhcmdd,
 *   по ссылке можно будет перейти 10 раз, и ссылка будет доступна 1 день
 */
@CommandLine.Command(name = "category", description = "")
public class CategoryGroup extends AbstractCommand {

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    public CategoryGroup(DataSourceDictionary dictionary, Context context, Printer printer) {
        super(dictionary, context, printer);
    }

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Эта команда не может быть вызвана без параметров");
    }

    @CommandLine.Command(name = "list", description = "Показать все категории")
    public void list() {
        var categoryDataSource = dictionary.getDataSource(Category.class);
        var categories = categoryDataSource.getAll().stream()
                .toList();
        if (categories.isEmpty()) {
            printer.info("Список пуст");
        }

        categories.forEach(category ->
                printer.info(category.toString()));
        printer.info();
    }

    @CommandLine.Command(name = "add")
    public void add(
            @CommandLine.Option(
                    names = {"-L", "--limit"},
                    description = "Лимит категории")
            final BigDecimal limit,
            @CommandLine.Parameters(
                    description = "Название категории",
                    paramLabel = "Название")
            final String[] nameParam) {
        var name = StringUtils.join(nameParam, StringUtils.SPACE).trim();
        var categoryDataSource = dictionary.getDataSource(Category.class);
        var foundCategory = categoryDataSource.getAll().stream()
                .filter(category -> StringUtils.equals(name, category.getName()))
                .filter(category -> Objects.equals(context.getUser().getId(), category.getUserId()))
                .findAny();
        if (foundCategory.isPresent()) {
            throw new IllegalStateException("Категория с таким названием уже существует");
        }

        var category = new Category();
        category.setId(categoryDataSource.getNextId());
        category.setUserId(context.getUser().getId());
        category.setName(name);
        category.setLimit(limit);
        categoryDataSource.add(category.getId(), category);

        printer.info("Категория " + category.getName() + " добавлена");
        printer.info();
    }

    @CommandLine.Command(name = "edit")
    public void edit(
            @CommandLine.Option(
                    names = {"-L", "--limit"},
                    description = "Лимит категории")
            final BigDecimal limit,
            @CommandLine.Option(
                    names = {"-I", "--id"},
                    description = "ID категории",
                    required = true)
            final Integer id,
            @CommandLine.Parameters(
                    description = "Новое название",
                    paramLabel = "Название")
            final String[] nameParam) {
        var name = StringUtils.join(nameParam, StringUtils.SPACE).trim();
        var categoryDataSource = dictionary.getDataSource(Category.class);
        var foundCategory = categoryDataSource.getAll().stream()
                .filter(category -> !Objects.equals(id, category.getId()))
                .filter(category -> Objects.equals(name, category.getName()))
                .filter(category -> Objects.equals(context.getUser().getId(), category.getUserId()))
                .findAny();
        if (foundCategory.isPresent()) {
            throw new IllegalStateException("Категория с таким названием уже существует");
        }

        var category = categoryDataSource.get(id);
        if (category == null) {
            throw new IllegalStateException("Категория не существует");
        }

        category.setName(name);
        if (Objects.nonNull(limit)) {
            category.setLimit(limit);
        }

        printer.info("Категория " + category.getName() + " обновлена");
        printer.info();
    }
}
