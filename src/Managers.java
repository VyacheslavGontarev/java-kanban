public class Managers {

    public TaskManager getDefault() {
       TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    static InMemoryHistoryManager getDefaultHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
