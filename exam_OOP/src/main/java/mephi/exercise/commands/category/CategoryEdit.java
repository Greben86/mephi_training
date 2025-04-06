package mephi.exercise.commands.category;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Category;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Команда редактирования категории
 * Например: {@code category edit на проезд} - будет запрошено новое название для категории "на проезд"
 * Параметр {@code "-B", "--budget"} задает бюджет по категории
 * Параметр {@code "-R", "--rename"} интерактивный и запрашивает новое имя категории
 * Параметр {@code "-D", "--drop} интерактивный и сбрасывает лимит категории
 */
@NoArgsConstructor
@CommandLine.Command(name = "edit", description = "Редактировать категорию")
public class CategoryEdit implements Runnable {

    @CommandLine.ParentCommand
    private CategoryGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(
            names = {"-B", "--budget"},
            description = "Бюджет категории",
            paramLabel = "Бюджет категории",
            prompt = "Бюджет категории: ",
            interactive = true)
    private BigDecimal budget;

    @CommandLine.Option(
            names = {"-R", "--rename"},
            description = "Новое название категории",
            paramLabel = "Новое название",
            prompt = "Новое название: ",
            interactive = true)
    private String newNameParam;

    @CommandLine.Option(
            names = {"-D", "--drop"},
            description = "Сбросить лимит",
            paramLabel = "Сбросить лимит")
    private boolean dropLimit;

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
                .findAny()
                .orElseThrow(() -> new CommandLine.ParameterException(spec.commandLine(), "Категория с названием \""+name+"\" не существует"));

        if (StringUtils.isNotBlank(newNameParam)) {
            var newName = StringUtils.join(newNameParam, StringUtils.SPACE).trim();
            categoryDataSource.getAll().stream()
                    .filter(category -> Objects.equals(newName, category.getName()))
                    .filter(category -> Objects.equals(parent.getContext().getUser().getId(), category.getUserId()))
                    .findAny()
                    .ifPresent(category -> {
                        throw new CommandLine.ParameterException(spec.commandLine(), "Категория с названием \""+category.getName()+"\" уже существует");
                    });

            foundCategory.setName(newName);
        }
        if (Objects.nonNull(budget)) {
            foundCategory.setBudget(budget);
            foundCategory.setLimit(budget);
        }
        if (dropLimit) {
            foundCategory.setLimit(foundCategory.getBudget());
        }

        parent.getPrinter().info("Категория " + foundCategory.getName() + " обновлена");
        parent.getPrinter().info();

        spec.parent().commandLine().setExecutionResult(Boolean.TRUE);
    }
}
