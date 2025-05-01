import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;

public class App extends JFrame implements ActionListener {
    private DatabaseManager dbManager = new DatabaseManager();
    private String currentUser;
    private boolean isAdmin;
    
    
    public App() {
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Login();
    }

    public void Login() {
        getContentPane().removeAll();

        /* Panel for title */
        JPanel titlePanel = new JPanel();
        
        JButton backButton = new JButton("Exit");
        backButton.setBounds(10, 10, 70, 30);
        backButton.addActionListener(e -> System.exit(0));
        add(backButton);
        
        JLabel portal = new JLabel("Employee Management System");

        titlePanel.add(portal);

        /* Panel for login credentials */
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 0, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(70, 150, 70, 150));
        
        JLabel username = new JLabel("Employee ID:");
        JTextField tusername = new JTextField();

        inputPanel.add(username);
        inputPanel.add(tusername);

        JLabel password = new JLabel("Password:");
        JPasswordField tpassword = new JPasswordField();

        inputPanel.add(password);
        inputPanel.add(tpassword);

        /* Panel with login button */
        JPanel loginPanel = new JPanel();

        JButton login = new JButton("Login");
        login.addActionListener(e -> {
            // Call method to verify user credentials
            Map<String, String> authResult = dbManager.verifyUser(tusername.getText(), new String(tpassword.getPassword()));
            
            if (!authResult.isEmpty()) {
                currentUser = authResult.get("empid");
                isAdmin = "admin".equals(authResult.get("role"));
                
                if (isAdmin) {
                    AdminPage();
                } else if (dbManager.isNewUser(Integer.parseInt(authResult.get("empid")))) {
                    showPasswordAdjustmentDialog(currentUser);
                } else {
                    showEmployeeDetails(currentUser, false);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", 
                                            "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        loginPanel.add(login);

        /* Add panels */
        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(loginPanel, BorderLayout.SOUTH);

        setSize(600, 300);
        setResizable(false);
        setVisible(true);
        revalidate();
        repaint();
    }

    public void AdminPage() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        /* Construct header panel with components */
        JPanel headerPanel = new JPanel();

        JLabel adminHeader = new JLabel("Admin Portal");
        adminHeader.setFont(new Font(adminHeader.getFont().getName(), Font.BOLD,18));
        
        headerPanel.add(adminHeader);

        /* Construct option panel with components */
        JPanel optionPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        optionPanel.setBorder(BorderFactory.createEmptyBorder(100, 150, 100, 150));

        // Admin components        
        JButton searchEmp = new JButton("Search Employees");
        searchEmp.addActionListener(e -> searchEmployeesPage());
        

        JButton addEmp = new JButton("Add New Employee");
        addEmp.addActionListener(e -> addNewEmployeePage());
        

        JButton genReport = new JButton("Generate Reports");
        genReport.addActionListener(e -> generateReportsPage());
    
        
        optionPanel.add(searchEmp);
        optionPanel.add(addEmp);
        optionPanel.add(genReport);

        /* Construct panel with logout button */
        JPanel backPanel = new JPanel();

        JButton backButton = new JButton("Logout");
        backButton.addActionListener(e -> Login());
        backPanel.add(backButton);     
        
        /* Add panels to JFrame */
        add(headerPanel, BorderLayout.NORTH);
        add(optionPanel, BorderLayout.CENTER);
        add(backPanel, BorderLayout.SOUTH);

        setSize(600, 500);
        setResizable(false);
        setVisible(true);
        revalidate();
        repaint();
    }


    public void searchEmployeesPage() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Search criteria
        JPanel searchPanel = new JPanel();
        JButton backButton = new JButton("Back");
        JTextField searchField = new JTextField(20);
        JComboBox<String> searchType = new JComboBox<>(new String[]{"Employee ID", "Name", "DOB", "SSN"});
        JButton searchButton = new JButton("Search");
        
        // Back button
        backButton.addActionListener(e -> AdminPage());
        searchPanel.add(backButton);
        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchType);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Results table
        JTable resultsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        
        searchButton.addActionListener(e -> {
            /* Call function to get matching employees */
            ArrayList<EmployeeInfo> results = dbManager.searchEmployees(searchField.getText(), 
                (String)searchType.getSelectedItem());
            
            // Convert to table model
            Object[][] data = new Object[results.size()][4];
            for (int i = 0; i < results.size(); i++) {
                EmployeeInfo emp = results.get(i);
                data[i][0] = emp.getEmpId();
                data[i][1] = emp.getName();
                data[i][2] = emp.getDob();
                data[i][3] = emp.getSsn();
            }
            
            resultsTable.setModel(new javax.swing.table.DefaultTableModel(data,new String[]{"ID", "Name", "DOB", "SSN"}));
        });
        
        // View details button
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int empId = (Integer)resultsTable.getValueAt(selectedRow, 0);
                showEmployeeDetails(String.valueOf(empId), true);
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        
        setSize(800, 400);
        setVisible(true);
        revalidate();
        repaint();
    }

    public void showEmployeeDetails(String empId, boolean isAdminView) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Get employee details
        ArrayList<EmployeeInfo> employee = dbManager.searchEmployees(empId, "Employee ID");
        if (employee.isEmpty()) return;
        
        EmployeeInfo emp = employee.get(0);
        
        // Create details panel
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        detailsPanel.add(new JLabel("Employee ID:"));
        JTextField idField = new JTextField(String.valueOf(emp.getEmpId()));
        idField.setEditable(false);
        detailsPanel.add(idField);
        
        detailsPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(emp.getName());
        nameField.setEditable(isAdminView);
        detailsPanel.add(nameField);
        
        detailsPanel.add(new JLabel("Date of Birth:"));
        JTextField dobField = new JTextField(emp.getDob());
        dobField.setEditable(isAdminView);
        detailsPanel.add(dobField);
        
        detailsPanel.add(new JLabel("SSN:"));
        JTextField ssnField = new JTextField(emp.getSsn());
        ssnField.setEditable(isAdminView);
        detailsPanel.add(ssnField);
        
        detailsPanel.add(new JLabel("Job Title:"));
        JTextField jobField = new JTextField(emp.getJobTitle());
        jobField.setEditable(false);
        detailsPanel.add(jobField);
        
        detailsPanel.add(new JLabel("Division:"));
        JTextField divField = new JTextField(emp.getDivision());
        divField.setEditable(false);
        detailsPanel.add(divField);
        
        detailsPanel.add(new JLabel("Current Salary:"));
        JTextField salaryField = new JTextField(String.format("$%.2f", emp.getSalary()));
        salaryField.setEditable(false);
        detailsPanel.add(salaryField);
        
        // Pay history
        JTextArea payHistoryArea = new JTextArea(10, 40);
        payHistoryArea.setEditable(false);
        ArrayList<String> payHistory = dbManager.getEmployeePayHistory(empId);
        for (String line : payHistory) {
            payHistoryArea.append(line + "\n");
        }
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        
        if (isAdminView) {
            // Update button
            JButton updateButton = new JButton("Update Details");
            updateButton.addActionListener(e -> {
                emp.setName(nameField.getText());
                emp.setDob(dobField.getText());
                emp.setSsn(ssnField.getText());
                
                if (dbManager.updateEmployee(emp)) {
                    JOptionPane.showMessageDialog(this, "Employee updated successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(updateButton);
            
            // Salary adjustment
            JButton adjustSalaryButton = new JButton("Adjust Salary");
            adjustSalaryButton.addActionListener(e -> showSalaryAdjustmentDialog(empId, emp.getSalary()));
            buttonPanel.add(adjustSalaryButton);
            
            // Delete employee
            JButton deleteButton = new JButton("Delete Employee");
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (dbManager.deleteEmployee(emp.getEmpId())) {
                        JOptionPane.showMessageDialog(this, "Employee deleted successfully");
                        AdminPage();
                    } else {
                        JOptionPane.showMessageDialog(this, "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(deleteButton);
        } 
        
        // Back button
        JButton backButton = new JButton(isAdminView ? "Back to Search" : "Logout");
        backButton.addActionListener(e -> {
            if (isAdminView) {
                searchEmployeesPage();
            } else {
                Login();
            }
        });
        buttonPanel.add(backButton);
        
        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(payHistoryArea), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setSize(700, 500);
        setVisible(true);
        revalidate();
        repaint();
    }

    private void showPasswordAdjustmentDialog(String empId) {

        JDialog dialog = new JDialog(this, "Create Password", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 20));

        dialog.add(new JLabel("New Password: "));
        JPasswordField newPasswordField = new JPasswordField();
        dialog.add(newPasswordField);

        dialog.add(new JLabel("Confirm Password: "));
        JPasswordField confirmPasswordField = new JPasswordField();
        dialog.add(confirmPasswordField);

        JButton updateButton = new JButton("Create Password");
        updateButton.addActionListener(e -> {
            /* Verify that passwords match */
            if ((new String(newPasswordField.getPassword())).equals(new String(confirmPasswordField.getPassword()))) {
                /* Call function to update password */
                if (dbManager.updatePassword(Integer.parseInt(empId), new String(newPasswordField.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Password updated successfully");
                    dialog.dispose();
                    Login();
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Passwords Do Not Match", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.add(updateButton);
        
        JPanel panel = (JPanel)dialog.getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showSalaryAdjustmentDialog(String empId, double currentSalary) {
        JDialog dialog = new JDialog(this, "Adjust Salary", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        
        dialog.add(new JLabel("Current Salary:"));
        JTextField currentField = new JTextField(String.format("$%.2f", currentSalary));
        currentField.setEditable(false);
        dialog.add(currentField);
        
        dialog.add(new JLabel("Adjustment Amount %:"));
        JTextField amountField = new JTextField();
        dialog.add(amountField);
        
        dialog.add(new JLabel("Adjustment Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Increase", "Decrease"});
        dialog.add(typeCombo);
        
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                double newSalary = typeCombo.getSelectedItem().equals("Increase") 
                    ? currentSalary * (1 + amount/100) 
                    : currentSalary * (1 - amount/100);
                
                if (newSalary <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Salary cannot be zero or negative", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (dbManager.updateSalary(Integer.parseInt(empId), newSalary)) {
                    JOptionPane.showMessageDialog(dialog, "Salary updated successfully");
                    dialog.dispose();
                    showEmployeeDetails(empId, true);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.add(applyButton);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void addNewEmployeePage() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Employee ID:"));
        JTextField idField = new JTextField();
        panel.add(idField);
        
        panel.add(new JLabel("First Name:"));
        JTextField firstNameField = new JTextField();
        panel.add(firstNameField);
        
        panel.add(new JLabel("Last Name:"));
        JTextField lastNameField = new JTextField();
        panel.add(lastNameField);
        
        panel.add(new JLabel("SSN:"));
        JTextField ssnField = new JTextField();
        panel.add(ssnField);

        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        JTextField dobField = new JTextField();
        panel.add(dobField);

        panel.add(new JLabel("Salary:"));
        JTextField salaryField = new JTextField();
        panel.add(salaryField);
        
        JButton saveButton = new JButton("Save Employee");
        saveButton.addActionListener(e -> {
            try {
                EmployeeInfo newEmployee = new EmployeeInfo(
                    Integer.parseInt(idField.getText()),
                    firstNameField.getText() + " " + lastNameField.getText(),
                    ssnField.getText(),
                    dobField.getText(),
                    "", "", Double.parseDouble(salaryField.getText()), ""
                );
                
                if (dbManager.addEmployee(newEmployee)) {
                    JOptionPane.showMessageDialog(this, "Employee added successfully");
                    AdminPage();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add employee", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid employee ID", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> AdminPage());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setSize(500, 300);
        setVisible(true);
    }

    public void generateReportsPage() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        
        // Report type selection
        JPanel typePanel = new JPanel();
        JComboBox<String> reportType = new JComboBox<>(new String[]{"By Division", "By Job Title"});
        JButton generateButton = new JButton("Generate Report");
        
        typePanel.add(new JLabel("Report Type:"));
        typePanel.add(reportType);
        typePanel.add(generateButton);
        
        // Report display
        JTextArea reportArea = new JTextArea(15, 50);
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        
        generateButton.addActionListener(e -> {
            ArrayList<String> report = dbManager.generatePayReport(
                (String)reportType.getSelectedItem()
            );
            
            reportArea.setText("");
            for (String line : report) {
                reportArea.append(line + "\n");
            }
        });
        
        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> AdminPage());
        
        panel.add(typePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);
        
        add(panel);
        setSize(600, 400);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->  new App());
    }
}