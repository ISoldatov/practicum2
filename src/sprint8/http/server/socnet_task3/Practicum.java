package sprint8.http.server.socnet_task3;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Practicum {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    private static final List<Post> posts = new ArrayList<>();

    static {
        Post post1 = new Post(1, "Это первый пост, который я здесь написал.");
        post1.addComment(new Comment("Пётр Первый", "Я успел откомментировать первым!"));
        posts.add(post1);

        Post post2 = new Post(22, "Это будет второй пост. Тоже короткий.");
        posts.add(post2);

        Post post3 = new Post(333, "Это пока последний пост.");
        posts.add(post3);
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/posts", new PostsHandler());
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class PostsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

            switch (endpoint) {
                case GET_POSTS: {
                    handleGetPosts(exchange);
                    break;
                }
                case GET_COMMENTS: {
                    handleGetComments(exchange);
                    break;
                }
                case POST_COMMENT: {
                    handlePostComments(exchange);
                    break;
                }
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private void handlePostComments(HttpExchange exchange) throws IOException {
            Optional<Integer> postIdOpt = getPostId(exchange);

            if (postIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор поста", 400);
                return;
            }

            Optional<Post> postOpt = posts.stream().filter(p -> postIdOpt.get() == p.getId()).findFirst();

            if (postOpt.isEmpty()) {
                writeResponse(exchange, "Пост с идентификатором " + postIdOpt.get() + " не найден", 404);
                return;
            }

            String jsonComment = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

            try {
                Comment comment = gson.fromJson(jsonComment, Comment.class);

                if (comment.getUser()==null || comment.getText() == null
                        || comment.getUser().isBlank() || comment.getText().isBlank()) {
                    writeResponse(exchange, "Поля комментария не могут быть пустыми", 400);
                    return;
                }
                postOpt.get().addComment(comment);
                writeResponse(exchange, "Комментарий добавлен", 201);

            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod) {
            String[] pathParts = requestPath.split("/");

            if (pathParts.length == 2 && pathParts[1].equals("posts")) {
                return Endpoint.GET_POSTS;
            }
            if (pathParts.length == 4 && pathParts[1].equals("posts") && pathParts[3].equals("comments")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_COMMENTS;
                }
                if (requestMethod.equals("POST")) {
                    return Endpoint.POST_COMMENT;
                }
            }
            return Endpoint.UNKNOWN;
        }

        private void handleGetPosts(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(posts), 200);
        }

        private void handleGetComments(HttpExchange exchange) throws IOException {
            Optional<Integer> postIdOpt = getPostId(exchange);
            /* Верните комментарии к указанному посту. Код ответа должен быть 200.
            Если запрос был составлен неверно, верните сообщение об ошибке. */
            if (postIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор поста", 400);
                return;
            }
            Optional<Post> post = posts.stream().filter(p -> postIdOpt.get() == p.getId()).findFirst();
            if (post.isEmpty()) {
                writeResponse(exchange, "Пост с идентификатором " + postIdOpt.get() + " не найден", 404);
                return;
            }
            String jsonComments = gson.toJson(post.get().getCommentaries());
            writeResponse(exchange, jsonComments, 200);
        }

        private Optional<Integer> getPostId(HttpExchange exchange) {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            try {
                return Optional.of(Integer.parseInt(pathParts[2]));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }

        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        enum Endpoint {GET_POSTS, GET_COMMENTS, POST_COMMENT, UNKNOWN}
    }
}