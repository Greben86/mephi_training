package mephi.exercise.service;

import lombok.RequiredArgsConstructor;
import mephi.exercise.commands.Command;
import mephi.exercise.commands.category.CategoryGroup;
import mephi.exercise.commands.user.UserGroup;
import mephi.exercise.commands.wallet.WalletGroup;
import mephi.exercise.commands.Exit;
import mephi.exercise.commands.Help;
import mephi.exercise.entity.Context;
import mephi.exercise.repository.DataSourceDictionary;
import org.apache.commons.lang3.tuple.Pair;
import picocli.CommandLine;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static mephi.exercise.commands.Command.EMPTY;
import static mephi.exercise.commands.Command.UNKNOWN;

/**
 * Класс обработчик команд пользователя
 */
@CommandLine.Command
@RequiredArgsConstructor
public class CommandProcessor implements Callable<Integer> {

    private final InputStream input;
    private final DataSourceDictionary dictionary;
    private final Printer printer;

    /**
     * Метод реализует главное меню
     *
     * @param scanner чтение вводимых данных
     * @param context контекст выполнения
     */
    private void processWithoutUser(Scanner scanner, Context context) {
        if (context.isSignUp() || context.isExitOnly()) {
            return;
        }

        printer.info("__________________________________________________");
        printer.info("|                     МЕНЮ                       |");
        printer.info("__________________________________________________");
        printer.info("|user    | Управление пользователями             |");
        printer.info("| ├-add  | Регистрация нового пользователя       |");
        printer.info("| ├-login| Войти по паролю пользователя          |");
        printer.info("| └-list | Показать всех пользователей           |");
        printer.info("|exit    | Закрыть приложение                    |");
        printer.info("|help    | Помощь                                |");
        printer.info("__________________________________________________");
        while (!context.isSignUp() && !context.isExitOnly()) {
            try {
                Thread.sleep(100);
                printer.print("Введите команду: ");
                var line = scanner.nextLine();
                var command = parseCommand(line);
                switch (command.getKey()) {
                    case USER -> new CommandLine(new UserGroup(dictionary, context, printer)).execute(command.getValue());
                    case EXIT -> new CommandLine(new Exit(context, printer)).execute(command.getValue());
                    case HELP -> new CommandLine(new Help(context, printer)).execute(command.getValue());
                    case EMPTY -> printer.error("Пустая команда");
                    case UNKNOWN -> printer.error("Неизвестная команда " + line);
                    default -> printer.error("Команда \"" + command.getKey() + "\" не поддерживается в этом меню");
                }
            } catch (Exception ex) {
                printer.error(ex.getMessage());
            }
        }
    }

    /**
     * Метод реализует процесс обработки вводимых команд авторизованного пользователя
     *
     * @param scanner чтение вводимых данных
     * @param context контекст выполнения
     */
    private void processUser(Scanner scanner, Context context) {
        if (!context.isSignUp() || context.isExitOnly()) {
            return;
        }

        printer.info("___________________________________________________");
        printer.info("|                      МЕНЮ                       |");
        printer.info("___________________________________________________");
        printer.info("|wallet   | Управление кошельком                  |");
        printer.info("| ├-push  | Положить деньги в кошелек             |");
        printer.info("| ├-pull  | Взять деньги из кошелька              |");
        printer.info("| ├-send  | Отправить деньги другому пользователю |");
        printer.info("| ├-squash| Свернуть все операции в одну          |");
        printer.info("| └-info  | Показать отчет по кошельку            |");
        printer.info("|category | Управление категориями расходов       |");
        printer.info("| ├-add   | Добавить новую категорию              |");
        printer.info("| ├-edit  | Редактировать категорию               |");
        printer.info("| ├-remove| Удалить категорию                     |");
        printer.info("| └-list  | Показать все категории                |");
        printer.info("|exit     | Выйти из сессии пользователя          |");
        printer.info("|help     | Помощь                                |");
        printer.info("___________________________________________________");
        while (context.isSignUp() && !context.isExitOnly()) {
            try {
                Thread.sleep(100);
                printer.print("Введите команду (используйте help чтобы посмотреть список команд): ");
                var line = scanner.nextLine();
                var command = parseCommand(line);
                switch (command.getKey()) {
                    case WALLET -> new CommandLine(new WalletGroup(dictionary, context, printer)).execute(command.getValue());
                    case CATEGORY -> new CommandLine(new CategoryGroup(dictionary, context, printer)).execute(command.getValue());
                    case EXIT -> new CommandLine(new Exit(context, printer)).execute(command.getValue());
                    case HELP -> new CommandLine(new Help(context, printer)).execute(command.getValue());
                    case EMPTY -> printer.error("Пустая команда");
                    case UNKNOWN -> printer.error("Неизвестная команда " + line);
                    default -> printer.error("Команда \"" + command.getKey() + "\" не поддерживается в этом меню");
                }
            } catch (Exception ex) {
                printer.error(ex.getMessage());
            }
        }
    }

    /**
     * Парсинг введенной команды, разрезаем ввод на массив по пробелам и первый элемент - это команда, а остальное аргументы
     *
     * @param line введенная команда
     * @return пара: команда и аргументы
     */
    private Pair<Command, String[]> parseCommand(String line) {
        if (line == null || line.isBlank()) {
            return Pair.of(EMPTY, null);
        }

        var lineAsArray = line.split(" ");
        var command = Command.getByName(lineAsArray[0]).orElse(UNKNOWN);
        var args = Arrays.copyOfRange(lineAsArray, 1, lineAsArray.length);
        return Pair.of(command, args);
    }

    /**
     * Старт обработчика
     *
     * @return 0 - завершено без ошибок, -1 - были ошибки
     */
    @Override
    public Integer call() {
        var context = new Context();

        printer.info();
        try (var scanner = new Scanner(input)) {
            while (!context.isExitOnly()) {
                // Процесс без пользователя
                processWithoutUser(scanner, context);
                // Процесс с авторизованным пользователем
                processUser(scanner, context);
            }

            return 0;
        } catch (Exception e) {
            printer.error(e.getMessage());
            return -1;
        }
    }
}
