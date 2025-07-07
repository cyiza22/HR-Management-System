package api.example.hrm_system.employee.ProfessionalInfo;

import api.example.hrm_system.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalInfoRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(String employeeId);

    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartment(String department);

    List<Employee> findByDesignation(String designation);

    List<Employee> findByEmployeeType(String employeeType);

    List<Employee> findByOfficeLocation(String officeLocation);

    List<Employee> findByWorkingDays(String workingDays);

    List<Employee> findByDepartmentAndDesignation(String department, String designation);

    List<Employee> findByDepartmentAndOfficeLocation(String department, String officeLocation);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByDepartment(String department);

    long countByDesignation(String designation);

    long countByOfficeLocation(String officeLocation);
}