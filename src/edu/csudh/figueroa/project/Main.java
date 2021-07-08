package edu.csudh.figueroa.project;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        int employeeID;
        String hostName;
        int vmType;

        // Simulate ITSM System to generate work ticket for VM provisioning
        Scanner input = new Scanner(System.in);
        System.out.printf("\n\n%s\n\n", "---===   Simulated ITSM VM Ticket Creation   ===---");
        System.out.print("Enter your employee ID: ");
        employeeID = input.nextInt();
        System.out.print("Enter the host name for the VM: ");
        hostName = input.next();
        System.out.println("Indicate the nature of this VM: ");
        System.out.println("     [1] CentOS 7 Base Machine");
        System.out.println("     [2] CentOS 7 Tools Machine");
        System.out.print("Selection: ");
        vmType = input.nextInt();

        // Generate cloud-init YAML with customized features & user authentication
        CloudInitYAML yaml = new CloudInitYAML(employeeID, hostName);
        yaml.generateYAML(vmType);
    }
}
