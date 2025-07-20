package api.example.hrm_system.Config;

import api.example.hrm_system.department.Department;
import api.example.hrm_system.department.DepartmentRepository;
import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import api.example.hrm_system.job.Job;
import api.example.hrm_system.job.JobRepository;
import api.example.hrm_system.Holiday.Holiday;
import api.example.hrm_system.Holiday.HolidayRepository;
import api.example.hrm_system.user.Role;
import api.example.hrm_system.user.User;
import api.example.hrm_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final HolidayRepository holidayRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeHRAccount();
        initializeDepartments();
        initializeEmployees();
        initializeJobs();
        initializeHolidays();
        createHREmployeeRecord();
    }

    private void initializeHRAccount() {
        String hrEmail = "hrms.hr@gmail.com";
        String hrPassword = "HrPassword12345";

        try {
            if (!userRepository.existsByEmail(hrEmail)) {
                User hrUser = User.builder()
                        .fullName("HR Manager")
                        .email(hrEmail)
                        .password(passwordEncoder.encode(hrPassword))
                        .role(Role.HR)
                        .verified(true)
                        .build();

                userRepository.save(hrUser);
                log.info("HR account created successfully with email: {}", hrEmail);
                log.info("HR can login with email: {} and password: {}", hrEmail, hrPassword);
            } else {
                log.info("HR account already exists with email: {}", hrEmail);
            }
        } catch (Exception e) {
            log.error("Failed to initialize HR account: {}", e.getMessage());
        }
    }

    private void createHREmployeeRecord() {
        String hrEmail = "hrms.hr@gmail.com";
        try {
            if (!employeeRepository.existsByEmail(hrEmail)) {
                Department hrDept = departmentRepository.findByDepartmentName("Human Resources").orElse(null);

                Employee hrEmployee = new Employee();
                hrEmployee.setEmployeeId("HR001");
                hrEmployee.setFirstName("HR");
                hrEmployee.setLastName("Manager");
                hrEmployee.setFullName("HR Manager");
                hrEmployee.setEmail(hrEmail);
                hrEmployee.setMobileNumber("+1234567890");
                hrEmployee.setDateOfBirth(LocalDate.of(1980, 1, 1));
                hrEmployee.setGender("Female");
                hrEmployee.setNationality("American");
                hrEmployee.setMaritalStatus("Single");
                hrEmployee.setAddress("123 HR Street");
                hrEmployee.setCity("HR City");
                hrEmployee.setState("CA");
                hrEmployee.setZipCode("90210");
                hrEmployee.setDesignation("HR Manager");
                hrEmployee.setEmployeeType("Full-time");
                hrEmployee.setJoiningDate(LocalDate.now().minusYears(5));
                hrEmployee.setWorkingDays(5);
                hrEmployee.setOfficeLocation("Main Office");
                hrEmployee.setStatus("Active");
                hrEmployee.setDepartment(hrDept);
                hrEmployee.setLinkedIn("https://linkedin.com/in/hr-manager");
                hrEmployee.setBankAccountNumber("1234567890");
                hrEmployee.setUsername("hrmanager");
                hrEmployee.setSlackId("hr.manager");
                hrEmployee.setGithubId("hr-manager");
                hrEmployee.setSkypeId("hr.manager");

                employeeRepository.save(hrEmployee);
                log.info("HR employee record created successfully");
            } else {
                log.info("HR employee record already exists");
            }
        } catch (Exception e) {
            log.error("Failed to create HR employee record: {}", e.getMessage());
        }
    }

    private void initializeDepartments() {
        try {
            Department[] departments = {
                    createDepartment("Design Department", "Main Office", Department.WorkType.Hybrid),
                    createDepartment("Sales Department", "Main Office", Department.WorkType.Office),
                    createDepartment("Project Manager Department", "Main Office", Department.WorkType.Hybrid),
                    createDepartment("Marketing Department", "Main Office", Department.WorkType.Remote),
                    createDepartment("Information Technology", "Tech Hub", Department.WorkType.Hybrid),
                    createDepartment("Human Resources", "Main Office", Department.WorkType.Office),
                    createDepartment("Finance", "Main Office", Department.WorkType.Office),
                    createDepartment("Operations", "Operations Center", Department.WorkType.Office)
            };

            for (Department dept : departments) {
                if (!departmentRepository.findByDepartmentName(dept.getDepartmentName()).isPresent()) {
                    departmentRepository.save(dept);
                    log.info("Created department: {}", dept.getDepartmentName());
                }
            }

            log.info("Department initialization completed");
        } catch (Exception e) {
            log.error("Failed to initialize departments: {}", e.getMessage());
        }
    }

    private Department createDepartment(String name, String location, Department.WorkType workType) {
        Department department = new Department();
        department.setDepartmentName(name);
        department.setLocation(location);
        department.setWorkType(workType);
        department.setEmploymentStatus(Department.EmploymentStatus.Active);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        return department;
    }

    private void initializeEmployees() {
        try {
            // Create sample employees for different departments
            if (employeeRepository.count() <= 1) { // Allow for HR employee
                Department designDept = departmentRepository.findByDepartmentName("Design Department").orElse(null);
                Department salesDept = departmentRepository.findByDepartmentName("Sales Department").orElse(null);
                Department marketingDept = departmentRepository.findByDepartmentName("Marketing Department").orElse(null);
                Department itDept = departmentRepository.findByDepartmentName("Information Technology").orElse(null);

                if (designDept != null) {
                    createEmployee("Dionne", "Russell", "dionne.russell@company.com", "EMP001", "Senior Designer", designDept);
                    createEmployee("Arlene", "McCoy", "arlene.mccoy@company.com", "EMP002", "UI/UX Designer", designDept);
                    createEmployee("Cody", "Fisher", "cody.fisher@company.com", "EMP003", "Graphic Designer", designDept);
                    createEmployee("Theresa", "Webb", "theresa.webb@company.com", "EMP004", "Product Designer", designDept);
                    createEmployee("Ronald", "Richards", "ronald.richards@company.com", "EMP005", "Design Lead", designDept);
                }

                if (salesDept != null) {
                    createEmployee("Darrell", "Steward", "darrell.steward@company.com", "EMP006", "Sales Manager", salesDept);
                    createEmployee("Kristin", "Watson", "kristin.watson@company.com", "EMP007", "Sales Executive", salesDept);
                    createEmployee("Courtney", "Henry", "courtney.henry@company.com", "EMP008", "Account Manager", salesDept);
                    createEmployee("Kathryn", "Murphy", "kathryn.murphy@company.com", "EMP009", "Sales Representative", salesDept);
                    createEmployee("Albert", "Flores", "albert.flores@company.com", "EMP010", "Business Development", salesDept);
                }

                if (marketingDept != null) {
                    createEmployee("Wade", "Warren", "wade.warren@company.com", "EMP011", "Marketing Manager", marketingDept);
                    createEmployee("Brooklyn", "Simmons", "brooklyn.simmons@company.com", "EMP012", "Digital Marketer", marketingDept);
                    createEmployee("Jacob", "Jones", "jacob.jones@company.com", "EMP013", "Content Creator", marketingDept);
                    createEmployee("Cody", "Fisher", "cody.fisher.marketing@company.com", "EMP014", "SEO Specialist", marketingDept);
                }

                if (itDept != null) {
                    createEmployee("Leslie", "Alexander", "leslie.alexander@company.com", "EMP015", "Project Manager", itDept);
                    createEmployee("Savannah", "Nguyen", "savannah.nguyen@company.com", "EMP016", "Software Developer", itDept);
                    createEmployee("Eleanor", "Pena", "eleanor.pena@company.com", "EMP017", "DevOps Engineer", itDept);
                    createEmployee("Esther", "Howard", "esther.howard@company.com", "EMP018", "QA Engineer", itDept);
                }

                log.info("Sample employees created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize employees: {}", e.getMessage());
        }
    }

    private void createEmployee(String firstName, String lastName, String email, String employeeId, String designation, Department department) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setFullName(firstName + " " + lastName);
        employee.setEmail(email);
        employee.setEmployeeId(employeeId);
        employee.setDesignation(designation);
        employee.setDepartment(department);
        employee.setEmployeeType("Full-time");
        employee.setJoiningDate(LocalDate.now().minusDays((long) (Math.random() * 365)));
        employee.setWorkingDays(5);
        employee.setOfficeLocation(department.getLocation());
        employee.setStatus("Active");
        employee.setMobileNumber("+1" + (int)(Math.random() * 9000000000L + 1000000000L));
        employee.setDateOfBirth(LocalDate.now().minusYears(25 + (int)(Math.random() * 15)));
        employee.setGender(Math.random() > 0.5 ? "Male" : "Female");
        employee.setNationality("American");
        employee.setMaritalStatus(Math.random() > 0.6 ? "Married" : "Single");
        employee.setAddress("123 Main St");
        employee.setCity("New York");
        employee.setState("NY");
        employee.setZipCode("10001");
        employee.setUsername(firstName.toLowerCase() + "." + lastName.toLowerCase());
        employee.setLinkedIn("https://linkedin.com/in/" + firstName.toLowerCase() + "-" + lastName.toLowerCase());
        employee.setBankAccountNumber("ACC" + (int)(Math.random() * 900000000 + 100000000));
        employee.setSlackId(firstName.toLowerCase() + "." + lastName.toLowerCase());
        employee.setGithubId(firstName.toLowerCase() + "-" + lastName.toLowerCase());
        employee.setSkypeId(firstName.toLowerCase() + "." + lastName.toLowerCase());

        employeeRepository.save(employee);
    }

    private void initializeJobs() {
        try {
            if (jobRepository.count() == 0) {
                Department designDept = departmentRepository.findByDepartmentName("Design Department").orElse(null);
                Department salesDept = departmentRepository.findByDepartmentName("Sales Department").orElse(null);
                Department marketingDept = departmentRepository.findByDepartmentName("Marketing Department").orElse(null);
                Department itDept = departmentRepository.findByDepartmentName("Information Technology").orElse(null);

                if (designDept != null) {
                    createJob("Senior UI/UX Designer", "Lead design projects and mentor junior designers", designDept, "New York", new BigDecimal("85000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                    createJob("Product Designer", "Design user-centered digital products", designDept, "San Francisco", new BigDecimal("78000"), Job.JobType.Remote, Job.JobStatus.Active);
                }

                if (salesDept != null) {
                    createJob("Sales Representative", "Drive revenue growth through client acquisition", salesDept, "Chicago", new BigDecimal("60000"), Job.JobType.Office, Job.JobStatus.Active);
                    createJob("Account Manager", "Manage and grow existing client relationships", salesDept, "Boston", new BigDecimal("72000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                }

                if (marketingDept != null) {
                    createJob("Digital Marketing Specialist", "Execute digital marketing campaigns", marketingDept, "Austin", new BigDecimal("65000"), Job.JobType.Remote, Job.JobStatus.Active);
                    createJob("Content Marketing Manager", "Lead content strategy and creation", marketingDept, "Seattle", new BigDecimal("75000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                }

                if (itDept != null) {
                    createJob("Full Stack Developer", "Develop and maintain web applications", itDept, "San Francisco", new BigDecimal("95000"), Job.JobType.Remote, Job.JobStatus.Active);
                    createJob("DevOps Engineer", "Manage infrastructure and deployment pipelines", itDept, "New York", new BigDecimal("105000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                }

                log.info("Sample jobs created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize jobs: {}", e.getMessage());
        }
    }

    private void createJob(String title, String description, Department department, String location, BigDecimal salary, Job.JobType jobType, Job.JobStatus status) {
        Job job = new Job();
        job.setJobTitle(title);
        job.setJobDescription(description);
        job.setDepartment(department);
        job.setLocation(location);
        job.setSalary(salary);
        job.setJobType(jobType);
        job.setStatus(status);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        jobRepository.save(job);
    }

    private void initializeHolidays() {
        try {
            if (holidayRepository.count() == 0) {
                // Create holidays for the current year
                int currentYear = LocalDate.now().getYear();

                Holiday[] holidays = {
                        createHoliday("New Year's Day", LocalDate.of(currentYear, 1, 1), "Federal Holiday"),
                        createHoliday("Martin Luther King Jr. Day", LocalDate.of(currentYear, 1, 15), "Federal Holiday"),
                        createHoliday("Presidents' Day", LocalDate.of(currentYear, 2, 19), "Federal Holiday"),
                        createHoliday("Memorial Day", LocalDate.of(currentYear, 5, 27), "Federal Holiday"),
                        createHoliday("Independence Day", LocalDate.of(currentYear, 7, 4), "Federal Holiday"),
                        createHoliday("Labor Day", LocalDate.of(currentYear, 9, 2), "Federal Holiday"),
                        createHoliday("Columbus Day", LocalDate.of(currentYear, 10, 14), "Federal Holiday"),
                        createHoliday("Veterans Day", LocalDate.of(currentYear, 11, 11), "Federal Holiday"),
                        createHoliday("Thanksgiving", LocalDate.of(currentYear, 11, 28), "Federal Holiday"),
                        createHoliday("Christmas Day", LocalDate.of(currentYear, 12, 25), "Federal Holiday"),
                        createHoliday("Company Retreat", LocalDate.of(currentYear, 8, 15), "Company Event"),
                        createHoliday("Summer Break", LocalDate.of(currentYear, 7, 15), "Company Holiday")
                };

                for (Holiday holiday : holidays) {
                    holidayRepository.save(holiday);
                }

                log.info("Sample holidays created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize holidays: {}", e.getMessage());
        }
    }

    private Holiday createHoliday(String name, LocalDate date, String description) {
        Holiday holiday = new Holiday();
        holiday.setName(name);
        holiday.setDate(date);
        holiday.setDescription(description);
        holiday.setCreatedAt(LocalDateTime.now());
        holiday.setUpdatedAt(LocalDateTime.now());
        return holiday;
    }
}