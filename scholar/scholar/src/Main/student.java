package Main;

import Config.config;
import java.util.*;

public class student {

    private Scanner sc;
    private config conf;
    private int studentId;

  public student(Scanner sc, config conf, int studentId) {
    this.sc = sc;
    this.conf = conf;
    this.studentId = studentId;
}

   

    public void openDashboard() {

        boolean loggedIn = true;

        System.out.println("\n=========================================");
        System.out.println("    WELCOME TO STUDENT SCHOLARSHIP SYSTEM");
        System.out.println("=========================================");

        while (loggedIn) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. View All Scholarships");
            System.out.println("2. Apply for Scholarship");
            System.out.println("3. View My Applications");
            System.out.println("4. Cancel Application");
            System.out.println("5. Logout");
            System.out.print("Enter action: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewScholarships();
                    break;

                case 2:
                    applyScholarship();
                    break;

                case 3:
                    viewMyApplications();
                    break;

                case 4:
                    cancelApplication();
                    break;

                case 5:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewScholarships() {
        String sql = "SELECT * FROM tbl_scholarship";

        String[] headers = {"ID", "Name", "Description", "Status"};
        String[] columns = {"scholarship_id", "scholarship_name", "s_desc", "s_status"};

        conf.viewRecords(sql, headers, columns);
    }

    private void applyScholarship() {

        viewScholarships();

        System.out.print("Enter Scholarship ID to apply: ");
        int sId = sc.nextInt();
        sc.nextLine();

        String check = "SELECT * FROM tbl_application WHERE u_id = ? AND scholarship_id = ?";
        List<Map<String, Object>> existing = conf.fetchRecords(check, studentId, sId);

        if (!existing.isEmpty()) {
            System.out.println("You already applied for this scholarship.");
            return;
        }

        String date = java.time.LocalDate.now().toString();
        String status = "Pending";

        String sql = "INSERT INTO tbl_application (u_id, scholarship_id, date_applied, a_status) VALUES (?, ?, ?, ?)";

        conf.addRecord(sql, studentId, sId, date, status);

        System.out.println("Application submitted successfully!");
    }

    private void viewMyApplications() {

        String sql = "SELECT a.app_id, s.s_name, a.app_date, a.app_status "
                + "FROM tbl_application a "
                + "JOIN tbl_scholarship s ON a.s_id = s.s_id "
                + "WHERE a.user_id = ?";

        List<Map<String, Object>> apps = conf.fetchRecords(sql, studentId);

        System.out.println("\n--- MY APPLICATIONS ---");
        if (apps.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

        for (Map<String, Object> row : apps) {
            System.out.println("Application ID: " + row.get("app_id"));
            System.out.println("Scholarship: " + row.get("s_name"));
            System.out.println("Date Applied: " + row.get("app_date"));
            System.out.println("Status: " + row.get("app_status"));
            System.out.println("------------------------------");
        }
    }

    private void cancelApplication() {

        viewMyApplications();

        System.out.print("Enter Application ID to cancel: ");
        int appId = sc.nextInt();
        sc.nextLine();

        String sql = "DELETE FROM tbl_application WHERE app_id = ? AND user_id = ?";

        conf.updateRecord(sql, appId, studentId);

        System.out.println("Application cancelled successfully.");
    }
}
