/* This class will store the  details of an employee in an object for future use*/
public class EmployeeInfo {
    private int empId;
    private String name;
    private String ssn;
    private String dob;
    private String jobTitle;
    private String division;
    private double salary;
    private String payStatement;

    public EmployeeInfo(int empId, String name, String ssn, String dob, String jobTitle, String division, double salary, String payStatement) {
        this.empId = empId;
        this.name = name;
        this.ssn = ssn;
        this.dob = dob;
        this.jobTitle = jobTitle;
        this.division = division;
        this.salary = salary;
        this.payStatement = payStatement;
    }

    public int getEmpId() { return empId; }
    public String getName() { return name; }
    public String getSsn() { return ssn; }
    public String getDob() { return dob; }
    public String getJobTitle() { return jobTitle; }
    public String getDivision() { return division; }
    public double getSalary() { return salary; }
    public String getPayStatement() { return payStatement; }

    public void setName(String name) { this.name = name; }
    public void setSsn(String ssn) { this.ssn = ssn; }
    public void setDob(String dob) { this.dob = dob; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setDivision(String division) { this.division = division; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setPayStatement(String payStatement) { this.payStatement = payStatement; }

}
