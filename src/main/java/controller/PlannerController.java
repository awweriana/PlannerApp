package controller;

import model.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlannerController {
    private List<Task> tasks;

    public PlannerController() {// создаем лист
        tasks = new ArrayList<>();
    }
    public void addTask(Task task) {// добавление задач
        tasks.add(task);
    }
    public void removeTask(Task task) {// удаление
        tasks.remove(task);
    }
    public List<Task> getTasks() {// получаем доступ к tasks
        return tasks;
    }
    public ArrayList<Task> getTaskForDay(LocalDate day) {// получаем задачи на день
        ArrayList<Task> tasksForDay = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDate().equals(day)) {
                tasksForDay.add(task);
                System.out.println(task);
            }
        }
        return tasksForDay;
    }
    public ArrayList<Task> getTaskForWeek(LocalDate week) {// получаем задачи на неделю
        ArrayList<Task> tasksForWeek = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDate().equals(week)) {
                tasksForWeek.add(task);
            }
        }
        return tasksForWeek;
    }
}
