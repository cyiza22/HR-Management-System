package api.example.hrm_system.employee.PersonalInfo;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalInfoRepository extends EmployeeRepository {
    // Find by first name containing (case insensitive)
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<Employee> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    // Find by last name containing (case insensitive)
    @Query("SELECT e FROM Employee e WHERE LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Employee> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);

    // Find by full name containing (case insensitive)
    @Query("SELECT e FROM Employee e WHERE LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<Employee> findByFullNameContainingIgnoreCase(@Param("fullName") String fullName);

    Optional<Employee> findByMobileNumber(String mobileNumber);

    List<Employee> findByGender(String gender);

    List<Employee> findByNationality(String nationality);

    List<Employee> findByMaritalStatus(String maritalStatus);

    List<Employee> findByCity(String city);

    List<Employee> findByState(String state);
}
