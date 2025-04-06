package mephi.exercise.commands.user;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.User;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.Date;

import static mephi.exercise.FinancialManagement.DELIMITER;

/**
 * Команда регистрации нового пользователя
 * Например: {@code add Viktor} - будет запрошен пароль нового пользователя и зарегистрирован пользователь с логином Viktor
 */
@NoArgsConstructor
@CommandLine.Command(name = "add", description = "Регистрация нового пользователя")
public class UserAdd implements Runnable {

    @CommandLine.ParentCommand
    private UserGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Mixin
    private UserGroup.LoginParameters loginParameters;

    /**
     * Регистрация нового пользователя
     */
    @Override
    public void run() {
        validate();

        var userDataSource = parent.getDictionary().getDataSource(User.class);
        var foundUser = userDataSource.getAll().stream()
                .filter(u -> u.getLogin().equals(parent.getLogin()))
                .findAny();
        if (foundUser.isPresent()) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Пользователь с таким логином уже существует");
        }

        var user = new User();
        user.setId(userDataSource.getNextId());
        user.setLogin(parent.getLogin());
        // Сам пароль не хранится, сохраняется только его хэш с солью, и сравнивается именно хэш
        user.setPassword(parent.encryptPassword(parent.getPassword()));
        user.setAmount(BigDecimal.ZERO);
        userDataSource.add(user.getId(), user);

        parent.getContext().setUser(user);
        parent.getContext().setSignUpTime(new Date());
        parent.getContext().setSignUp(true);

        parent.getPrinter().info("Пользователь " + user.getLogin() + " зарегистрирован");
        parent.getPrinter().info();
    }

    /**
     * Проверка введенного логина пользователя
     */
    private void validate() {
        if (StringUtils.isBlank(parent.getLogin())) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Логин не может быть пустым");
        }

        if (StringUtils.isBlank(parent.getPassword())) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Пароль не может быть пустым");
        }

        // Если не соблюдать это требование, то такой логин сломает сохранение
        // и при чтении такого пользователя из файла его логин будет обрезан по этому символу
        if (StringUtils.contains(parent.getLogin(), DELIMITER)) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Логин не может содержать символ " + DELIMITER);
        }

        // Если не соблюдать это требование, то такой логин тоже сломает сохранение и последующее чтение
        if (StringUtils.contains(parent.getLogin(), StringUtils.SPACE)) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Логин не может содержать символ пробела");
        }
    }
}
