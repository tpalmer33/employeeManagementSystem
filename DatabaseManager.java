import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/employees";
        String user = "root";
        String password = "trizZy#981_rw";   
        return DriverManager.getConnection(url, user, password);
    }
    
    public Map<String, String> verifyUser(String empID, String userPassword) {
        Map<String, String> result = new HashMap<>();
        String sqlCommand = "SELECT e.empid, ejt.job_title_id FROM employees e " +
                            "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                            "WHERE e.empid = ? AND e.password = ? ";
        
        try (Connection myConn = getConnection()) {
            PreparedStatement pstmt = myConn.prepareStatement(sqlCommand);
            pstmt.setInt(1,  Integer.parseInt(empID));
            pstmt.setString(2, userPassword);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result.put("empid", rs.getString(1));
                result.put("role", rs.getInt(2) == 1 ? "admin" : "employee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<EmployeeInfo> searchEmployees(String searchTerm, String searchType) {
        ArrayList<EmployeeInfo> results = new ArrayList<>();
        String sqlCommand = "";
        
        try (Connection myConn = getConnection()) {
            switch(searchType) {
                case "Employee ID":
                    sqlCommand = "SELECT e.empid, CONCAT(e.first_name, ' ', e.last_name), a.dob, e.ssn, " +
                    "jt.job_title, d.division_name, p.earnings FROM employees e " +
                    "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                    "LEFT JOIN address a ON e.empid = a.empid " +
                    "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                    "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
                    "LEFT JOIN division d ON ed.div_id = d.id " +
                    "LEFT JOIN payroll p ON e.empid = p.empid " +
                    "WHERE e.empid = ? ";
                    break;
                case "Name":
                    sqlCommand = "SELECT e.empid, CONCAT(e.first_name, ' ', e.last_name), a.dob, e.ssn, " +
                                 "jt.job_title, d.division_name, p.earnings FROM employees e " +
                                 "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                                 "LEFT JOIN address a ON e.empid = a.empid " +
                                 "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                                 "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
                                 "LEFT JOIN division d ON ed.div_id = d.id " +
                                 "LEFT JOIN payroll p ON e.empid = p.empid " +
                                 "WHERE CONCAT(e.first_name, ' ', e.last_name) LIKE ? ";
                    break;
                case "DOB":
                    sqlCommand = "SELECT e.empid, CONCAT(e.first_name, ' ', e.last_name), a.dob, e.ssn, " +
                                 "jt.job_title, d.division_name, p.earnings FROM employees e " +
                                 "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                                 "LEFT JOIN address a ON e.empid = a.empid " +
                                 "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                                 "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
                                 "LEFT JOIN division d ON ed.div_id = d.id " +
                                 "LEFT JOIN payroll p ON e.empid = p.empid " +
                                 "WHERE a.dob = ? ";
                    break;
                case "SSN":
                    sqlCommand = "SELECT e.empid, CONCAT(e.first_name, ' ', e.last_name), a.dob, e.ssn, " +
                                 "jt.job_title, d.division_name, p.earnings FROM employees e " +
                                 "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                                 "LEFT JOIN address a ON e.empid = a.empid " +
                                 "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                                 "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
                                 "LEFT JOIN division d ON ed.div_id = d.id " +
                                 "LEFT JOIN payroll p ON e.empid = p.empid " +
                                 "WHERE e.ssn = ? ";
                    break;
            }
            
            PreparedStatement pstmt = myConn.prepareStatement(sqlCommand);
            pstmt.setString(1, searchType.equals("Name") ? "%" + searchTerm + "%" : searchTerm);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                results.add(new EmployeeInfo(
                    rs.getInt(1), 
                    rs.getString(2), 
                    rs.getString(4), // SSN
                    rs.getString(3), 
                    rs.getString(5), 
                    rs.getString(6), 
                    rs.getDouble(7), 
                    ""
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean addEmployee(EmployeeInfo employee) {
        String sqlCommand = "INSERT INTO employees (empid, first_name, last_name, password, ssn) " +
                            "VALUES (?, ?, ?, ?, ?) ";
        String sqlCommand2 = "INSERT INTO address (empid, dob) " + 
                            "VALUES (?, ?)";
        
        try (Connection myConn = getConnection()) {
            PreparedStatement pstmt = myConn.prepareStatement(sqlCommand);
            String[] names = employee.getName().split(" ");
            
            pstmt.setInt(1, employee.getEmpId());
            pstmt.setString(2, names[0]);
            pstmt.setString(3, names.length > 1 ? names[1] : "");
            pstmt.setString(4, "defaultPassword"); // Set default password
            pstmt.setString(5, employee.getSsn());
            
            PreparedStatement pstmt2 = myConn.prepareStatement(sqlCommand2);
            pstmt2.setInt(1, employee.getEmpId());
            pstmt2.setString(2, employee.getDob());
            
            return pstmt.executeUpdate() > 0 && pstmt2.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEmployee(EmployeeInfo employee) {
        String sqlCommand = "UPDATE employees SET first_name = ?, last_name = ?, ssn = ? WHERE empid = ?";
        String sqlCommand2 = "UPDATE address SET dob = ? WHERE empid = ?"; 
        
        try (Connection myConn = getConnection()) {
            PreparedStatement pstmt = myConn.prepareStatement(sqlCommand);
            String[] names = employee.getName().split(" ");
            
            pstmt.setString(1, names[0]);
            pstmt.setString(2, names.length > 1 ? names[1] : "");
            pstmt.setString(3, employee.getSsn());
            pstmt.setInt(4, employee.getEmpId());

            PreparedStatement pstmt2 = myConn.prepareStatement(sqlCommand2);
            pstmt2.setString(1, employee.getDob());
            pstmt2.setInt(2, employee.getEmpId());

            return pstmt.executeUpdate() > 0 && pstmt2.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSalary(int empId, double newSalary) {
        String sqlCommand = "UPDATE payroll SET earnings = ? WHERE empid = ?";
        
        try (Connection myConn = getConnection()) {
            PreparedStatement pstmt = myConn.prepareStatement(sqlCommand);
            pstmt.setDouble(1, newSalary);
            pstmt.setInt(2, empId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmployee(int empId) {
        try (Connection myConn = getConnection()) {
            // First delete from dependent tables
            String[] tables = {"employee_job_titles", "employee_division", "payroll", "employees"};
            
            for (String table : tables) {
                String sqlCommand = "DELETE FROM " + table + " WHERE empid = ?";
                PreparedStatement pstmt = myConn.prepareStatement(sqlCommand);
                pstmt.setInt(1, empId);
                pstmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> generatePayReport(String reportType) {
        ArrayList<String> reportLines = new ArrayList<>();
        String sqlCommand = "";
        
        try (Connection myConn = getConnection()) {
            if (reportType.equals("Division")) {
                sqlCommand = "SELECT d.division_name, SUM(p.earnings) as total_pay " +
                            "FROM payroll p " +
                            "JOIN employee_division ed ON p.empid = ed.empid " +
                            "JOIN division d ON ed.div_id = d.id " +
                            "GROUP BY d.division_name";
            } else {
                sqlCommand = "SELECT jt.job_title, SUM(p.earnings) as total_pay " +
                            "FROM payroll p " +
                            "JOIN employee_job_titles ejt ON p.empid = ejt.empid " +
                            "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                            "GROUP BY jt.job_title";
            }
            
            Statement stmt = myConn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);
            
            // Add column headers
            reportLines.add(String.format("%-30s %-15s", reportType, "Total Pay"));
            reportLines.add("----------------------------------------");
            
            // Add data rows
            while (rs.next()) {
                reportLines.add(String.format("%-30s $%-15.2f", 
                    rs.getString(1), 
                    rs.getDouble(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportLines;
    }

    public ArrayList<String> getEmployeePayHistory(String empId) {
        ArrayList<String> reportLines = new ArrayList<>();
        String sqlCommand = "SELECT pay_date, earnings FROM payroll WHERE empid = ? ORDER BY pay_date DESC";
        
        try (Connection myConn = getConnection()) {
            PreparedStatement pstmt = myConn.prepareStatement(sqlCommand);
            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();
            
            // Add column headers
            reportLines.add(String.format("%-15s %-15s", "Pay Date", "Earnings"));
            reportLines.add("--------------------------");
            
            // Add data rows
            while (rs.next()) {
                reportLines.add(String.format("%-15s $%-15.2f", 
                    rs.getDate(1).toString(), 
                    rs.getDouble(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportLines;
    }
}