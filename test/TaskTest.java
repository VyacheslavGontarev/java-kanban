import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void taskEquals() {
        Task task0 = new Task("Тестовая задача 0", "id 0", Status.NEW);
        task0.setId(0);
        Task task1 = new Task("Тестовая задача 1", "id 0", Status.NEW);
        task1.setId(0);
        assertEquals(task0, task1);
    }
}