package jobmain;

import config.config;
import java.util.Scanner;

public class AdminDashboard {

    static Scanner sc = new Scanner(System.in);
    static config database = new config();

    // ===========================
    // VIEW FUNCTIONS
    // ===========================

    public static void viewUsers() {
        String query = "SELECT * FROM tbl_users";
        String[] headers = {"ID", "Name", "Password", "Email", "Phone", "Role", "Status"};
        String[] columns = {"ID", "Name", "Password", "Email", "Phone", "Role", "Status"};
        database.viewRecords(query, headers, columns);
    }

    public static void viewPendingAccounts() {
        String query = "SELECT ID, Name, Email, Phone, Role, Status FROM tbl_users WHERE Status = 'Pending'";
        String[] headers = {"ID", "Name", "Email", "Phone", "Role", "Status"};
        String[] columns = {"ID", "Name", "Email", "Phone", "Role", "Status"};
        database.viewRecords(query, headers, columns);
    }

    public static void viewApprovedApplicants() {
        String query = "SELECT * FROM tbl_approved";
        String[] headers = {"ID", "Name", "Email", "Phone", "Role", "Status"};
        String[] columns = {"ID", "Name", "Email", "Phone", "Role", "Status"};
        database.viewRecords(query, headers, columns);
    }

    public static void viewApplications() {
        String query = "SELECT * FROM tbl_applications";
        String[] headers = {"ApplicationID", "ApplicantID", "Position", "DateApplied", "Status"};
        String[] columns = {"ApplicationID", "ApplicantID", "Position", "DateApplied", "Status"};
        database.viewRecords(query, headers, columns);
    }

    public static void viewInterviews() {
        String query = "SELECT * FROM tbl_interviews";
        String[] headers = {"InterviewID", "ApplicantID", "Date", "Time", "Location", "Status"};
        String[] columns = {"InterviewID", "ApplicantID", "Date", "Time", "Location", "Status"};
        database.viewRecords(query, headers, columns);
    }
    public static void viewRejectedApplicants() {
    String query = "SELECT * FROM tbl_users WHERE Status = 'Rejected' AND Role = 'Applicant'";
    String[] headers = {"ID", "Name", "Email", "Phone", "Role", "Status"};
    String[] columns = {"ID", "Name", "Email", "Phone", "Role", "Status"};
    database.viewRecords(query, headers, columns);
}


    // ===========================
    // ACTION FUNCTIONS
    // ===========================

    public static void managePendingUsers() {
    viewPendingAccounts();

    System.out.print("\nEnter ID of the user to Approve/Reject: ");
    int id = sc.nextInt();
    sc.nextLine(); // consume newline

    char decision;
    do {
        System.out.print("Approve (A) or Reject (R) this user? (A/R): ");
        decision = sc.nextLine().trim().toUpperCase().charAt(0);

        if (decision == 'A') {
            // 1️⃣ Update tbl_users to Approved
            String sqlUpdate = "UPDATE tbl_users SET Status = ? WHERE ID = ?";
            database.updateRecord(sqlUpdate, "Approved", id);

            // 2️⃣ Copy approved user to tbl_approved
            String sqlInsert = "INSERT INTO tbl_approved (ID, Name, Email, Phone, Role, Status) " +
                   "SELECT ID, Name, Email, Phone, Role, Status FROM tbl_users " +
                   "WHERE ID = ? AND Status = 'Approved'";

            database.addRecord(sqlInsert, id);

            System.out.println("✅ User approved and added to tbl_approved!");

        } else if (decision == 'R') {
            // 1️⃣ Update tbl_users to Rejected
            String sqlUpdate = "UPDATE tbl_users SET Status = ? WHERE ID = ?";
            database.updateRecord(sqlUpdate, "Rejected", id);

            // 2️⃣ Copy rejected user to tbl_rejected
            String sqlInsert = "INSERT INTO tbl_rejected (ID, Name, Email, Phone, Role, Status) " +
                               "SELECT ID, Name, Email, Phone, Role, Status FROM tbl_users " +
                               "WHERE ID = ? AND Status = 'Rejected' AND Role = 'Applicant'";
            database.addRecord(sqlInsert, id);

            System.out.println("❌ User rejected and added to tbl_rejected!");

        } else {
            System.out.println("Invalid choice! Please enter 'A' or 'R'.");
        }

    } while (decision != 'A' && decision != 'R');
}



    // ===========================
    // ADMIN DASHBOARD MENU
    // ===========================

    public static void showMenu() {
        int choice = -1;
        char cont;

        do {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. Approve/Reject Pending Accounts");
            System.out.println("2. View All Users");
            System.out.println("3. View Approved Applicants");
            System.out.println("4. View Rejected Applicants");
            System.out.println("5. View Applications");
            System.out.println("6. View Interviews");
            System.out.println("7. UPDATE APPLICANTS");
            System.out.println("8. DELETE APPLICANTS");
            System.out.println("9. Logout / Exit");
            
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input! Please enter a number (1-9): ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    managePendingUsers();
                    break;
                case 2:
                    viewUsers();
                    break;
                case 3:
                    viewApprovedApplicants();
                    break;
                case 4:
                    viewRejectedApplicants();
                    break;
                case 5:
                   viewApplications();
                    break;
                case 6:
                    viewInterviews();
                    break;
                case 7:
                    UserFunctions.updateUser();
                    break;
                case 8:
                   UserFunctions.deleteUser();
                    break;
                              
                case 9:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Please select 1-9.");
            }

            System.out.print("Do you want to continue? (Y/N): ");
            cont = sc.nextLine().trim().toUpperCase().charAt(0);

        } while (cont == 'Y');

        System.out.println("Returning to main menu...");
    }
}
