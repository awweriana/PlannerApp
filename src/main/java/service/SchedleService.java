package service;

import model.DaySchedule;
import java.util.List;

public interface SchedleService {
    void saveSchedule(DaySchedule schedule);
    List<DaySchedule> loadAllSchedules();

}
