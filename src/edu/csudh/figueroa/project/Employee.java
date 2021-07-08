package edu.csudh.figueroa.project;

public class Employee {

    private String userName;
    private int employeeID;
    private String password;
    private String SSHKey;

    public Employee(String userName, int employeeID, String password, String SSHKey) {
        this.userName = userName;
        this.employeeID = employeeID;
        this.password = password;
        this.SSHKey = SSHKey;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String s) {
        this.userName = s;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getSSHKey() {
        return SSHKey;
    }

    public void setSSHKey(String SSHKey) {
        this.SSHKey = SSHKey;
    }
}
