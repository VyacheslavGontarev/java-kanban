
public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("SavedTasks.csv");
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
        Subtask subtask2 = new Subtask("Спросить совет продавца", "Нужно чтоб сказал ДА СПЕЛЫЙ ОН", 2, Status.NEW);
        taskManager.createSubtask(subtask2);
        Epic epic1 = new Epic("Купить молоко","Нужно свежее", Status.NEW);
        taskManager.createEpic(epic1);

        taskManager.getAllTasks();
        taskManager.getEpicByID(2);
        taskManager.getSubTaskByID(3);
        System.out.println("История:");
        System.out.println(taskManager.getStory());
        taskManager.getEpicByID(2);
        taskManager.getEpicByID(6);
        System.out.println("История:");
        System.out.println(taskManager.getStory());
        taskManager.delOneTask(0);
        System.out.println("История:");
        System.out.println(taskManager.getStory());
        taskManager.delOneEpic(2);
        System.out.println("История:");
        System.out.println(taskManager.getStory());
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
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
        for (Task task : manager.getStory()) {
            System.out.println(task);
        }
    }
}