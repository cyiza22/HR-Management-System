package api.example.hrm_system.employee.ProfessionalInfo;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.department.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalInfoService {

    @Autowired
    private ProfessionalInfoRepository professionalInfoRepository;

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
        return professionalInfoRepository.findByDepartment(department)
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

    public List<ProfessionalInfoDTO> getProfessionalInfoByWorkingDays(String workingDays) {
        return professionalInfoRepository.findByWorkingDays(workingDays)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByDepartmentAndDesignation(String department, String designation) {
        return professionalInfoRepository.findByDepartmentAndDesignation(department, designation)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProfessionalInfoDTO> getProfessionalInfoByDepartmentAndOfficeLocation(String department, String officeLocation) {
        return professionalInfoRepository.findByDepartmentAndOfficeLocation(department, officeLocation)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ProfessionalInfoDTO createProfessionalInfo(ProfessionalInfoDTO professionalInfoDTO) {
        Employee entity = convertToEntity(professionalInfoDTO);
        Employee savedEntity = professionalInfoRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    public Optional<ProfessionalInfoDTO> updateProfessionalInfo(Long id, ProfessionalInfoDTO professionalInfoDTO) {
        return professionalInfoRepository.findById(id)
                .map(existingEntity -> {
                    updateEntityFromDTO(existingEntity, professionalInfoDTO);
                    Employee updatedEntity = professionalInfoRepository.save(existingEntity);
                    return convertToDTO(updatedEntity);
                });
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
        return professionalInfoRepository.countByDepartment(department);
    }

    public long countByDesignation(String designation) {
        return professionalInfoRepository.countByDesignation(designation);
    }

    public long countByOfficeLocation(String officeLocation) {
        return professionalInfoRepository.countByOfficeLocation(officeLocation);
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

        if (entity.getWorkingDays() != null) {
            dto.setWorkingDays(entity.getWorkingDays().toString());
        }

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
        if (dto.getWorkingDays() != null) {
            entity.setWorkingDays(Integer.parseInt(dto.getWorkingDays()));
        }

        entity.setJoiningDate(dto.getJoiningDate());
        entity.setOfficeLocation(dto.getOfficeLocation());
        return entity;
    }

    private void updateEntityFromDTO(Employee entity, ProfessionalInfoDTO dto) {
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setEmployeeType(dto.getEmployeeType());
        if (dto.getDepartment() != null) {
            if (entity.getDepartment() == null) {
                entity.setDepartment(new Department());
            }
            entity.getDepartment().setDepartmentName(dto.getDepartment());
        }

        entity.setDesignation(dto.getDesignation());
        if (dto.getWorkingDays() != null) {
            entity.setWorkingDays(Integer.parseInt(dto.getWorkingDays()));
        }

        entity.setJoiningDate(dto.getJoiningDate());
        entity.setOfficeLocation(dto.getOfficeLocation());
    }
}