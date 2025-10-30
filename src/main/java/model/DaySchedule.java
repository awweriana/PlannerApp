package model;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class DaySchedule {

    private int id;
    private String dayName;        // день недели
    private LocalDate date;        // дата
    private List<String> lessons;  // список уроков пар

    public DaySchedule(String dayName, LocalDate date, List<String> lessons) {
        this.dayName = dayName;
        this.date = date;
        this.lessons = lessons;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getLessons() {
        return lessons;
    }

    public void setLessons(List<String> lessons) {
        this.lessons = lessons;
    }

    @Override
    public String toString() {
        return dayName + " (" + date + "): " + lessons;
    }
}

