import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    Managers managers = new Managers();
    @Test
    void getDefault() {
        ArrayList<TaskManager> taskManagers = new ArrayList<>();
        taskManagers.add(managers.getDefault());
        assertNotNull(taskManagers);
    }

    @Test
    void getDefaultHistory() {
        ArrayList<HistoryManager> historyManagers = new ArrayList<>();
        historyManagers.add(managers.getDefaultHistory());
        assertNotNull(historyManagers);
    }
}