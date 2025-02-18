import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void subtaskEquals() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Epic epic0 = new Epic("Тестовый эпик 0", "id 0", Status.NEW,
                null, Duration.ofMinutes(0));
        epic0.setId(0);
        Subtask subtask0 = new Subtask("Тестовая подзадача 0", "id 0", 0, Status.NEW,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        subtask0.setId(1);
        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "id 0", 0, Status.NEW,
                LocalDateTime.parse("05.02.2025 01:48", formatter), Duration.ofMinutes(1));
        subtask1.setId(1);
        assertEquals(subtask0, subtask1);
    }


}