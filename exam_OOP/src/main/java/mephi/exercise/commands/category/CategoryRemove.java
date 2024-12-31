package mephi.exercise.commands.category;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.Category;
import mephi.exercise.entity.Operation;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.Objects;

/**
 * Команда удаления категории
 * Например: {@code category remove на проезд} - будет удалена категория с названием "на проезд"
 * Параметр {@code "-F", "--force"} обнуляет все ссылки в операциях на удаляемую категорию
 */
@NoArgsConstructor
@CommandLine.Command(name = "remove", description = "Удалить категорию")
public class CategoryRemove implements Runnable {

    @CommandLine.ParentCommand
    private CategoryGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(
            names = {"-F", "--force"},
            description = "Обнуление ссылок")
    private boolean force;

    @CommandLine.Parameters(
            description = "Название категории",
            paramLabel = "Название")
    private String[] nameParam;

    /**
     * Удаление категории
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

        var operationDataSource = parent.getDictionary().getDataSource(Operation.class);
        var operationList = operationDataSource.getAll().stream()
                .filter(operation -> foundCategory.getId().equals(operation.getCategoryId()))
                .toList();

        if (!force && !operationList.isEmpty()) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Категория \""+name+"\" содержит операции (" + operationList.size() + ")");
        }

        operationList.forEach(operation -> operation.setCategoryId(-1));
        categoryDataSource.remove(foundCategory.getId());

        parent.getPrinter().info("Категория " + foundCategory.getName() + " удалена");
        parent.getPrinter().info();

        spec.parent().commandLine().setExecutionResult(Boolean.TRUE);
    }
}
