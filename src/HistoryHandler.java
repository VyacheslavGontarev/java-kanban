import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    protected HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            getHistory(exchange);
        }
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        String tasks = gson.toJson(taskManager.getStory());
        exchange.sendResponseHeaders(200, 0);
        sendText(exchange, tasks);
    }
}