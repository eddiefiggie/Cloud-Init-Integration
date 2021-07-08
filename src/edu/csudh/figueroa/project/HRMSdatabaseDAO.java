package edu.csudh.figueroa.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HRMSdatabaseDAO implements DAO<Employee>{

    private List<Employee> employees = new ArrayList<>();

    public HRMSdatabaseDAO(String path) {
        try {
            CSVHandler csvData = new CSVHandler(path, ',', 3);
            int data = csvData.getData().size();
            int count = 0;

            while(count < data) {

                String userName = csvData.getData().get(count++);
                int empID = Integer.parseInt(csvData.getData().get(count++));
                String password = csvData.getData().get(count++);
                String SSHKey = csvData.getData().get(count++);


                Employee employee = new Employee(userName, empID, password, SSHKey);
                this.employees.add(employee);
            }
        }
        catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    @Override
    public Optional<Employee> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Employee> getAll() {
        return employees;
    }

    @Override
    public void save(Employee employee, String path) {

    }

    @Override
    public void update(Employee employee, String[] params) {

    }

    @Override
    public void delete(Employee employee, String path) {

    }
}
