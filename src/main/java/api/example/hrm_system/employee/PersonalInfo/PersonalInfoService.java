package api.example.hrm_system.employee.PersonalInfo;


import api.example.hrm_system.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonalInfoService {

    @Autowired
    private PersonalInfoRepository personalInfoRepository;
    public PersonalInfoService(PersonalInfoRepository personalInfoRepository) {
        this.personalInfoRepository = personalInfoRepository;
    }


    public PersonalInfoDTO savePersonalInfo(PersonalInfoDTO personalInfoDto) {
        Employee employee = convertToEntity(personalInfoDto);
        Employee savedEmployee = personalInfoRepository.save(employee);
        return convertToDto(savedEmployee);
    }

    public PersonalInfoDTO updatePersonalInfo(Long id, PersonalInfoDTO personalInfoDto) {
        Optional<Employee> existingEmployee = personalInfoRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            updateEmployeeFromDto(employee, personalInfoDto);
            Employee savedEmployee = personalInfoRepository.save(employee);
            return convertToDto(savedEmployee);
        }
        throw new RuntimeException("Employee not found with id: " + id);
    }

    public PersonalInfoDTO getPersonalInfoById(Long id) {
        Optional<Employee> employee = personalInfoRepository.findById(id);
        if (employee.isPresent()) {
            return convertToDto(employee.get());
        }
        throw new RuntimeException("Employee not found with id: " + id);
    }

    public PersonalInfoDTO getPersonalInfoByName(String firstName, String lastName) {
        Optional<Employee> employee = personalInfoRepository.findByFirstNameAndLastName(firstName, lastName);
        if (employee.isPresent()) {
            return convertToDto(employee.get());
        }
        throw new RuntimeException("Employee not found with name: " + firstName + " " + lastName);
    }

    public List<PersonalInfoDTO> searchByFirstName(String firstName) {
        List<Employee> employees = personalInfoRepository.findByFirstNameContainingIgnoreCase(firstName);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PersonalInfoDTO> searchByLastName(String lastName) {
        List<Employee> employees = personalInfoRepository.findByLastNameContainingIgnoreCase(lastName);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PersonalInfoDTO> searchByFullName(String fullName) {
        List<Employee> employees = personalInfoRepository.findByFullNameContainingIgnoreCase(fullName);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PersonalInfoDTO getPersonalInfoByEmail(String email) {
        Optional<Employee> employee = personalInfoRepository.findByEmail(email);
        if (employee.isPresent()) {
            return convertToDto(employee.get());
        }
        throw new RuntimeException("Employee not found with email: " + email);
    }

    public List<PersonalInfoDTO> getAllPersonalInfo() {
        List<Employee> employees = personalInfoRepository.findAll();
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deletePersonalInfo(Long id) {
        personalInfoRepository.deleteById(id);
    }

    private PersonalInfoDTO convertToDto(Employee employee) {
        return new PersonalInfoDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getMobileNumber(),
                employee.getEmail(),
                employee.getDateOfBirth(),
                employee.getGender(),
                employee.getNationality(),
                employee.getMaritalStatus(),
                employee.getAddress(),
                employee.getCity(),
                employee.getState(),
                employee.getZipCode()
        );
    }

    private Employee convertToEntity(PersonalInfoDTO dto) {
        Employee employee = new Employee();
        updateEmployeeFromDto(employee, dto);
        return employee;
    }

    private void updateEmployeeFromDto(Employee employee, PersonalInfoDTO dto) {
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setMobileNumber(dto.getMobileNumber());
        employee.setEmail(dto.getEmail());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGender(dto.getGender());
        employee.setNationality(dto.getNationality());
        employee.setMaritalStatus(dto.getMaritalStatus());
        employee.setAddress(dto.getAddress());
        employee.setCity(dto.getCity());
        employee.setState(dto.getState());
        employee.setZipCode(dto.getZipCode());
    }
}