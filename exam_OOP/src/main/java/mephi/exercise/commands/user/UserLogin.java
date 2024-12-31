package mephi.exercise.commands.user;

import lombok.NoArgsConstructor;
import mephi.exercise.entity.User;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.Date;

/**
 * Команда авторизации пользователя
 * Например: {@code login fbd14e52-14e7-4e95-9cc3-ff14a70d638e} - будет авторизован пользователь
 * с идентификатором fbd14e52-14e7-4e95-9cc3-ff14a70d638e
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

    @Override
    public void run() {
        var userDataSource = parent.getDictionary().getDataSource(User.class);
        var user = userDataSource.getAll().stream()
                .filter(u -> u.getLogin().equals(parent.getLogin()))
                .findAny()
                .orElseThrow(() -> new CommandLine.ParameterException(spec.commandLine(),
                        "Пользователь с таким логином не найден"));

        if (!StringUtils.equals(user.getPassword(), parent.encryptPassword(parent.getPassword()))) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Пароль пользователя не правильный");
        }

        parent.getContext().setUser(user);
        parent.getContext().setSignUpTime(new Date());
        parent.getContext().setSignUp(true);
        parent.getPrinter().info("Пользователь " + user.getLogin() + " авторизован");
    }
}
