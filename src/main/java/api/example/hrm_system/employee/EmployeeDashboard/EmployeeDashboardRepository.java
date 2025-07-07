package api.example.hrm_system.employee.EmployeeDashboard;

import api.example.hrm_system.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeDashboardRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<Employee> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Employee> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);

    @Query("SELECT e FROM Employee e WHERE LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<Employee> findByFullNameContainingIgnoreCase(@Param("fullName") String fullName);

    Optional<Employee> findByEmployeeId(String employeeId);

    // Fixed: changed d.name to d.departmentName
    @Query("SELECT e FROM Employee e JOIN e.department d WHERE d.departmentName = :departmentName")
    List<Employee> findByDepartmentName(@Param("departmentName") String departmentName);

    List<Employee> findByDesignation(String designation);

    List<Employee> findByEmployeeType(String employeeType);

    List<Employee> findByStatus(String status);

    @Query("SELECT e FROM Employee e JOIN e.department d WHERE " +
            "(:department IS NULL OR d.departmentName = :department) AND " +
            "(:designation IS NULL OR e.designation = :designation) AND " +
            "(:employeeType IS NULL OR e.employeeType = :employeeType) AND " +
            "(:status IS NULL OR e.status = :status)")
    List<Employee> findByMultipleFilters(@Param("department") String department,
                                         @Param("designation") String designation,
                                         @Param("employeeType") String employeeType,
                                         @Param("status") String status);

    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);

    // Fixed: changed d.name to d.departmentName
    @Query("SELECT COUNT(e) FROM Employee e JOIN e.department d WHERE d.departmentName = :departmentName")
    long countByDepartmentName(@Param("departmentName") String departmentName);

    long countByStatus(String status);

    long countByEmployeeType(String employeeType);
}
