import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    protected EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":
                if (pathParts.length < 3) {
                    getAllEpics(exchange);
                    break;
                } else if (pathParts.length == 3) {
                    getEpicByID(exchange, pathParts[2]);
                    break;
                } else {
                    getEpicSubtasks(exchange, pathParts[2]);
                    break;
                }
            case "POST":
                    createEpic(exchange);
                    break;
            case "DELETE":
                if (pathParts.length == 3) {
                    delOneEpic(exchange, pathParts[2]);
                    break;
                } else {
                    exchange.sendResponseHeaders(400, 0);
                    sendText(exchange, "Invalid epic ID");
                }
        }
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {
        String epics = gson.toJson(taskManager.getAllEpics());
        exchange.sendResponseHeaders(200, 0);
        sendText(exchange, epics);
    }

    private void getEpicByID(HttpExchange exchange, String taskId) throws IOException {
        try {
            Epic epic = taskManager.getEpicByID(Integer.parseInt(taskId));
            String response = gson.toJson(epic);
            exchange.sendResponseHeaders(200, 0);
            sendText(exchange, response);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        String requestBody;
        requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        if (requestBody.isEmpty()) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Empty request body");
            return;
        }
        try {
            Epic epic = gson.fromJson(requestBody, Epic.class);
                taskManager.createEpic(epic);
                exchange.sendResponseHeaders(201, 0);
                sendText(exchange, "Creation request sent");
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Invalid request body");
        }
    }

    private void delOneEpic(HttpExchange exchange, String taskId) throws IOException {
        try {
            Epic epic = taskManager.getEpicByID(Integer.parseInt(taskId));
            taskManager.delOneEpic(Integer.parseInt(taskId));
            exchange.sendResponseHeaders(200, 0);
            sendText(exchange, "Epic deleted");
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    public void getEpicSubtasks(HttpExchange exchange, String taskId) throws IOException {
        try {
            Epic epic = taskManager.getEpicByID(Integer.parseInt(taskId));
            String subtasks = gson.toJson(taskManager.getSubtasksByEpicId(Integer.parseInt(taskId)));
            exchange.sendResponseHeaders(200, 0);
            sendText(exchange, subtasks);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }
}