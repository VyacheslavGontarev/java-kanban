import java.util.Scanner;


public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        System.out.println("Поехали!");
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            switch (command) {
                case ("1"):
                    System.out.println(taskManager.addTask());
                    break;
                case ("2"):
                    System.out.println(taskManager.printTask());
                    break;
                case ("3"):
                    System.out.println(taskManager.deleteTasks());
                    break;
                case ("4"):
                    System.out.println(taskManager.getTask());
                    break;
                case ("5"):
                System.out.println(taskManager.changeTask());
                break;
                case ("6"):
                System.out.println(taskManager.deleteOneTask());
                break;
                case ("0"):
                    return;
                default:
                    System.out.println("Такой команды пока что нет:)");
                    break;
            }
        }
    }

    static void printMenu() {

        System.out.println("Выберите команду: ");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Список всех задач");
        System.out.println("3. Удалить все задачи");
        System.out.println("4. Поиск задачи по имени");
        System.out.println("5. Обновить статус задачи");
        System.out.println("6. Удалить задачу");
        System.out.println("0. Выход");

    }


}

