package mephi.exercise.commands.user;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.User;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.Date;

import static mephi.exercise.Main.DELIMITER;

/**
 * Команда регистрации нового пользователя
 * Например: {@code add Viktor} - будет зарегистрирован пользователь с логином Viktor
 * Новому пользователю будет присвоен и выведен уникальный UUID, по нему будет выполняться авторизация пользователя
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

    @Override
    public void run() {
        validate();

        var userDataSource = parent.getDictionary().getDataSource(User.class);
        var foundUser = userDataSource.getAll().stream()
                .filter(u -> u.getLogin().equals(parent.getLogin()))
                .findAny();
        if (foundUser.isPresent()) {
            throw new IllegalStateException("Пользователь с таким логином уже существует");
        }

        var user = new User();
        user.setId(userDataSource.getNextId());
        user.setLogin(parent.getLogin());
        user.setPassword(parent.encryptPassword(parent.getPassword()));
        user.setAmount(BigDecimal.ZERO);
        userDataSource.add(user.getId(), user);

        parent.getContext().setUser(user);
        parent.getContext().setSignUpTime(new Date());
        parent.getContext().setSignUp(true);

        parent.getPrinter().info("Пользователь " + user.getLogin() + " зарегистрирован, присвоен ID " + user.getId());
        parent.getPrinter().info();
    }

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
