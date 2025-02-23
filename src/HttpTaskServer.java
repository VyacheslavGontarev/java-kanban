import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;
    private TaskManager taskManager;

    public HttpTaskServer (TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    public static void main(String[] args) throws IOException {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Task task = new Task("Сходить в магазин", "Пятёрочка топ", Status.NEW,
                LocalDateTime.parse("04.02.2025 01:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task);
        Task task1 = new Task("Получить баллы на карту x5 клуба", "Карту нужно активировать", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createTask(task1);
        Epic epic = new Epic("Купить арбуз", "Нужен самый сладкий", Status.NEW, null, Duration.ofMinutes(0));
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Понюхать хвостик", "Будет вкусно пахнуть", 2, Status.DONE,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        taskManager.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Постучать по арбузу", "Должен глухо звучать", 2, Status.DONE,
                LocalDateTime.parse("05.02.2025 02:48", formatter), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Спросить совет продавца", "Нужно чтоб сказал ДА СПЕЛЫЙ ОН",
                2, Status.DONE, LocalDateTime.parse("05.02.2025 03:48", formatter), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask2);
        Epic epic1 = new Epic("Купить молоко", "Нужно свежее", Status.NEW, null, Duration.ofMinutes(0));
        taskManager.createEpic(epic1);
        taskManager.getAllTasks();
        taskManager.getEpicByID(2);
        taskManager.getSubTaskByID(3);
        start(taskManager);
    }

    public static void start(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static void stop() {
        httpServer.stop(1);
        System.out.println("Shutdown...");
    }
}
