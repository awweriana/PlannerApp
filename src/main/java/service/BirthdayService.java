package service;

import model.Birthday;
import java.util.List;

public interface BirthdayService {
    void saveBirthdayService(Birthday birthday);
    List<Birthday> loadAllBirthday();
    void deleteBirthday(Birthday birthday);
}
