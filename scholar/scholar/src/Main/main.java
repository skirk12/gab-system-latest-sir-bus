package Main;

import Config.config;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class main {

    public static void viewUsers() {
        String Query = "SELECT * FROM tbl_users";
        String[] headers = {"ID", "Name", "Contact", "Email", "Type", "Status"};
        String[] columns = {"u_id", "u_name", "u_contact", "u_email", "u_type", "u_status"};

        config conf = new config();
        conf.viewRecords(Query, headers, columns);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        config conf = new config();

        config.connectDB();

        int choice;
        char cont;

        do {
            System.out.println("===== MAIN MENU =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter email: ");
                    String em = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String pas = sc.nextLine();

                    String qry = "SELECT * FROM tbl_users WHERE u_email = ? AND u_pass = ?";
                    List<Map<String, Object>> result = conf.fetchRecords(qry, em, pas);

                    if (result.isEmpty()) {
                        System.out.println("INVALID CREDENTIALS");
                        break;
                    }

                    Map<String, Object> user = result.get(0);

                    String stat = user.get("u_status").toString();
                    String type = user.get("u_type").toString();

                    if (stat.equals("Pending")) {
                        System.out.println("Your account is still pending. Contact Admin!");
                        break;
                    }

                    System.out.println("LOGIN SUCCESS!");

                    if (type.equals("Admin")) {
                       
    System.out.println("WELCOME TO ADMIN DASHBOARD");

    admin adminDash = new admin(sc, conf);
    adminDash.openDashboard();



                    } else if (type.equals("Student")) {

    int studentId = Integer.parseInt(user.get("u_id").toString());

    student sd = new student(sc, conf, studentId);
    sd.openDashboard();
} 

                    break;

                case 2:
                    System.out.print("Enter user name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Contact Number: ");
                    String contact = sc.nextLine();

                    System.out.print("Enter user email: ");
                    String email = sc.nextLine();

                    List<Map<String, Object>> checkEmail;

                    do {
                        checkEmail = conf.fetchRecords("SELECT * FROM tbl_users WHERE u_email = ?", email);
                        if (!checkEmail.isEmpty()) {
                            System.out.print("Email already exists, enter another email: ");
                            email = sc.nextLine();
                        }
                    } while (!checkEmail.isEmpty());

                    int typeInt;
                    do {
                        System.out.print("Enter user Type (1 - Admin / 2 - Student): ");
                        typeInt = sc.nextInt();
                        sc.nextLine();
                    } while (typeInt < 1 || typeInt > 2);

                    String tp = (typeInt == 1) ? "Admin" : "Student";

                    System.out.print("Enter Password: ");
                    String pass = sc.nextLine();

                    String insSQL = "INSERT INTO tbl_users (u_name, u_contact, u_email, u_type, u_status, u_pass) VALUES (?, ?, ?, ?, ?, ?)";
                    conf.addRecord(insSQL, name, contact, email, tp, "Pending", pass);

                    break;

                case 3:
                    System.out.println("Program terminated.");
                    System.exit(0);

                default:
                    System.out.println("Invalid option.");
            }

            System.out.print("Do you want to continue? (Y/N): ");
            cont = sc.next().charAt(0);
            sc.nextLine();

        } while (cont == 'Y' || cont == 'y');

        System.out.println("Thank you! Program ended.");
    }
}
