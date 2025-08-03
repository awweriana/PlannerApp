package model;
import java.time.LocalDate;

public class Task {
    private int id;
    private String title;// название
    private String description;// описание
    private LocalDate date;// дата

    public Task(Integer id,String title, String description, LocalDate date) {
        this.title = title;//название
        this.description = description;// описание
        this.date = date;
        this.id = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {return id; }

    public void setId(int id) { this.id = id; }


    @Override
    public String toString() {
        return id + title +" "+ description+ " (" + date + ") " ;
    }
}
