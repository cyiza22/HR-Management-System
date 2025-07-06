package api.example.hrm_system.Holiday;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping("/add")
    public ResponseEntity<Holiday> addHoliday(@RequestBody HolidayDTO dto) {
        return ResponseEntity.ok(holidayService.addHoliday(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        return ResponseEntity.ok(holidayService.getAllHolidays());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Holiday>> getUpcomingHolidays() {
        return ResponseEntity.ok(holidayService.getUpcomingHolidays());
    }

    @GetMapping("/past")
    public ResponseEntity<List<Holiday>> getPastHolidays() {
        return ResponseEntity.ok(holidayService.getPastHolidays());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Integer id) {
        holidayService.deleteHoliday(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Holiday>> searchHolidays() {
        return ResponseEntity.ok(holidayService.getUpcomingHolidays());
    }

}
