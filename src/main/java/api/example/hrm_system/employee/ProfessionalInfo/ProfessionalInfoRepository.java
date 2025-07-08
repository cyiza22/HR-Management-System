package api.example.hrm_system.employee.ProfessionalInfo;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalInfoRepository extends EmployeeRepository {
    List<Employee> findByDepartment_DepartmentName(String departmentName);

    List<Employee> findByDesignation(String designation);

    List<Employee> findByEmployeeType(String employeeType);

    List<Employee> findByOfficeLocation(String officeLocation);

    List<Employee> findByWorkingDays(Integer workingDays);
    List<Employee> findByDepartment_DepartmentNameAndDesignation(String departmentName, String designation);

    List<Employee> findByDepartment_DepartmentNameAndOfficeLocation(String departmentName, String officeLocation);

    long countByDepartment_DepartmentName(String departmentName);

    long countByDesignation(String designation);

    long countByOfficeLocation(String officeLocation);
}
