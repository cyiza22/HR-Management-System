package api.example.hrm_system.Config;

import api.example.hrm_system.Candidate.Candidate;
import api.example.hrm_system.Candidate.CandidateRepository;
import api.example.hrm_system.Document.Document;
import api.example.hrm_system.Document.DocumentRepository;
import api.example.hrm_system.Holiday.Holiday;
import api.example.hrm_system.Holiday.HolidayRepository;
import api.example.hrm_system.Leave.Leave;
import api.example.hrm_system.Leave.LeaveRepository;
import api.example.hrm_system.Notification.Notification;
import api.example.hrm_system.Notification.NotificationRepository;
import api.example.hrm_system.Project.Project;
import api.example.hrm_system.Project.ProjectRepository;
import api.example.hrm_system.attendance.Attendance;
import api.example.hrm_system.attendance.AttendanceRepository;
import api.example.hrm_system.department.Department;
import api.example.hrm_system.department.DepartmentRepository;
import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import api.example.hrm_system.job.Job;
import api.example.hrm_system.job.JobRepository;
import api.example.hrm_system.payroll.Payroll;
import api.example.hrm_system.payroll.PayrollRepository;
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
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final HolidayRepository holidayRepository;
    private final PayrollRepository payrollRepository;
    private final LeaveRepository leaveRepository;
    private final ProjectRepository projectRepository;
    private final AttendanceRepository attendanceRepository;
    private final DocumentRepository documentRepository;
    private final NotificationRepository notificationRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Starting data initialization...");
        initializeHRAccount();
        initializeDepartments();
        initializeEmployees();
        initializeJobs();
        initializeHolidays();
        createHREmployeeRecord();
        initializePayrolls();
        initializeLeaves();
        initializeProjects();
        initializeAttendance();
        initializeDocuments();
        initializeNotifications();
        initializeCandidates();
        log.info("Data initialization completed successfully!");
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
            if (employeeRepository.count() <= 1) { // Allow for HR employee
                Department designDept = departmentRepository.findByDepartmentName("Design Department").orElse(null);
                Department salesDept = departmentRepository.findByDepartmentName("Sales Department").orElse(null);
                Department marketingDept = departmentRepository.findByDepartmentName("Marketing Department").orElse(null);
                Department itDept = departmentRepository.findByDepartmentName("Information Technology").orElse(null);
                Department financeDept = departmentRepository.findByDepartmentName("Finance").orElse(null);
                Department operationsDept = departmentRepository.findByDepartmentName("Operations").orElse(null);

                // Create users for authentication
                createUserAccounts();

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
                    createEmployee("Devon", "Lane", "devon.lane@company.com", "EMP014", "SEO Specialist", marketingDept);
                }

                if (itDept != null) {
                    createEmployee("Leslie", "Alexander", "leslie.alexander@company.com", "EMP015", "Project Manager", itDept);
                    createEmployee("Savannah", "Nguyen", "savannah.nguyen@company.com", "EMP016", "Software Developer", itDept);
                    createEmployee("Eleanor", "Pena", "eleanor.pena@company.com", "EMP017", "DevOps Engineer", itDept);
                    createEmployee("Esther", "Howard", "esther.howard@company.com", "EMP018", "QA Engineer", itDept);
                    createEmployee("Cameron", "Williamson", "cameron.williamson@company.com", "EMP019", "Full Stack Developer", itDept);
                }

                if (financeDept != null) {
                    createEmployee("Marvin", "McKinney", "marvin.mckinney@company.com", "EMP020", "Finance Manager", financeDept);
                    createEmployee("Jane", "Cooper", "jane.cooper@company.com", "EMP021", "Accountant", financeDept);
                    createEmployee("Robert", "Fox", "robert.fox@company.com", "EMP022", "Financial Analyst", financeDept);
                }

                if (operationsDept != null) {
                    createEmployee("Annette", "Black", "annette.black@company.com", "EMP023", "Operations Manager", operationsDept);
                    createEmployee("Ralph", "Edwards", "ralph.edwards@company.com", "EMP024", "Operations Coordinator", operationsDept);
                }

                log.info("Sample employees created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize employees: {}", e.getMessage());
        }
    }

    private void createUserAccounts() {
        // Create user accounts for employees
        String[] emails = {
                "dionne.russell@company.com", "arlene.mccoy@company.com", "cody.fisher@company.com",
                "theresa.webb@company.com", "ronald.richards@company.com", "darrell.steward@company.com",
                "kristin.watson@company.com", "courtney.henry@company.com", "kathryn.murphy@company.com",
                "albert.flores@company.com", "wade.warren@company.com", "brooklyn.simmons@company.com",
                "jacob.jones@company.com", "devon.lane@company.com", "leslie.alexander@company.com",
                "savannah.nguyen@company.com", "eleanor.pena@company.com", "esther.howard@company.com",
                "cameron.williamson@company.com", "marvin.mckinney@company.com", "jane.cooper@company.com",
                "robert.fox@company.com", "annette.black@company.com", "ralph.edwards@company.com"
        };

        String[] names = {
                "Dionne Russell", "Arlene McCoy", "Cody Fisher", "Theresa Webb", "Ronald Richards",
                "Darrell Steward", "Kristin Watson", "Courtney Henry", "Kathryn Murphy", "Albert Flores",
                "Wade Warren", "Brooklyn Simmons", "Jacob Jones", "Devon Lane", "Leslie Alexander",
                "Savannah Nguyen", "Eleanor Pena", "Esther Howard", "Cameron Williamson", "Marvin McKinney",
                "Jane Cooper", "Robert Fox", "Annette Black", "Ralph Edwards"
        };

        Role[] roles = {
                Role.EMPLOYEE, Role.EMPLOYEE, Role.EMPLOYEE, Role.EMPLOYEE, Role.MANAGER,
                Role.MANAGER, Role.EMPLOYEE, Role.EMPLOYEE, Role.EMPLOYEE, Role.EMPLOYEE,
                Role.MANAGER, Role.EMPLOYEE, Role.EMPLOYEE, Role.EMPLOYEE, Role.MANAGER,
                Role.EMPLOYEE, Role.EMPLOYEE, Role.EMPLOYEE, Role.EMPLOYEE, Role.MANAGER,
                Role.EMPLOYEE, Role.EMPLOYEE, Role.MANAGER, Role.EMPLOYEE
        };

        for (int i = 0; i < emails.length; i++) {
            if (!userRepository.existsByEmail(emails[i])) {
                User user = User.builder()
                        .fullName(names[i])
                        .email(emails[i])
                        .password(passwordEncoder.encode("Password123"))
                        .role(roles[i])
                        .verified(true)
                        .build();
                userRepository.save(user);
                log.info("Created user account: {} with role: {}", emails[i], roles[i]);
            }
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
        employee.setAddress("123 " + firstName + " St");
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
                Department financeDept = departmentRepository.findByDepartmentName("Finance").orElse(null);

                if (designDept != null) {
                    createJob("Senior UI/UX Designer", "Lead design projects and mentor junior designers", designDept, "New York", new BigDecimal("85000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                    createJob("Product Designer", "Design user-centered digital products", designDept, "San Francisco", new BigDecimal("78000"), Job.JobType.Remote, Job.JobStatus.Active);
                    createJob("Graphic Designer", "Create visual concepts and designs", designDept, "Los Angeles", new BigDecimal("65000"), Job.JobType.Office, Job.JobStatus.Active);
                }

                if (salesDept != null) {
                    createJob("Sales Representative", "Drive revenue growth through client acquisition", salesDept, "Chicago", new BigDecimal("60000"), Job.JobType.Office, Job.JobStatus.Active);
                    createJob("Account Manager", "Manage and grow existing client relationships", salesDept, "Boston", new BigDecimal("72000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                    createJob("Business Development Manager", "Identify new business opportunities", salesDept, "Miami", new BigDecimal("80000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                }

                if (marketingDept != null) {
                    createJob("Digital Marketing Specialist", "Execute digital marketing campaigns", marketingDept, "Austin", new BigDecimal("65000"), Job.JobType.Remote, Job.JobStatus.Active);
                    createJob("Content Marketing Manager", "Lead content strategy and creation", marketingDept, "Seattle", new BigDecimal("75000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                    createJob("SEO Specialist", "Optimize website for search engines", marketingDept, "Denver", new BigDecimal("68000"), Job.JobType.Remote, Job.JobStatus.Active);
                }

                if (itDept != null) {
                    createJob("Full Stack Developer", "Develop and maintain web applications", itDept, "San Francisco", new BigDecimal("95000"), Job.JobType.Remote, Job.JobStatus.Active);
                    createJob("DevOps Engineer", "Manage infrastructure and deployment pipelines", itDept, "New York", new BigDecimal("105000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                    createJob("QA Engineer", "Ensure software quality through testing", itDept, "Austin", new BigDecimal("75000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                    createJob("Project Manager", "Lead technical projects and teams", itDept, "Chicago", new BigDecimal("90000"), Job.JobType.Hybrid, Job.JobStatus.Active);
                }

                if (financeDept != null) {
                    createJob("Financial Analyst", "Analyze financial data and create reports", financeDept, "New York", new BigDecimal("70000"), Job.JobType.Office, Job.JobStatus.Active);
                    createJob("Accountant", "Handle accounting and bookkeeping tasks", financeDept, "Dallas", new BigDecimal("58000"), Job.JobType.Office, Job.JobStatus.Active);
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
                        createHoliday("Summer Break", LocalDate.of(currentYear, 7, 15), "Company Holiday"),
                        createHoliday("Spring Festival", LocalDate.of(currentYear, 4, 20), "Company Event"),
                        createHoliday("Team Building Day", LocalDate.of(currentYear, 6, 10), "Company Event")
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

    private void initializePayrolls() {
        try {
            if (payrollRepository.count() == 0) {
                List<Employee> employees = employeeRepository.findAll();
                Random random = new Random();

                for (Employee employee : employees) {
                    // Skip HR employee as it might have different salary structure
                    if ("hrms.hr@gmail.com".equals(employee.getEmail())) {
                        continue;
                    }

                    Payroll payroll = new Payroll();
                    payroll.setEmployee(employee);

                    // Set salary based on designation
                    BigDecimal baseSalary = getBaseSalaryByDesignation(employee.getDesignation());
                    payroll.setCtc(baseSalary.multiply(new BigDecimal("1.3"))); // CTC is 30% more than base
                    payroll.setSalaryPerMonth(baseSalary.divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP));

                    // Random deductions (0-10% of monthly salary)
                    BigDecimal deductionPercentage = new BigDecimal(random.nextDouble() * 0.1);
                    payroll.setDeduction(payroll.getSalaryPerMonth().multiply(deductionPercentage).setScale(2, BigDecimal.ROUND_HALF_UP));

                    payroll.setStatus(random.nextBoolean() ? Payroll.PayrollStatus.COMPLETED : Payroll.PayrollStatus.PENDING);

                    payrollRepository.save(payroll);
                }

                log.info("Sample payrolls created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize payrolls: {}", e.getMessage());
        }
    }

    private BigDecimal getBaseSalaryByDesignation(String designation) {
        if (designation == null) return new BigDecimal("60000");

        switch (designation.toLowerCase()) {
            case "senior designer":
            case "design lead":
            case "sales manager":
            case "marketing manager":
            case "project manager":
            case "finance manager":
            case "operations manager":
                return new BigDecimal("85000");
            case "ui/ux designer":
            case "product designer":
            case "account manager":
            case "business development":
            case "devops engineer":
            case "full stack developer":
                return new BigDecimal("75000");
            case "graphic designer":
            case "sales executive":
            case "digital marketer":
            case "software developer":
            case "qa engineer":
            case "financial analyst":
                return new BigDecimal("65000");
            case "sales representative":
            case "content creator":
            case "seo specialist":
            case "accountant":
            case "operations coordinator":
                return new BigDecimal("55000");
            default:
                return new BigDecimal("60000");
        }
    }

    private void initializeLeaves() {
        try {
            if (leaveRepository.count() == 0) {
                List<Employee> employees = employeeRepository.findAll();
                Random random = new Random();

                String[] leaveReasons = {
                        "Annual vacation", "Sick leave", "Personal emergency", "Family event",
                        "Medical appointment", "Mental health day", "Wedding", "Maternity leave",
                        "Conference attendance", "Training program"
                };

                for (Employee employee : employees) {
                    // Create 2-4 leaves per employee
                    int numLeaves = random.nextInt(3) + 2;

                    for (int i = 0; i < numLeaves; i++) {
                        Leave leave = new Leave();
                        leave.setEmployee(employee);

                        // Random start date in the past 6 months or future 3 months
                        LocalDate startDate = LocalDate.now().minusDays(random.nextInt(180)).plusDays(random.nextInt(90));
                        leave.setStartDate(startDate);

                        // End date 1-5 days after start date
                        leave.setEndDate(startDate.plusDays(random.nextInt(5) + 1));

                        leave.setReason(leaveReasons[random.nextInt(leaveReasons.length)]);

                        // Random status
                        Leave.LeaveStatus[] statuses = Leave.LeaveStatus.values();
                        leave.setStatus(statuses[random.nextInt(statuses.length)]);

                        leaveRepository.save(leave);
                    }
                }

                log.info("Sample leaves created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize leaves: {}", e.getMessage());
        }
    }

    private void initializeProjects() {
        try {
            if (projectRepository.count() == 0) {
                List<Employee> employees = employeeRepository.findAll();
                Random random = new Random();

                String[] projectNames = {
                        "Website Redesign", "Mobile App Development", "Customer Portal", "E-commerce Platform",
                        "Data Analytics Dashboard", "Marketing Campaign", "Sales Automation", "Inventory System",
                        "HR Management System", "Financial Reporting Tool", "Social Media Integration",
                        "Payment Gateway", "Security Enhancement", "Performance Optimization"
                };

                for (int i = 0; i < projectNames.length && i < employees.size(); i++) {
                    Project project = new Project();
                    project.setName(projectNames[i]);

                    // Random start date in the past 3 months
                    project.setStartDate(LocalDate.now().minusDays(random.nextInt(90)));

                    // End date 1-6 months after start date
                    project.setEndDate(project.getStartDate().plusDays(random.nextInt(180) + 30));

                    // Random status
                    Project.ProjectStatus[] statuses = Project.ProjectStatus.values();
                    project.setStatus(statuses[random.nextInt(statuses.length)]);

                    // Assign to random employee
                    project.setAssignedTo(employees.get(random.nextInt(employees.size())));

                    projectRepository.save(project);
                }

                log.info("Sample projects created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize projects: {}", e.getMessage());
        }
    }

    private void initializeAttendance() {
        try {
            if (attendanceRepository.count() == 0) {
                List<Employee> employees = employeeRepository.findAll();
                Random random = new Random();

                // Create attendance for the last 30 days
                for (int day = 0; day < 30; day++) {
                    LocalDate date = LocalDate.now().minusDays(day);

                    // Skip weekends
                    if (date.getDayOfWeek().getValue() > 5) {
                        continue;
                    }

                    for (Employee employee : employees) {
                        // 90% chance of attendance
                        if (random.nextDouble() < 0.9) {
                            Attendance attendance = new Attendance();
                            attendance.setEmployee(employee);
                            attendance.setDate(date);

                            // Random check-in time between 8:00 and 10:00
                            LocalDateTime checkIn = date.atTime(8 + random.nextInt(3), random.nextInt(60));
                            attendance.setCheckIn(checkIn);

                            // Check-out 8-9 hours later
                            attendance.setCheckOut(checkIn.plusHours(8 + random.nextInt(2)));

                            // Status based on check-in time
                            attendance.setStatus(checkIn.getHour() <= 9 ?
                                    Attendance.AttendanceStatus.OnTime : Attendance.AttendanceStatus.Late);

                            // Random work type
                            attendance.setWorkType(random.nextBoolean() ?
                                    Attendance.WorkType.Office : Attendance.WorkType.Remote);

                            attendanceRepository.save(attendance);
                        }
                    }
                }

                log.info("Sample attendance records created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize attendance: {}", e.getMessage());
        }
    }

    private void initializeDocuments() {
        try {
            if (documentRepository.count() == 0) {
                List<Employee> employees = employeeRepository.findAll();

                String[] documentTypes = {
                        "Resume", "ID Copy", "Address Proof", "Educational Certificate",
                        "Experience Letter", "Salary Certificate", "Medical Certificate"
                };

                for (Employee employee : employees) {
                    // Create 2-3 documents per employee
                    for (int i = 0; i < 3; i++) {
                        Document document = new Document();
                        document.setName(documentTypes[i % documentTypes.length]);
                        document.setUrl("https://example.com/documents/" + employee.getEmployeeId() + "_" + (i+1) + ".pdf");
                        document.setOwner(employee);
                        document.setPublicId("doc_" + employee.getEmployeeId() + "_" + (i+1));
                        document.setUploadedAt(LocalDateTime.now().minusDays(new Random().nextInt(365)));

                        documentRepository.save(document);
                    }
                }

                log.info("Sample documents created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize documents: {}", e.getMessage());
        }
    }

    private void initializeNotifications() {
        try {
            if (notificationRepository.count() == 0) {
                List<Employee> employees = employeeRepository.findAll();
                Random random = new Random();

                String[] messages = {
                        "Your leave request has been approved",
                        "New project assigned to you",
                        "Payroll processed successfully",
                        "Please update your profile information",
                        "Team meeting scheduled for tomorrow",
                        "Holiday announced for next week",
                        "Performance review due next month",
                        "New company policy update",
                        "Welcome to the company!",
                        "Document uploaded successfully",
                        "Attendance reminder",
                        "Training session available"
                };

                for (Employee employee : employees) {
                    // Create 3-5 notifications per employee
                    int numNotifications = random.nextInt(3) + 3;

                    for (int i = 0; i < numNotifications; i++) {
                        Notification notification = new Notification();
                        notification.setMessage(messages[random.nextInt(messages.length)]);
                        notification.setRecipient(employee);
                        notification.setTimestamp(LocalDateTime.now().minusDays(random.nextInt(30)));
                        notification.setRead(random.nextBoolean());

                        notificationRepository.save(notification);
                    }
                }

                log.info("Sample notifications created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize notifications: {}", e.getMessage());
        }
    }

    private void initializeCandidates() {
        try {
            if (candidateRepository.count() == 0) {
                List<Job> jobs = jobRepository.findAll();
                Random random = new Random();

                String[] candidateNames = {
                        "John Smith", "Sarah Johnson", "Michael Brown", "Emily Davis",
                        "David Wilson", "Jessica Miller", "Chris Anderson", "Amanda Taylor",
                        "Matthew Thomas", "Ashley Jackson", "Daniel White", "Jennifer Harris",
                        "Anthony Garcia", "Lisa Martinez", "Kevin Rodriguez", "Michelle Lewis",
                        "Brian Lee", "Nicole Walker", "Jason Hall", "Rachel Allen",
                        "William Young", "Stephanie King", "Mark Wright", "Laura Lopez"
                };

                String[] phoneNumbers = {
                        "+1555123456", "+1555234567", "+1555345678", "+1555456789",
                        "+1555567890", "+1555678901", "+1555789012", "+1555890123",
                        "+1555901234", "+1555012345", "+1555112233", "+1555223344"
                };

                for (Job job : jobs) {
                    // Create 2-4 candidates per job
                    int numCandidates = random.nextInt(3) + 2;

                    for (int i = 0; i < numCandidates; i++) {
                        String candidateName = candidateNames[random.nextInt(candidateNames.length)];

                        Candidate candidate = new Candidate();
                        candidate.setName(candidateName);
                        candidate.setEmail(candidateName.toLowerCase().replace(" ", ".") +
                                random.nextInt(100) + "@email.com");
                        candidate.setPhoneNumber(phoneNumbers[random.nextInt(phoneNumbers.length)]);
                        candidate.setCv_url("https://example.com/cv/" + candidateName.replace(" ", "_") + ".pdf");
                        candidate.setJob_id(job.getId().intValue());

                        // Random status
                        Candidate.ApplicationStatus[] statuses = Candidate.ApplicationStatus.values();
                        candidate.setStatus(statuses[random.nextInt(statuses.length)]);

                        candidate.setApplyDate(LocalDateTime.now().minusDays(random.nextInt(60)));

                        candidateRepository.save(candidate);
                    }
                }

                log.info("Sample candidates created successfully");
            }
        } catch (Exception e) {
            log.error("Failed to initialize candidates: {}", e.getMessage());
        }
    }
}