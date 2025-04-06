package mephi.exercise.commands.converters;

import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static mephi.exercise.FinancialManagement.DELIMITER;

/**
 * Конвертер списка категорий введенных вручную и разделенных символом ";" в коллекцию названий категорий
 */
public class CategoriesConverter  implements CommandLine.ITypeConverter<List<String>> {

    /**
     * Конвертирует список категорий введенных вручную и разделенных символом ";" в коллекцию названий категорий
     *
     * @param categoryArg список категорий введенных вручную и разделенных символом ";"
     * @return коллекция названий категорий
     */
    @Override
    public List<String> convert(String categoryArg) {
        if (StringUtils.isBlank(categoryArg)) {
            return Collections.emptyList();
        }

        var categories = StringUtils.split(categoryArg, DELIMITER);
        return Stream.of(categories)
                .map(StringUtils::trim)
                .toList();
    }
}
