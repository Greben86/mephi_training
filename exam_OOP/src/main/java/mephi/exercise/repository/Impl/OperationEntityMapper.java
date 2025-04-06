package mephi.exercise.repository.Impl;

import lombok.RequiredArgsConstructor;
import mephi.exercise.entity.Operation;
import mephi.exercise.repository.EntityMapper;
import mephi.exercise.service.Printer;
import org.apache.commons.lang3.StringUtils;

import java.io.ObjectStreamClass;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static mephi.exercise.FinancialManagement.DELIMITER;

/**
 * Маппер операций
 */
@RequiredArgsConstructor
public class OperationEntityMapper implements EntityMapper<Operation> {

    private static final int FIELD_COUNT = Operation.class.getDeclaredFields().length;
    private static final String OPERATION_UID = String.valueOf(ObjectStreamClass.lookup(Operation.class).getSerialVersionUID());

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private final Printer printer;

    /**
     * {@inheritDoc}
     */
    @Override
    public Operation map(String line) {
        if (StringUtils.isBlank(line)) {
            return null;
        }

        var splittedLine = line.split(DELIMITER);
        if (!OPERATION_UID.equals(splittedLine[0])) {
            printer.error("Не совпадает версия объекта " + Operation.class.getName());
            return null;
        }

        if (splittedLine.length != FIELD_COUNT) {
            printer.error("Не совпадает количество атрибутов объекта " + Operation.class.getName());
            return null;
        }

        try {
            return Operation.builder()
                    .setId(Integer.parseInt(splittedLine[1]))
                    .setUserId(Integer.parseInt(splittedLine[2]))
                    .setCategoryId(Integer.parseInt(splittedLine[3]))
                    .setTime(dateFormat.parse(splittedLine[4]))
                    .setValue(new BigDecimal(splittedLine[5]))
                    .build();
        } catch (ParseException e) {
            printer.error(e.getMessage());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String map(Operation operation) {
        return new StringBuilder()
                .append(OPERATION_UID)
                .append(DELIMITER)
                .append(operation.getId())
                .append(DELIMITER)
                .append(operation.getUserId())
                .append(DELIMITER)
                .append(operation.getCategoryId())
                .append(DELIMITER)
                .append(dateFormat.format(operation.getTime()))
                .append(DELIMITER)
                .append(operation.getValue())
                .toString();
    }
}
