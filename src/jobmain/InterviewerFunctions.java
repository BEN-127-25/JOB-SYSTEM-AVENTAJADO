package jobmain;

import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InterviewerFunctions {
    static config database = new config();
    static Scanner sc = new Scanner(System.in);

    // ==========================
    // 1️⃣ View All Applicants
    // ==========================
    public static void viewApplicants() {
        System.out.println("\n=== VIEW ALL APPLICANTS ===");

        // Show only approved applicants to interviewers
        String query = "SELECT ID, Name, Email, Phone, Role, Status FROM tbl_users WHERE Role = 'Applicant' AND Status = 'Approved'";

        List<Map<String, Object>> records = database.fetchRecords(query);

        if (records == null || records.isEmpty()) {
            System.out.println("⚠ No applicants found.");
            return;
        }

        for (Map<String, Object> row : records) {
            System.out.println("Applicant ID: " + row.get("ID"));
            System.out.println("Name: " + row.get("Name"));
            System.out.println("Email: " + row.get("Email"));
            System.out.println("Phone: " + row.get("Phone"));
            System.out.println("Status: " + row.get("Status"));
            System.out.println("----------------------------------");
        }
    }

    // ==========================
    // 2️⃣ Assign (Accept) Applicant for Interview
    // ==========================
    public static void assignApplicant(int interviewerId) {
        viewApplicants();

        System.out.print("\nEnter Applicant ID to assign for interview: ");
        int applicantId = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Interview Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        System.out.print("Enter Interview Time (e.g., 10:00 AM): ");
        String time = sc.nextLine();

        System.out.print("Enter Interview Location: ");
        String location = sc.nextLine();

        String sql = "INSERT INTO tbl_interviews (ApplicantID, InterviewerID, Date, Time, Location, Status) VALUES (?, ?, ?, ?, ?, 'Scheduled')";
        database.addRecord(sql, applicantId, interviewerId, date, time, location);

        System.out.println("✅ Interview scheduled successfully!");
    }

    // ==========================
    // 3️⃣ View Scheduled Interviews
    // ==========================
  public static void viewMyInterviews(int interviewerId) {
    System.out.println("\n=== MY SCHEDULED INTERVIEWS ===");

    // SQL query to get interviews for the specific interviewer (with parameter placeholder)
    String sqlQuery = "SELECT InterviewID, ApplicantID, Date, Time, Location, Status FROM tbl_interviews WHERE InterviewerID = ?";

    // Define column headers (these will be printed in the output)
    String[] columnHeaders = {"InterviewID", "ApplicantID", "Date", "Time", "Location", "Status"};

    // Define column names that correspond to the ResultSet's column names
    String[] columnNames = {"InterviewID", "ApplicantID", "Date", "Time", "Location", "Status"};

    // Since viewRecords does not currently accept parameters, we need to set the interviewer's ID in the query directly
    try (Connection conn = config.connectDB(); 
         PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

        // Set the interviewerId parameter
        pstmt.setInt(1, interviewerId);

        // Execute the query and get the result set
        try (ResultSet rs = pstmt.executeQuery()) {

            // Call the existing viewRecords method to display the result set
            StringBuilder headerLine = new StringBuilder();
            headerLine.append("--------------------------------------------------------------------------------\n| ");
            for (String header : columnHeaders) {
                headerLine.append(String.format("%-20s | ", header));
            }
            headerLine.append("\n--------------------------------------------------------------------------------");

            System.out.println(headerLine.toString());

            // Iterate through result set and print each row
            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-20s | ", value != null ? value : ""));
                }
                System.out.println(row.toString());
            }
            System.out.println("--------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error retrieving interview records: " + e.getMessage());
        }

    } catch (SQLException e) {
        System.out.println("Error setting up query: " + e.getMessage());
    }
}


    // ==========================
    // 4️⃣ Update Interview Status
    // ==========================
    public static void updateInterviewStatus(int interviewerId) {
        viewMyInterviews(interviewerId);

        System.out.print("\nEnter Interview ID to update: ");
        int interviewId = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter new status (e.g., Completed, Canceled, Rescheduled): ");
        String status = sc.nextLine();

        String sql = "UPDATE tbl_interviews SET Status = ? WHERE InterviewID = ? AND InterviewerID = ?";
        database.updateRecord(sql, status, interviewId, interviewerId);

        System.out.println("✅ Interview status updated successfully!");
    }

    // ==========================
    // 5️⃣ Delete Interview Record
    // ==========================
    public static void deleteInterview(int interviewerId) {
        viewMyInterviews(interviewerId);

        System.out.print("\nEnter Interview ID to delete: ");
        int interviewId = sc.nextInt();

        String sql = "DELETE FROM tbl_interviews WHERE InterviewID = ? AND InterviewerID = ?";
        database.deleteRecord(sql, interviewId, interviewerId);

        System.out.println("🗑 Interview deleted successfully!");
    }
    
public static void addJob(int interviewerId) {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter Job Title: ");
    String title = sc.nextLine();

    System.out.print("Enter Experience required (in years): ");
    int experience = sc.nextInt();
    sc.nextLine();  // Consume the newline character

    System.out.print("Enter Date Posted (YYYY-MM-DD): ");
    String datePosted = sc.nextLine();

    // SQL query to insert the job into the table
    String sql = "INSERT INTO tbl_jobs (Title, Experience, DatePosted, AddedBy) VALUES (?, ?, ?, ?)";

    try (Connection conn = config.connectDB(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, title);
        pstmt.setInt(2, experience);
        pstmt.setString(3, datePosted);
        pstmt.setInt(4, interviewerId);  // Store who added the job

        // Execute the update to insert the job
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("✅ Job added successfully!");
        } else {
            System.out.println("❌ Error adding job. Please try again.");
        }

    } catch (SQLException e) {
        System.out.println("❌ Error adding job: " + e.getMessage());
        e.printStackTrace();
    }
}




    public static void viewJobs() {
        String sql = "SELECT * FROM tbl_job";
        try {
            List<Map<String, Object>> jobs = database.fetchRecords(sql);
            System.out.println("\n=== JOB LISTINGS ===");
            System.out.printf("%-5s %-20s %-30s %-15s\n", "ID", "Title", "Description", "Location");
            for (Map<String, Object> job : jobs) {
                System.out.printf("%-5s %-20s %-30s %-15s\n",
                        job.get("ID"),
                        job.get("Title"),
                        job.get("Description"),
                        job.get("Location"));
            }
        } catch (Exception e) {
            System.err.println("❌ Error fetching jobs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateJob() {
        viewJobs();
        System.out.print("Enter Job ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter New Job Title: ");
        String title = sc.nextLine();
        System.out.print("Enter New Job Description: ");
        String description = sc.nextLine();
        System.out.print("Enter New Job Location: ");
        String location = sc.nextLine();

        String sql = "UPDATE tbl_job SET Title = ?, Description = ?, Location = ? WHERE ID = ?";
        try {
            database.updateRecord(sql, title, description, location, id);
            System.out.println("✅ Job updated successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error updating job: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteJob() {
        viewJobs();
        System.out.print("Enter Job ID to Delete: ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline

        String sql = "DELETE FROM tbl_job WHERE ID = ?";
        try {
            database.deleteRecord(sql, id);
            System.out.println("🗑️ Job deleted successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error deleting job: " + e.getMessage());
            e.printStackTrace();
        }
    }

}