package mephi.exercise.commands.user;

import lombok.Getter;
import lombok.Setter;
import mephi.exercise.commands.AbstractCommand;
import mephi.exercise.entity.Context;
import mephi.exercise.entity.User;
import mephi.exercise.repository.DataSourceDictionary;
import mephi.exercise.service.Printer;
import org.apache.commons.codec.digest.DigestUtils;
import picocli.CommandLine;

/**
 * Команда регистрации нового пользователя
 * Например: {@code new Viktor} - будет зарегистрирован пользователь с логином Viktor
 * Новому пользователю будет присвоен и выведен уникальный UUID, по нему будет выполняться авторизация пользователя
 */
@Getter
@Setter
@CommandLine.Command(name = "user", description = "Управление пользователем",
        subcommands = {UserAdd.class, UserLogin.class})
public class UserGroup extends AbstractCommand {

    public static final String SALT = "salt";

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    private String login;
    private String password;

    public UserGroup(DataSourceDictionary dictionary, Context context, Printer printer) {
        super(dictionary, context, printer);
    }

    public String encryptPassword(String password) {
        return DigestUtils.md5Hex(SALT + password);
    }

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Эта команда не может быть вызвана без параметров");
    }

    @CommandLine.Command(name = "list", description = "Показать всех пользователей")
    public void list() {
        var userDataSource = dictionary.getDataSource(User.class);
        var users = userDataSource.getAll().stream()
                .toList();
        if (users.isEmpty()) {
            getPrinter().info("Список пуст");
        }

        users.forEach(user ->
                getPrinter().info(user.equals(getContext().getUser()) ? "* " + user : user.toString()));
        getPrinter().info();
    }

    public static class LoginParameters {
        @CommandLine.ParentCommand
        private UserGroup parent;

        @CommandLine.Spec
        private CommandLine.Model.CommandSpec spec;

        @CommandLine.Parameters(
                description = "Логин пользователя",
                paramLabel = "Логин")
        private void setLogin(String login) {
            parent.setLogin(login);
        }

        @CommandLine.Parameters(
                description = "Пароль пользователя",
                prompt = "Пароль: ",
                paramLabel = "Пароль",
                interactive = true)
        private void setPassword(String password) {
            parent.setPassword(password);
        }
    }
}