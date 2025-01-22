import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void add() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTaskByID(0);
        final List<Task> history = taskManager.getStory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
    @Test
    void unique(){
        List<Task> list = new ArrayList<>();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task);
        list.add(task);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task1);
        list.add(task1);
        list.add(task);
        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(0);
        final List<Task> history = taskManager.getStory();
        assertNotSame(list, history, "Объекты не равны");
    }
    @Test
    void remove(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task1);
        taskManager.getTaskByID(0);
        taskManager.getTaskByID(1);
        final List<Task> list = taskManager.getStory();
        taskManager.delOneTask(1);
        final List<Task> history = taskManager.getStory();
        assertNotEquals(list.size(), history.size(), "Объекты не равны");
    }
}