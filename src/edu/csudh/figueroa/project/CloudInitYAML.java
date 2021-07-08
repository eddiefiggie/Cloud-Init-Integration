package edu.csudh.figueroa.project;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CloudInitYAML {

    private boolean authorized = false;
    private int employeeNumber;
    private String userName;  // The user's login name

    final private boolean SUDO = true;  // Accepts a sudo rule string
    final private boolean LOCK_PASSWORD = false; // Defaults to true if not made false

    private String sshAuthorizedKeys; // Add keys to user's authorized keys file

    // The hash SHA-512 password
    private String password;

    private String hostName;

    public CloudInitYAML(int employeeNumber, String hostName) {
        authorizationHelper(employeeNumber);

        this.password = hashPassword(password);
        this.hostName = hostName;
    }

    private void authorizationHelper(int employeeNumber) {
        HRMSdatabaseDAO HRMS = new HRMSdatabaseDAO("HRMS-System");

        for(Employee e : HRMS.getAll()) {
            if(employeeNumber == e.getEmployeeID()) {

                this.userName = e.getUserName();
                this.password = e.getPassword();
                this.sshAuthorizedKeys = e.getSSHKey();
                this.authorized = true;
            }
        }
        if(!this.authorized) {
            System.out.println("You entered an invalid employee ID number.");
            System.exit(0);
        }
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getSudo() {
        return this.SUDO;
    }

    public String getSSHAuthorizedKeys() {
        return sshAuthorizedKeys;
    }

    public void setSSHAuthorizedKeys(String sshAuthorizedKeys) {
        this.sshAuthorizedKeys = sshAuthorizedKeys;
    }

    public boolean isLockPassWord() {
        return this.LOCK_PASSWORD;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    private String hashPassword (String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(s.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);

            // Padding for 32 bit
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            // set the hash as password
            return hashText;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateYAML(int vmType) throws FileNotFoundException {

        String outputFileName = "output.yaml";
        PrintWriter yamlOut =  new PrintWriter(outputFileName);

        yamlOut.printf("%s\n", "#cloud-config");
        yamlOut.printf("%s\n", "users:");

        // Username
        yamlOut.printf("  %s", "- name:");
        yamlOut.printf(" %s\n", getUserName());

        // If sudo access is granted, otherwise default
        if (getSudo()) {
            yamlOut.printf("    %s", "sudo:");
            yamlOut.printf("%s\n", " ['ALL=(ALL) NOPASSWD:ALL']");
        }

        // SSH Keys & Password
        yamlOut.printf("    %s\n", "ssh-authorized-keys:");
        yamlOut.printf("%s%s\n", "      - ", getSSHAuthorizedKeys());

        if(!isLockPassWord()) {
            yamlOut.printf("    %s", "lock-passwd: ");
            yamlOut.printf("%s\n", isLockPassWord());
            yamlOut.printf("    %s", "passwd: ");
            yamlOut.printf("%s\n", getPassword());
        }

        yamlOut.println();

        // hostname
        yamlOut.printf("%s", "hostname:");
        yamlOut.printf(" %s\n\n", getHostName());

        // If a base system is requested, yaml is done, otherwise custom add packages for dev tools
        if(vmType == 2) {
            yamlOut.printf("%s\n", "packages:");
            yamlOut.printf("%s\n", "  - gcc-c++");
            yamlOut.printf("%s\n", "  - make");
            yamlOut.printf("%s\n", "  - unzip");
            yamlOut.printf("%s\n", "  - bash-completion");
            yamlOut.printf("%s\n", "  - python-pip");
            yamlOut.printf("%s\n", "  - s3cmd");
            yamlOut.printf("%s\n", "  - stress");
            yamlOut.printf("%s\n", "  - ntupdate");
            yamlOut.printf("%s\n", "  - nodejs");
            yamlOut.printf("%s\n", "  - python36");
            yamlOut.printf("%s\n", "  - python36-steuptools");
            yamlOut.printf("%s\n", "  - jq");

            yamlOut.printf("%s\n", "runcmd:");
            yamlOut.printf("%s\n", "  - npm install -g request express");
            yamlOut.printf("%s\n", "  - systemctl stop firewalld");
            yamlOut.printf("%s\n", "  - /sbin/setenforce 0");
            yamlOut.printf("%s\n", "  - sed -i -e 's/enforcing/disabled/g' /etc/selinux/config");
            yamlOut.printf("%s\n", "  - /bin/python3.6 -m ensurepip");
            yamlOut.printf("%s\n", "  - pip install -U pip");
            yamlOut.printf("%s\n", "  - pip install boto3 python-magic");
            yamlOut.printf("%s\n", "  - ntpdate -u -s 0.pool.ntp.org 1.pool.ntp.org 2.pool.ntp.org 3.pool.ntp.org");
            yamlOut.printf("%s\n", "  - systemctl restart ntpd");

            yamlOut.printf("\n%s\n", "final_message: CentOS 7 Tools Machine setup successfully!");

        }

        yamlOut.printf("%s", "final_message: CentOS 7 Base Machine setup successfully!");

        yamlOut.close();

        System.out.println("Cloud-init YAML file creation successful. Created a new: output.yaml");
    }
}
