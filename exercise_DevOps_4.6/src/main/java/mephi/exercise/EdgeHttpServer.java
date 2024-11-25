package mephi.exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Простой зеркалирующий HTTP-сервер
 */
public class EdgeHttpServer {

    // Порт по умолчанию
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length == 1) {
            port = parsePort(args[0]);
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started!");

            while (true) {
                // ожидаем подключения
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                // для подключившегося клиента открываем потоки
                // чтения и записи
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream())) {

                    // ждем первой строки запроса
                    while (!input.ready()) ;

                    // считываем все что было отправлено клиентом
                    List<String> buffer = new ArrayList<>();
                    while (input.ready()) {
                        buffer.add(input.readLine());
                    }

                    // отправляем зеркальный ответ
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html; charset=utf-8");
                    output.println();
                    for (String line : buffer) {
                        output.println("<p>"+line+"</p>");
                    }
                    output.flush();

                    // по окончанию выполнения блока try-with-resources потоки,
                    // а вместе с ними и соединение будут закрыты
                    System.out.println("Client disconnected!");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private static int parsePort(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            ex.printStackTrace(System.err);
            return DEFAULT_PORT;
        }
    }
}