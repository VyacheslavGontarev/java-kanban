import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void taskEquals() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Task task0 = new Task("Тестовая задача 0", "id 0", Status.NEW,
                LocalDateTime.parse("04.02.2025 03:48", formatter), Duration.ofMinutes(30));
        task0.setId(0);
        Task task1 = new Task("Тестовая задача 1", "id 0", Status.NEW,
                LocalDateTime.parse("04.02.2025 04:48", formatter), Duration.ofMinutes(30));
        task1.setId(0);
        assertEquals(task0, task1);
    }
}