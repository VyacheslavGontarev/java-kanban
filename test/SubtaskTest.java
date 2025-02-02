import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void subtaskEquals() {
        Epic epic0 = new Epic("Тестовый эпик 0", "id 0", Status.NEW);
        epic0.setId(0);
        Subtask subtask0 = new Subtask("Тестовая подзадача 0", "id 0", 0, Status.NEW);
        subtask0.setId(1);
        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "id 0", 0, Status.NEW);
        subtask1.setId(1);
        assertEquals(subtask0, subtask1);
    }


}