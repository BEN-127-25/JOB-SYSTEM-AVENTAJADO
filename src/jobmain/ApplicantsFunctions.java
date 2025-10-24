package jobmain;

import config.config;
import java.util.Scanner;

public class ApplicantsFunctions {
    
     static Scanner sc = new Scanner(System.in);
    static config database = new config();
    
    public static void viewAvailableJobs() {
        String sql = "SELECT * FROM tbl_jobs";
        String[] headers = {"JobID", "Title", "Experience", "DatePosted"};
        String[] columns = {"JobID", "Title", "Experience", "DatePosted"};
        database.viewRecords(sql, headers, columns);
    }

    public static void applyForJob(int applicantId) {
        System.out.print("Enter Job ID to apply: ");
        while (!sc.hasNextInt()) {      
            System.out.print("Invalid input! Please enter a valid Job ID: ");
            sc.next();
        }
        int jobId = sc.nextInt();
        sc.nextLine(); // clear newline

        String sql = "INSERT INTO tbl_applications (ApplicantID, JobID, Status) VALUES (?, ?, 'Pending')";
        database.addRecord(sql, applicantId, jobId);
        System.out.println("✅ Application submitted!");
    }

    public static void viewMyApplications(int applicantId) {
        String sql = "SELECT * FROM tbl_applications WHERE ApplicantID = ?";
        String[] headers = {"ApplicationID", "JobID", "Status", "DateApplied"};
        String[] columns = {"ApplicationID", "JobID", "Status", "DateApplied"};
        database.viewRecords(sql, headers, columns);
    }

    public static void viewMyInterviews(int applicantId) {
        String sql = "SELECT * FROM tbl_interviews WHERE ApplicantID = ?";
        String[] headers = {"InterviewID", "Date", "Time", "Status"};
        String[] columns = {"InterviewID", "Date", "Time", "Status"};
        database.viewRecords(sql, headers, columns);
    }

    public static void updateProfile(int applicantId) {
        sc.nextLine(); // clear buffer before reading strings
        System.out.print("Enter new email: ");
        String email = sc.nextLine();
        System.out.print("Enter new phone: ");
        String phone = sc.nextLine();

        String sql = "UPDATE tbl_approved SET Email = ?, Phone = ? WHERE ID = ?";
        database.updateRecord(sql, email, phone, applicantId);
        System.out.println("✅ Profile updated successfully!");
    }

    public static void deleteApplication(int applicantId) {
        System.out.print("Enter Application ID to withdraw: ");
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input! Please enter a valid Application ID: ");
            sc.next();
        }
        int appId = sc.nextInt();
        sc.nextLine(); // clear newline

        String sql = "DELETE FROM tbl_applications WHERE ApplicationID = ? AND ApplicantID = ?";
        database.deleteRecord(sql, appId, applicantId);
        System.out.println("🗑 Application withdrawn successfully!");
    }
}
