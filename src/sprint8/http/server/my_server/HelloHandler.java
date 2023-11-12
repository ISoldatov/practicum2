package sprint8.http.server.my_server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HelloHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /hello запроса от клиента.");

//      Получение тела запроса
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        System.out.println("Тело запроса:\n" + body);

//      Получение метода запроса
        String method = httpExchange.getRequestMethod();

        String response;
        switch(method) {
            case "POST":
                response = "Вы использовали метод POST!";
                break;
            case "GET":
                response = "Вы использовали метод GET!";
                break;
            default:
                response = "Вы использовали какой-то другой метод!";
        }

//      Получение заголовков запроса
        Headers requestHeaders = httpExchange.getRequestHeaders();
        System.out.println("Заголовки запроса: " + requestHeaders.entrySet());

//      Получение параметров пути запроса (под индексом 0 - пустая строка)
        String path = httpExchange.getRequestURI().getPath();

        String name = path.split("/")[2];
        System.out.println("Имя: " + name);

//      Свои заголовки в ответе
        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        // можно создать свои собственные заголовки
        responseHeaders.set("X-your-own-header", "any-information-you-want");
        responseHeaders.set("X-your-own-header-2", "any-information-you-want-2");

        StringBuilder response2 = new StringBuilder("Привет! Рады видеть на нашем сервере. ")
                .append(response)
                .append("Привет, " + name + "!");



        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response2.toString().getBytes());
        }
    }
}


