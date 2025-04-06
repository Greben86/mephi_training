package mephi.exercise.commands.user;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.User;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.Date;

/**
 * Команда авторизации пользователя
 * Например: {@code login Viktor} - будет запрошен пароль нового пользователя и вход в сессию пользователя Viktor
 */
@NoArgsConstructor
@CommandLine.Command(name = "login", description = "Авторизация пользователя по логину и паролю")
public class UserLogin implements Runnable {

    @CommandLine.ParentCommand
    private UserGroup parent;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Mixin
    private UserGroup.LoginParameters loginParameters;

    /**
     * Вход в сессию пользователя по логину и паролю
     */
    @Override
    public void run() {
        var userDataSource = parent.getDictionary().getDataSource(User.class);
        var user = userDataSource.getAll().stream()
                .filter(u -> u.getLogin().equals(parent.getLogin()))
                .findAny()
                .orElseThrow(() -> new CommandLine.ParameterException(spec.commandLine(),
                        "Пользователь с таким логином не найден"));

        // Сам пароль не хранится, сохраняется только его хэш с солью, и сравнивается именно хэш
        if (!StringUtils.equals(user.getPassword(), parent.encryptPassword(parent.getPassword()))) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Пароль пользователя не правильный");
        }

        parent.getContext().setUser(user);
        parent.getContext().setSignUpTime(new Date());
        parent.getContext().setSignUp(true);
        parent.getPrinter().info("Пользователь " + user.getLogin() + " авторизован");
        parent.getPrinter().info();
    }
}
