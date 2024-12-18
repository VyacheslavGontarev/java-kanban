import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryHistoryManagerTest {
InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void add() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}