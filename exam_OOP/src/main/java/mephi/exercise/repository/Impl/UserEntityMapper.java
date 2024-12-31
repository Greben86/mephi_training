package mephi.exercise.repository.Impl;

import lombok.RequiredArgsConstructor;
import mephi.exercise.entity.User;
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
public class UserEntityMapper implements EntityMapper<User> {

    private static final int FIELD_COUNT = User.class.getDeclaredFields().length;
    private static final String USER_UID = String.valueOf(ObjectStreamClass.lookup(User.class).getSerialVersionUID());

    private final Printer printer;

    /**
     * {@inheritDoc}
     */
    @Override
    public User map(String line) {
        if (StringUtils.isBlank(line)) {
            return null;
        }

        var splittedLine = line.split(DELIMITER);
        if (!USER_UID.equals(splittedLine[0])) {
            printer.error("Не совпадает версия объекта " + User.class.getName());
            return null;
        }

        if (splittedLine.length != FIELD_COUNT) {
            printer.error("Не совпадает количество атрибутов объекта " + User.class.getName());
            return null;
        }

        return User.builder()
                .setId(Integer.parseInt(splittedLine[1]))
                .setLogin(splittedLine[2])
                .setPassword(splittedLine[3])
                .setAmount(new BigDecimal(splittedLine[4]))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String map(User user) {
        return new StringBuilder()
                .append(USER_UID)
                .append(DELIMITER)
                .append(user.getId())
                .append(DELIMITER)
                .append(user.getLogin())
                .append(DELIMITER)
                .append(user.getPassword())
                .append(DELIMITER)
                .append(user.getAmount())
                .toString();
    }
}
