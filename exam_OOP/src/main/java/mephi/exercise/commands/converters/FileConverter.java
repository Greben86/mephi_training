package mephi.exercise.commands.converters;

import picocli.CommandLine;

import java.io.File;

/**
 * Класс-конвертер введенного полного имени файла в объект файла, если это файл и он существует
 */
public class FileConverter implements CommandLine.ITypeConverter<File> {

    /**
     * Конвертер введенного полного имени файла в объект файла
     *
     * @param s аргумент командной строки, содержащий полное имя файла
     * @return объект файла, если это файл и он существует, иначе null
     */
    @Override
    public File convert(String s) {
        var file = new File(s);
        if (file.exists() && file.isFile()) {
            return file;
        }

        return null;
    }
}
