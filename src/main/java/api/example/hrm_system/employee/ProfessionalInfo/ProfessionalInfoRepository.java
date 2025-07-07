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

    // Fix: Use dot notation for nested object field (department.departmentName)
    List<Employee> findByDepartment_DepartmentName(String departmentName);

    List<Employee> findByDesignation(String designation);

    List<Employee> findByEmployeeType(String employeeType);

    List<Employee> findByOfficeLocation(String officeLocation);

    List<Employee> findByWorkingDays(Integer workingDays);

    // Fix: Use dot notation for both department and designation
    List<Employee> findByDepartment_DepartmentNameAndDesignation(String departmentName, String designation);

    List<Employee> findByDepartment_DepartmentNameAndOfficeLocation(String departmentName, String officeLocation);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Fix: Count by department name
    long countByDepartment_DepartmentName(String departmentName);

    long countByDesignation(String designation);

    long countByOfficeLocation(String officeLocation);
}
