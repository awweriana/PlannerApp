package service;

import model.Task;
import java.util.List;

public interface PlannerService {
    void savePlannerService(Task task);
    List<Task> loadAllTasks();
    void deleteTask(Task task);
}
