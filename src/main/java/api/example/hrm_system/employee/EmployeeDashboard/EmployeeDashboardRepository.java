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

    // Find by first name and last name
    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    // Find by first name containing (case insensitive)
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<Employee> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    // Find by last name containing (case insensitive)
    @Query("SELECT e FROM Employee e WHERE LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Employee> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);

    // Find by full name containing (case insensitive)
    @Query("SELECT e FROM Employee e WHERE LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<Employee> findByFullNameContainingIgnoreCase(@Param("fullName") String fullName);

    // Find by employee ID
    Optional<Employee> findByEmployeeId(String employeeId);

    // Find by department name
    @Query("SELECT e FROM Employee e JOIN e.department d WHERE d.name = :departmentName")
    List<Employee> findByDepartmentName(@Param("departmentName") String departmentName);

    // Find by designation
    List<Employee> findByDesignation(String designation);

    // Find by employee type
    List<Employee> findByEmployeeType(String employeeType);

    // Find by status
    List<Employee> findByStatus(String status);

    // Find by multiple filters
    @Query("SELECT e FROM Employee e JOIN e.department d WHERE " +
            "(:department IS NULL OR d.name = :department) AND " +
            "(:designation IS NULL OR e.designation = :designation) AND " +
            "(:employeeType IS NULL OR e.employeeType = :employeeType) AND " +
            "(:status IS NULL OR e.status = :status)")
    List<Employee> findByMultipleFilters(@Param("department") String department,
                                         @Param("designation") String designation,
                                         @Param("employeeType") String employeeType,
                                         @Param("status") String status);

    // Search employees by name, email, or employee ID
    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);

    // Count by department
    @Query("SELECT COUNT(e) FROM Employee e JOIN e.department d WHERE d.name = :departmentName")
    long countByDepartmentName(@Param("departmentName") String departmentName);

    // Count by status
    long countByStatus(String status);

    // Count by employee type
    long countByEmployeeType(String employeeType);
}
