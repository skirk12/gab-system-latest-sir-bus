package Main;

import Config.config;
import java.util.Scanner;

public class admin {
    private Scanner sc;
    private config conf;

    public admin(Scanner sc, config conf) {
        this.sc = sc;
        this.conf = conf;
    }

    public void openDashboard() {
        boolean adminLoggedIn = true;

        while (adminLoggedIn) {
            System.out.println("\n--- ADMIN DASHBOARD ---");
            System.out.println("1. Create Scholarship");
            System.out.println("2. View Scholarships");
            System.out.println("3. Logout");
            System.out.print("Enter Action: ");

            int action = sc.nextInt();
            sc.nextLine();  // consume newline

            switch (action) {
                case 1:
                    createScholarship();
                    break;

                case 2:
                    viewScholarships();
                    break;

                case 3:
                    System.out.println("Logging out...");
                    adminLoggedIn = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void createScholarship() {
        System.out.print("Enter Scholarship Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Scholarship Amount: ");
        int amount = sc.nextInt();
        sc.nextLine(); 
        
        System.out.print("Enter Scholarship Description: ");
        String desc = sc.nextLine();

        System.out.print("Enter Scholarship Status (Open/Closed): ");
        String status = sc.nextLine();

        System.out.print("Enter Application Deadline (YYYY-MM-DD): ");
        String deadline = sc.nextLine();

        String sql = "INSERT INTO tbl_scholarship (scholarship_name, s_amount, s_desc, s_status, s_application_deadline) VALUES (?,?, ?, ?, ?)";
        conf.addRecord(sql, name, amount, desc, status, deadline);

        System.out.println("Scholarship created successfully!");
    }

    private void viewScholarships() {
        String query = "SELECT * FROM tbl_scholarship";
        String[] headers = {"ID", "Name", "Amount", "Status", "Deadline"};
        String[] columns = {"scholarship_id", "scholarship_name", "s_amount", "s_status", "s_application_deadline"};

        conf.viewRecords(query, headers, columns);
    }
}
    