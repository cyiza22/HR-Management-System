package api.example.hrm_system.Holiday;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public Holiday addHoliday(HolidayDTO dto) {
        Holiday holiday = new Holiday();
        holiday.setName(dto.getName());
        holiday.setDate(dto.getDate());
        holiday.setDescription(dto.getDescription());
        holiday.setCreatedAt(LocalDateTime.now());
        holiday.setUpdatedAt(LocalDateTime.now());

        return holidayRepository.save(holiday);
    }

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public List<Holiday> getUpcomingHolidays() {
        return holidayRepository.findByDateAfter(LocalDate.now());
    }

    public List<Holiday> getPastHolidays() {
        return holidayRepository.findByDateBefore(LocalDate.now());
    }

    public void deleteHoliday(Integer id) {
        holidayRepository.deleteById(id);
    }
}
