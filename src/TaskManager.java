import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {

    static int id = 0;

    @Override
    public String toString() {
        return "TaskManager{" +
                "task=" + tasks +
                '}';
    }

    static HashMap<Integer, Task> tasks = new HashMap<>();
    static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    static HashMap<Integer, Epic> epics = new HashMap<>();
    Scanner scanner = new Scanner(System.in);

    public String addTask() {
        System.out.println("Введите тип задачи (task, subtask, epic): ");
        String command = scanner.nextLine();
        switch (command) {
            case ("task"):
                return createTask();
            case ("epic"):
                return createEpic();
            case ("subtask"):
                return createSubtask();
            default:
                return "Введён неверный тип!";
        }
    }

    public String createTask() {
        System.out.println("Введите название задачи: ");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи: ");
        String description = scanner.nextLine();
        Task quest = new Task(name, description, id);
        tasks.put(id, quest);
        id += 1;
        return "Задача добавлена!";
    }

    public String createEpic() {
        System.out.println("Введите название эпика: ");
        String name = scanner.nextLine();
        System.out.println("Введите описание эпика: ");
        String description = scanner.nextLine();
        Epic quest = new Epic(name, description, id);
        epics.put(id, quest);
        id += 1;
        return "Задача добавлена!";
    }

    public String createSubtask() {
        System.out.println("Введите название эпика: ");
        String scan = scanner.nextLine();
        for (int i : epics.keySet()) {
            if (epics.get(i).getName().equals(scan)) { // не понятно корректно ли работает поиск null проверка нужна

                System.out.println("Введите название подзадачи: ");
                String name = scanner.nextLine();
                System.out.println("Введите описание подзадачи: ");
                String description = scanner.nextLine();
                Subtask quest = new Subtask(name, description, id, i);
                Epic.putSubtask(id, quest);
                subtasks.put(id, quest);
                id += 1;
                return "Подзадача успешно создана";
            }
        }
        return "Такого эпика не существует!";
    }

    public String printTask() {
        System.out.println("Введите тип задачи (task, subtask, epic): ");
        String command = scanner.nextLine();
        switch (command) {
            case ("task"):
                return printAllTasks();
            case ("epic"):
                return printAllEpics();
            case ("subtask"):
                return printAllSubtasks();
            default:
                return "Введён неверный тип!";
        }
    }

    public String printAllTasks() {
        String result = "";
        if (!tasks.isEmpty()) {
            result = result + tasks;
        } else {
            result = "Задачи отсутствуют!";
        }
        return result;
    }

    public String printAllEpics() {
        String result = "";
        if (!epics.isEmpty()) {
            result = result + epics;
        } else {
            result = "Задачи отсутствуют!";
        }
        return result;
    }

    public String printAllSubtasks() {
        String result = "";
        if (!subtasks.isEmpty()) {
            result = result + subtasks;
        } else {
            result = "Задачи отсутствуют!";
        }
        return result;
    }

    public String deleteTasks() {
        System.out.println("Введите тип задачи (task, subtask, epic): ");
        String command = scanner.nextLine();
        switch (command) {
            case ("task"):
                return deleteAllTasks();
            case ("epic"):
                deleteAllSubtasks();
                return deleteAllEpics();
            case ("subtask"):
                return deleteAllSubtasks();
            default:
                return "Введён неверный тип!";
        }
    }

    public String deleteAllTasks() {
        tasks.clear();
        return "[данные удалены]";
    }

    public String deleteAllSubtasks() {
        subtasks.clear();
        return "[данные удалены]";
    }

    public String deleteAllEpics() {
        epics.clear();
        return "[данные удалены]";
    }

    public String getTask() {
        System.out.println("Введите тип задачи (task, subtask, epic): ");
        String command = scanner.nextLine();
        System.out.println("Введите название задачи: ");
        String name = scanner.nextLine();
        switch (command) {
            case ("task"):
                return getTaskByName(name);
            case ("epic"):
                return getEpicByName(name);
            case ("subtask"):
                return getSubTaskByName(name);
            default:
                return "Введён неверный тип!";
        }
    }

    public String getTaskByName(String name) {
        String result = "Задачи отсутствуют!";
        if (!tasks.isEmpty()) {
            for (Task val : tasks.values()) {
                if (val.getName().equals(name)) {
                    result = val.toString();
                } else {
                    result = "Задачи с таким названием не существует";
                }
            }
        }
        return result;
    }

    public String getSubTaskByName(String name) {
        String result = "Подзадачи отсутствуют!";
        if (!subtasks.isEmpty()) {
            for (Subtask val : subtasks.values())
                if (val.getName().equals(name)) {
                    result = val.toString();
                } else {
                    result = "Подзадачи с таким названием не существует";
                }
        }
        return result;
    }

    public String getEpicByName(String name) {
        String result = "Эпиков нет!";
        if (!tasks.isEmpty()) {
            for (Epic val : epics.values())
                if (val.getName().equals(name)) {
                    result = val.toString();
                } else {
                    result = "Задачи с таким названием не существует";
                }
        }
        return result;
    }

    public String changeTask() {
        System.out.println("Выберите тип задачи (task, subtask): ");
        String command = scanner.nextLine();
        System.out.println("Введите имя задачи: ");
        String name = scanner.nextLine();
        switch (command) {
            case ("task"):
                for (int key : tasks.keySet()) {
                    if (tasks.get(key).getName().equals(name)) {
                        String description = tasks.get(key).getDescription();
                        Status status = tasks.get(key).getStatus();
                        Task task = new Task(name, description, key);
                        if (status == Status.NEW) {
                            task.setStatus(Status.IN_PROGRESS);
                        } else if (status == Status.IN_PROGRESS) {
                            task.setStatus(Status.DONE);
                        }
                        tasks.put(key, task);
                        return "Статус задачи изменён";
                    }
                }
                return "Задача с таким названием отсутствует";
            case ("subtask"):
                for (int key : subtasks.keySet()) {
                    if (subtasks.get(key).getName().equals(name)) {
                        String description = subtasks.get(key).getDescription();
                        int epicId = subtasks.get(key).getEpicId();
                        Status status = subtasks.get(key).getStatus();
                        Subtask subtask = new Subtask(name, description, key, epicId);
                        if (status == Status.NEW) {
                            subtask.setStatus(Status.IN_PROGRESS);
                        } else if (status == Status.IN_PROGRESS) {
                            subtask.setStatus(Status.DONE);
                        }
                        epics.get(epicId).status = Status.IN_PROGRESS;
                        subtasks.put(key, subtask);
                        epics.get(epicId).putSubtask(key, subtask);
                        epics.get(epicId).changeEpicStatusDone();
                        return "Статус задачи изменён";
                    }
                }
            default:
                return "Неверный тип задачи!";
        }
    }
    public String deleteOneTask(){
        System.out.println("Введите тип задачи (task, subtask, epic): ");
        String command = scanner.nextLine();
        System.out.println("Введите название задачи: ");
        String name = scanner.nextLine();
        switch (command) {
            case ("task"):
                return delOneTask(name);
            case ("epic"):
                return delOneEpic(name);
            case ("subtask"):
                return delOneSubtask(name);
            default:
                return "Введён неверный тип!";
        }
    }

    public String delOneTask(String name){
        for (int key : tasks.keySet()) {
            if (tasks.get(key).getName().equals(name)){
                tasks.remove(key);
                return "Задача удалена";
            }
        }
        return "Задачи с таким названием нет";
    }
    public String delOneEpic(String name){
        for (int key : epics.keySet()) {
            if (epics.get(key).getName().equals(name)){
                epics.remove(key);
                epics.get(key).deleteAllSubtasks();
                return "Задача удалена";
            }
        }
        return "Задачи с таким названием нет";
    }
    public String delOneSubtask(String name){
        for (int key : subtasks.keySet()) {
            if (subtasks.get(key).getName().equals(name)){
                int epicId = subtasks.get(key).epicId;
                subtasks.remove(key);
                epics.get(epicId).removeSubtask(key);
                if (epics.get(epicId).subtaskIsEmpty()){
                    epics.get(epicId).setStatus(Status.DONE);
                }
                return "Задача удалена";
            }
        }

        return "Задачи с таким названием нет";
    }
}

