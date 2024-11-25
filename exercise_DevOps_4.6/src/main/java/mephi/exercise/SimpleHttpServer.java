package mephi.exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Простой HTTP-сервер
 *
 * @author Greben
 */
public class SimpleHttpServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Старт сервера");

            while (true) {
                // ожидаем подключения
                Socket socket = serverSocket.accept();
                System.out.println("Соединение");

                // для подключившегося клиента открываем потоки чтения и записи
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                     PrintWriter output = new PrintWriter(socket.getOutputStream())) {

                    // ждем первой строки запроса
                    while (!input.ready()) ;

                    // отправляем ответ
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html; charset=utf-8");
                    output.println();
                    output.println("<h1>4.6. Практическое задание Docker</h1>");
                    output.println("<h1>Студент Гребень В.Н.</h1>");
                    output.flush();

                    System.out.println("Запрос обработан");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}