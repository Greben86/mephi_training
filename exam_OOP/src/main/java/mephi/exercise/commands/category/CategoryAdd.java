package mephi.exercise.commands.category;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Category;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Команда добавления новой категории
 * Например: {@code category add на проезд} - будет добавлена новая категория с названием "на проезд"
 * Параметр {@code "-B", "--budget} задает бюджет по категории
 */
@NoArgsConstructor
@CommandLine.Command(name = "add", description = "Добавить новую категорию")
public class CategoryAdd implements Runnable {

    @CommandLine.ParentCommand
    private CategoryGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(
            names = {"-B", "--budget"},
            description = "Бюджет категории")
    private BigDecimal budget;

    @CommandLine.Parameters(
            description = "Название категории",
            paramLabel = "Название")
    private String[] nameParam;

    /**
     * Добавление новой категории
     */
    @Override
    public void run() {
        spec.parent().commandLine().setExecutionResult(Boolean.FALSE);
        if (nameParam == null || nameParam.length == 0) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Название категории не может быть пустым");
        }

        var name = StringUtils.join(nameParam, StringUtils.SPACE).trim();
        var categoryDataSource = parent.getDictionary().getDataSource(Category.class);
        var foundCategory = categoryDataSource.getAll().stream()
                .filter(category -> StringUtils.equals(name, category.getName()))
                .filter(category -> Objects.equals(parent.getContext().getUser().getId(), category.getUserId()))
                .findAny();
        if (foundCategory.isPresent()) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Категория с таким названием уже существует");
        }

        var category = new Category();
        category.setId(categoryDataSource.getNextId());
        category.setUserId(parent.getContext().getUser().getId());
        category.setName(name);
        category.setBudget(budget);
        category.setLimit(budget);
        categoryDataSource.add(category.getId(), category);

        parent.getPrinter().info("Категория " + category.getName() + " добавлена");
        parent.getPrinter().info();

        spec.parent().commandLine().setExecutionResult(Boolean.TRUE);
    }
}
