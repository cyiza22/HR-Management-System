package api.example.hrm_system.Holiday;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
    List<Holiday> findByDateAfter(LocalDate date);
    List<Holiday> findByDateBefore(LocalDate date);

}
