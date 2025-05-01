# employeeManagementSystem
LOGIN PAGE
This project generates a JFrame displaying the login screen for our employee management system. The user is shown two text boxes prompting them to enter valid login credentials. With valid credentials, the user will be sent to one of two pages based on their credentials, otherwise an "invalid credentials" prompt will show. If the password = "defaultPassword" (user's first login), a dialog will prompt user to enter a new password twice; if they match, the password will be created and the dialog will be disposed.

ADMIN / EMPLOYEE PAGE
If the user has admin status (they have a 1000+ job_title_id in the database), they will be sent to the admin page. If they have employee status (they have a job_title_id 0000-0999), they will be sent to the employee page, which will show their personal information(ineditable) (This is another version of the "SHOW EMPLOYEE DETAILS" page). The admin page consists of three buttons: "search employees", "add new employee", "generate reports". 

SEARCH EMPLOYEES PAGE
The "search employees" button generates a page with a JComboBox specifying the search criteria and a text box to enter details of the employee the user wants to find. When the search button is pressed, a list of employees that match the criteria are shown. The user can then click on a desired employee and the "view details" button will generate a new page.

SHOW EMPLOYEE DETAILS
This page shows the employee's information, which can be edited with admin permissions. The text boxes can be edited and the new information will be saved into the database with the "update details" button. The user can also click the "adjust salary" button, which produces a new window. This prompts the user to select increase or decrease, and a percentage to adjust the salary by. Once saved, this info is updated on the screen and the database. The page also has a "delete employee" button, which prompts a verification of deletion, then deletes the employee from the database.

ADD NEW EMPLOYEE
This page prompts the user to enter employee details, then click the "add employee" button, which will add a new employee into the database with those details.

GENERATE REPORTS
This page contains a drop-down menu with "by division" and "by job title" which specifies the grouping of the data shown. According to this grouping the "generate report" button will show the total payroll for each title.

PAGE FUNCTIONALITY
Each page implements a back or logout button that sends the user back to the previous page. 
The pages are not resizable.

USER FUNCTIONALITY
New employees will be added with "defaultPassword" as the initial password. Upon initial login, they will be able to create a unique password.