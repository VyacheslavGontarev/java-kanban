import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void epicsEquals() {
        ArrayList<Integer> subtasks = new ArrayList<>();
        Epic epic0 = new Epic("Тестовый эпик 0", "id 0", Status.NEW, null, Duration.ofMinutes(0));
        epic0.setId(0);
        epic0.setSubtask(subtasks);
        Epic epic1 = new Epic("Тестовый эпик 1", "id 0", Status.NEW, null, Duration.ofMinutes(0));
        epic1.setId(0);
        epic1.setSubtask(subtasks);
        assertEquals(epic0, epic1);
    }

}