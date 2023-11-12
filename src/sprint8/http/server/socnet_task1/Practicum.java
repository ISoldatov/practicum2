package sprint8.http.server.socnet_task1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Practicum {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {

        // добавьте код для конфигурирования и запуска сервера
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/posts", new PostsHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class PostsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            // получите информацию об эндпоинте, к которому был запрос
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

            switch (endpoint) {
                case GET_POSTS: {
                    writeResponse(exchange, "Получен запрос на получение постов", 200);
                    break;
                }
                case GET_COMMENTS: {
                    writeResponse(exchange, "Получен запрос на получение комментариев", 200);
                    break;
                }
                case POST_COMMENT: {
                    writeResponse(exchange, "Получен запрос на добавление комментария", 200);
                    break;
                }
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod) {
            String[] pathItems = requestPath.split("/");
            if (requestMethod.equals("GET") && pathItems.length == 2 && pathItems[1].equals("posts")) {
                return Endpoint.GET_POSTS;
            }
            if (requestMethod.equals("GET") && pathItems.length == 4 && pathItems[3].equals("comments")) {
                return Endpoint.GET_COMMENTS;
            }
            if (requestMethod.equals("POST") && pathItems.length == 4 && pathItems[3].equals("comments")) {
                return Endpoint.POST_COMMENT;
            }
            return Endpoint.UNKNOWN;
        }

        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            /* Реализуйте отправку ответа, который содержит responseString в качестве тела ответа
            и responseCode в качестве кода ответа.
            Учтите, что если responseString — пустая строка, то её не нужно передавать в ответе. */
            exchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseString.getBytes());
            }
        }

        enum Endpoint {GET_POSTS, GET_COMMENTS, POST_COMMENT, UNKNOWN}
    }
}


