import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    protected SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":
                if (pathParts.length < 3) {
                    getAllSubtasks(exchange);
                    break;
                } else {
                    getSubtaskByID(exchange, pathParts[2]);
                    break;
                }
            case "POST":
                if (pathParts.length < 3) {
                    createSubtask(exchange);
                    break;
                } else {
                    updateSubtask(exchange);
                    break;
                }
            case "DELETE":
                if (pathParts.length == 3) {
                    delOneSubtask(exchange, pathParts[2]);
                    break;
                } else {
                    exchange.sendResponseHeaders(400, 0);
                    sendText(exchange, "Invalid subtask ID");
                }
        }
    }

    private void getAllSubtasks(HttpExchange exchange) throws IOException {
        String subtasks = gson.toJson(taskManager.getAllSubtasks());
        exchange.sendResponseHeaders(200, 0);
        sendText(exchange, subtasks);
    }

    private void getSubtaskByID(HttpExchange exchange, String taskId) throws IOException {
        try {
            Subtask subtask = taskManager.getSubTaskByID(Integer.parseInt(taskId));
            String response = gson.toJson(subtask);
            exchange.sendResponseHeaders(200, 0);
            sendText(exchange, response);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void createSubtask(HttpExchange exchange) throws IOException {
        String requestBody;
        requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        if (requestBody.isEmpty()) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Empty request body");
            return;
        }
        try {
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            if (!taskManager.timeValidator(subtask)) {
                taskManager.createSubtask(subtask);
                exchange.sendResponseHeaders(201, 0);
                sendText(exchange, "Creation request sent");
            } else {
                sendHasInteractions(exchange);
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(409, 0);
            sendText(exchange, "Invalid request body");
        }
    }

    private void updateSubtask(HttpExchange exchange) throws IOException {
        String requestBody;
        requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        if (requestBody.isEmpty()) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Empty request body");
            return;
        }
        try {
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            taskManager.updateSubtask(subtask);
            exchange.sendResponseHeaders(201, 0);
            sendText(exchange, "Update request sent");
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Invalid request body");
        }
    }


    private void delOneSubtask(HttpExchange exchange, String taskId) throws IOException {
        try {
            Subtask subtask = taskManager.getSubTaskByID(Integer.parseInt(taskId));
            taskManager.delOneSubtask(Integer.parseInt(taskId));
            exchange.sendResponseHeaders(200, 0);
            sendText(exchange, "Subtask deleted");
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }
}