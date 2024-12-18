import java.util.ArrayList;
import java.util.List;
//for commit

public class Main {


    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();
        System.out.println("Поехали!");
        Task task = new Task("Сходить в магазин", "Пятёрочка топ", Status.NEW);
        taskManager.createTask(task);
        Task task1 = new Task("Получить баллы на карту x5 клуба", "Карту нужно активировать", Status.NEW);
        taskManager.createTask(task1);
        Epic epic = new Epic("Купить арбуз", "Нужен самый сладкий", Status.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Понюхать хвостик","Будет вкусно пахнуть", 2, Status.NEW);
        taskManager.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Постучать по арбузу","Должен глухо звучать", 2, Status.NEW);
        taskManager.createSubtask(subtask1);
        Epic epic1 = new Epic("Купить молоко","Нужно свежее", Status.NEW);
        taskManager.createEpic(epic1);
        Subtask subtask2 = new Subtask("Проверить срок годности", "Нужно не старше 3 дней", 5, Status.NEW);
        taskManager.createSubtask(subtask2);
        task = new Task("Сходить в магазин", "Вообще-то Магнит лучше", Status.IN_PROGRESS);
        task.setId(0);
        taskManager.updateTask(task);
        subtask = new Subtask("Посмотреть цвет", "Должен быть зелёным", 2, Status.IN_PROGRESS);
        subtask.setId(3);
        taskManager.updateSubtask(subtask);

        Task task3 = new Task("Проверочная задача 1", "Чисто проверить 1", Status.NEW);
        taskManager.createTask(task3);
        Task task4 = new Task("Проверочная задача 2", "Чисто проверить 2", Status.NEW);
        taskManager.createTask(task4);
        taskManager.delOneTask(1);
        taskManager.delOneEpic(5);
        printAllTasks(taskManager);
        taskManager.deleteAllTasks();
        System.out.println(taskManager.getAllTasks());
    }
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : (ArrayList<Task>) manager.getAllTasks()) {
            System.out.println(manager.getTaskByID(task.getId()));
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(manager.getEpicByID(epic.getId()));

            for (Task task : manager.getSubtasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(manager.getSubTaskByID(subtask.getId()));
        }

        System.out.println("История:");
        for (Task task : (List<Task>) manager.getStory()) {
            System.out.println(task);
        }
    }
}
