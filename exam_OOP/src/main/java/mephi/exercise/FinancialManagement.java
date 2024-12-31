package mephi.exercise;

import mephi.exercise.commands.converters.FileConverter;
import mephi.exercise.entity.Category;
import mephi.exercise.entity.Operation;
import mephi.exercise.entity.User;
import mephi.exercise.repository.Impl.CategoryDataSource;
import mephi.exercise.repository.Impl.CategoryEntityMapper;
import mephi.exercise.repository.Impl.DataConnectorImpl;
import mephi.exercise.repository.Impl.DataSourceDictionaryImpl;
import mephi.exercise.repository.Impl.OperationDataSource;
import mephi.exercise.repository.Impl.OperationEntityMapper;
import mephi.exercise.repository.Impl.UserDataSource;
import mephi.exercise.repository.Impl.UserEntityMapper;
import mephi.exercise.service.CommandProcessor;
import mephi.exercise.service.Printer;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Простая система управления личными финансами
 * Класс имплементирует интерфейс {@link Callable} чтобы реализовать ожидание завершения выполнения потока командного процессора
 */
@CommandLine.Command
public class FinancialManagement implements Callable<Integer> {

    // Разделитель
    public static final String DELIMITER = ";";

    /**
     * Метод-точка входа в приложение, запускает первую команду, которую реализует этот класс
     *
     * @param args <b>переданные аргументы:</b>
     *             <p>-F, --file - файл хранения данных, в этом фале будут сохранены сущности для последующего чтения</p>
     */
    public static void main(String[] args) {
        new CommandLine(new FinancialManagement()).execute(args);
    }

    @CommandLine.Option(
            names = {"-F", "--file"},
            description = "Файл хранения данных",
            converter = FileConverter.class
    )
    private File file;

    @Override
    public Integer call() {
        var printer = new Printer(System.out, System.err);

        // Чтение файла и загрузка объектов в память
        var dataConnector = new DataConnectorImpl(printer, file);
        dataConnector.loadFile();

        var userEntityMapper = new UserEntityMapper(printer);
        var categoryEntityMapper = new CategoryEntityMapper(printer);
        var operationEntityMapper = new OperationEntityMapper(printer);
        var dictionary = new DataSourceDictionaryImpl();
        dictionary.putDataSource(User.class, new UserDataSource(dataConnector.readEntities(User.class, userEntityMapper)));
        dictionary.putDataSource(Category.class, new CategoryDataSource(dataConnector.readEntities(Category.class, categoryEntityMapper)));
        dictionary.putDataSource(Operation.class, new OperationDataSource(dataConnector.readEntities(Operation.class, operationEntityMapper)));

        // Старт командного процессора
        int result = new CommandLine(new CommandProcessor(System.in, dictionary, printer)).execute();

        // Сохранение данных в файл перед выходом из приложения, если не было ошибок
        if (result == 0) {
            dataConnector.clearFile();
            dataConnector.saveEntities(User.class, userEntityMapper, dictionary);
            dataConnector.saveEntities(Category.class, categoryEntityMapper, dictionary);
            dataConnector.saveEntities(Operation.class, operationEntityMapper, dictionary);
        }

        return result;
    }
}