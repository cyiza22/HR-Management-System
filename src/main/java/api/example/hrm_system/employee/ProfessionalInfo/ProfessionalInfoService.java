package api.example.hrm_system.employee.ProfessionalInfo;

import aj.org.objectweb.asm.commons.Remapper;
import api.example.hrm_system.department.DepartmentRepository;
import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.department.Department;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalInfoService {

    @Autowired
    private ProfessionalInfoRepository professionalInfoRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<ProfessionalInfoDTO> getAllProfessionalInfo() {
        return professionalInfoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<ProfessionalInfoDTO> getProfessionalInfoById(Long id) {
        return professionalInfoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<ProfessionalInfoDTO> getProfessionalInfoByEmployeeId(String employeeId) {
        return professionalInfoRepository.findByEmployeeId(employeeId)
                .map(this::convertToDTO);
    }

    public Optional<ProfessionalInfoDTO> getProfessionalInfoByUsername(String username) {
        return professionalInfoRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    public Optional<ProfessionalInfoDTO> getProfessionalInfoByEmail(String email) {
        return professionalInfoRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByDepartment(String department) {
        return professionalInfoRepository.findByDepartment_DepartmentName(department)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByDesignation(String designation) {
        return professionalInfoRepository.findByDesignation(designation)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByEmployeeType(String employeeType) {
        return professionalInfoRepository.findByEmployeeType(employeeType)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByOfficeLocation(String officeLocation) {
        return professionalInfoRepository.findByOfficeLocation(officeLocation)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByWorkingDays(Integer workingDays) {
        return professionalInfoRepository.findByWorkingDays(workingDays)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByDepartmentAndDesignation(String department, String designation) {
        return professionalInfoRepository.findByDepartment_DepartmentNameAndDesignation(department, designation)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByDepartmentAndOfficeLocation(String department, String officeLocation) {
        return professionalInfoRepository.findByDepartment_DepartmentNameAndOfficeLocation(department, officeLocation)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ProfessionalInfoDTO createProfessionalInfo(ProfessionalInfoDTO professionalInfoDTO) {
        Employee entity = convertToEntity(professionalInfoDTO);
        Employee savedEntity = professionalInfoRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    private void updateEntityFromDTO(Employee entity, ProfessionalInfoDTO dto) {
        if (dto.getEmployeeId() != null) {
            entity.setEmployeeId(dto.getEmployeeId());
        }
        if (dto.getUsername() != null) {
            entity.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getEmployeeType() != null) {
            entity.setEmployeeType(dto.getEmployeeType());
        }
        if (dto.getDepartment() != null) {
            Department department = departmentRepository.findByDepartmentName(dto.getDepartment())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            entity.setDepartment(department);
        }
        if (dto.getDesignation() != null) {
            entity.setDesignation(dto.getDesignation());
        }
        if (dto.getWorkingDays() != null) {
            entity.setWorkingDays(dto.getWorkingDays());
        }
        if (dto.getJoiningDate() != null) {
            entity.setJoiningDate(dto.getJoiningDate());
        }
        if (dto.getOfficeLocation() != null) {
            entity.setOfficeLocation(dto.getOfficeLocation());
        }
    }

    public boolean deleteProfessionalInfo(Long id) {
        if (professionalInfoRepository.existsById(id)) {
            professionalInfoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByEmployeeId(String employeeId) {
        return professionalInfoRepository.existsByEmployeeId(employeeId);
    }

    public boolean existsByUsername(String username) {
        return professionalInfoRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return professionalInfoRepository.existsByEmail(email);
    }

    public long countByDepartment(String department) {
        return professionalInfoRepository.countByDepartment_DepartmentName(department);
    }

    public long countByDesignation(String designation) {
        return professionalInfoRepository.countByDesignation(designation);
    }

    public long countByOfficeLocation(String officeLocation) {
        return professionalInfoRepository.countByOfficeLocation(officeLocation);
    }

    public Optional<ProfessionalInfoDTO> updateProfessionalInfo(Long id, ProfessionalInfoDTO dto) {
        return professionalInfoRepository.findById(id)
                .map(existingEmployee -> {
                    updateEntityFromDTO(existingEmployee, dto);
                    Employee updatedEmployee = professionalInfoRepository.save(existingEmployee);
                    return convertToDTO(updatedEmployee);
                });
    }

    private ProfessionalInfoDTO convertToDTO(Employee entity) {
        ProfessionalInfoDTO dto = new ProfessionalInfoDTO();
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setEmployeeType(entity.getEmployeeType());

        if (entity.getDepartment() != null) {
            dto.setDepartment(entity.getDepartment().getDepartmentName());
        }

        dto.setDesignation(entity.getDesignation());
        dto.setWorkingDays(entity.getWorkingDays()); // Direct assignment now
        dto.setJoiningDate(entity.getJoiningDate());
        dto.setOfficeLocation(entity.getOfficeLocation());

        return dto;
    }

    private Employee convertToEntity(ProfessionalInfoDTO dto) {
        Employee entity = new Employee();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setEmployeeType(dto.getEmployeeType());

        if (dto.getDepartment() != null) {
            Department department = new Department();
            department.setDepartmentName(dto.getDepartment());
            entity.setDepartment(department);
        }

        entity.setDesignation(dto.getDesignation());
        entity.setWorkingDays(dto.getWorkingDays()); // Direct assignment now
        entity.setJoiningDate(dto.getJoiningDate());
        entity.setOfficeLocation(dto.getOfficeLocation());

        return entity;
    }


}

