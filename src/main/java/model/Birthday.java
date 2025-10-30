package model;

import java.time.LocalDate;

public class Birthday {

    private int id;
    private String name;
    private LocalDate birthday;

    public Birthday(String name, LocalDate birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return name + " — " + birthday.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
