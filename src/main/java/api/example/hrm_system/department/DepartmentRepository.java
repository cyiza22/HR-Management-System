package api.example.hrm_system.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentName(String departmentName);
    List<Department> findByLocation(String location);
    List<Department> findByWorkType(Department.WorkType workType);
    List<Department> findByEmploymentStatus(Department.EmploymentStatus employmentStatus);

    @Query("SELECT d FROM Department d WHERE d.manager.id = :managerId")
    List<Department> findByManagerId(Long managerId);

    @Query("SELECT d FROM Department d WHERE d.departmentName LIKE %:name%")
    List<Department> findByDepartmentNameContaining(String name);
}