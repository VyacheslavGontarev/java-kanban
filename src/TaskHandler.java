import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    protected TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":
                if (pathParts.length < 3) {
                    getAllTasks(exchange);
                    break;
                } else {
                    getTaskByID(exchange, pathParts[2]);
                    break;
                }
            case "POST":
                if (pathParts.length < 3) {
                    createTask(exchange);
                    break;
                } else {
                    updateTask(exchange);
                    break;
                }
            case "DELETE":
                if (pathParts.length == 3) {
                    deleteTask(exchange, pathParts[2]);
                    break;
                } else {
                    exchange.sendResponseHeaders(400, 0);
                    sendText(exchange, "Invalid task ID");
                }
        }
    }

    private void getAllTasks(HttpExchange exchange) throws IOException {
        String tasks = gson.toJson(taskManager.getAllTasks());
        exchange.sendResponseHeaders(200, 0);
        sendText(exchange, tasks);
    }

    private void getTaskByID(HttpExchange exchange, String taskId) throws IOException {
        try {
            Task task = taskManager.getTaskByID(Integer.parseInt(taskId));
            String response = gson.toJson(task);
            exchange.sendResponseHeaders(200, 0);
            sendText(exchange, response);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

    private void createTask(HttpExchange exchange) throws IOException {
        String requestBody;
        requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        if (requestBody.isEmpty()) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Empty request body");
            return;
        }
        try {
            Task task = gson.fromJson(requestBody, Task.class);
            if (!taskManager.timeValidator(task)) {
                taskManager.createTask(task);
                exchange.sendResponseHeaders(201, 0);
                sendText(exchange, "Creation request sent");
            } else {
                sendHasInteractions(exchange);
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Invalid request body");
        }
    }

    private void updateTask(HttpExchange exchange) throws IOException {
        String requestBody;
        requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        if (requestBody.isEmpty()) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Empty request body");
            return;
        }
        try {
            Task task = gson.fromJson(requestBody, Task.class);
            taskManager.updateTask(task);
            exchange.sendResponseHeaders(201, 0);
            sendText(exchange, "Update request sent");
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, 0);
            sendText(exchange, "Invalid request body");
        }
    }


    private void deleteTask(HttpExchange exchange, String taskId) throws IOException {
        try {
            Task task = taskManager.getTaskByID(Integer.parseInt(taskId));
            taskManager.delOneTask(Integer.parseInt(taskId));
            exchange.sendResponseHeaders(200, 0);
            sendText(exchange, "Task deleted");
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }
}
