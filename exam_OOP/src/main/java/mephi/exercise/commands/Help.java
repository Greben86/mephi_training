package mephi.exercise.commands;

import lombok.RequiredArgsConstructor;
import mephi.exercise.entity.Context;
import mephi.exercise.service.Printer;
import picocli.CommandLine;

/**
 * Команда вывода в консоль всех доступных команд
 * Например: {@code help} - будут показаны все доступные команды в виде списка
 */
@RequiredArgsConstructor
@CommandLine.Command(name = "help", description = "Показать все доступные команды")
public class Help implements Runnable {

    private final Context context;
    private final Printer printer;

    @Override
    public void run() {
        if (context.isSignUp()) {
            printer.info("wallet    - Управление кошельком");
            printer.info(" ├-push   - Положить деньги в кошелек, например \"wallet push 500\" - будет выполнена операция добавления 500 в кошелек");
            printer.info(" ├-pull   - Взять деньги из кошелька, например \"wallet pull 500\" - будет выполнена операция изъятия 500 из кошелька");
            printer.info(" ├-send   - Отправить деньги другому пользователю, например \"wallet send Viktor 500\" - пользователю Viktor будет отправлен перевод 500");
            printer.info(" ├-squash - Свернуть все операции в одну, например \"wallet squash\" - все операции будут свернуты в одну, история операций будет утеряна");
            printer.info(" └-info   - Показать отчет по кошельку, например \"wallet info\" - будет выведен отчет по кошельку");
            printer.info("category  - Управление категориями расходов");
            printer.info(" ├-add    - Добавить новую категорию, например \"category add на проезд\" - будет добавлена новая категория с названием \"на проезд\"");
            printer.info(" ├-edit   - Редактировать категорию, например \"category edit на проезд\" - будет запрошено новое название для категории \"на проезд\"");
            printer.info(" ├-remove - Удалить категорию, например \"category remove на проезд\" - будет удалена категория с названием \"на проезд\"");
            printer.info(" └-list   - Показать все категории, например \"category list\" - будет показаны все категории пользователя");
        } else {
            printer.info("user - Управление пользователями");
            printer.info(" ├-add - Регистрация нового пользователя, например \"user add Viktor\" - будет запрошен " +
                    "пароль нового пользователя и зарегистрирован пользователь с логином Viktor");
            printer.info(" ├-login - Авторизация пользователя по логину, например " +
                    "\"user login Viktor\" - будет запрошен пароль нового пользователя и вход в сессию пользователя Viktor");
            printer.info(" └-list - Показать всех пользователей, например \"user list\" -> будет выведен список всех пользователей");
        }
        printer.info("help - Показать все доступные команды, например \"help\" -> будут показаны все доступные " +
                "команды в виде списка");
        printer.info("exit - Завершить сессию пользователя или работу приложения, например \"exit\" -> будет " +
                "выполнен выход из сессии пользователя или завершено приложение, если нет активной сессии; " +
                "\"exit -F\" - будет выполнен выход из приложения");
    }
}
