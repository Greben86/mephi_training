package mephi.exercise.repository.Impl;

import lombok.RequiredArgsConstructor;
import mephi.exercise.entity.Category;
import mephi.exercise.repository.EntityMapper;
import mephi.exercise.service.Printer;
import org.apache.commons.lang3.StringUtils;

import java.io.ObjectStreamClass;
import java.math.BigDecimal;

import static mephi.exercise.FinancialManagement.DELIMITER;

/**
 * Маппер пользователей
 */
@RequiredArgsConstructor
public class CategoryEntityMapper implements EntityMapper<Category> {

    private static final int FIELD_COUNT = Category.class.getDeclaredFields().length;
    private static final String CATEGORY_UID = String.valueOf(ObjectStreamClass.lookup(Category.class).getSerialVersionUID());

    private final Printer printer;

    /**
     * {@inheritDoc}
     */
    @Override
    public Category map(String line) {
        if (StringUtils.isBlank(line)) {
            return null;
        }

        var splittedLine = line.split(DELIMITER);
        if (!CATEGORY_UID.equals(splittedLine[0])) {
            printer.error("Не совпадает версия объекта " + Category.class.getName());
            return null;
        }

        if (splittedLine.length != FIELD_COUNT) {
            printer.error("Не совпадает количество атрибутов объекта " + Category.class.getName());
            return null;
        }

        return Category.builder()
                .setId(Integer.parseInt(splittedLine[1]))
                .setUserId(Integer.parseInt(splittedLine[2]))
                .setName(splittedLine[3])
                .setBudget("null".equals(splittedLine[4]) ? null : new BigDecimal(splittedLine[4]))
                .setLimit("null".equals(splittedLine[5]) ? null : new BigDecimal(splittedLine[5]))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String map(Category category) {
        return new StringBuilder()
                .append(CATEGORY_UID)
                .append(DELIMITER)
                .append(category.getId())
                .append(DELIMITER)
                .append(category.getUserId())
                .append(DELIMITER)
                .append(category.getName())
                .append(DELIMITER)
                .append(category.getBudget())
                .append(DELIMITER)
                .append(category.getLimit())
                .toString();
    }
}
