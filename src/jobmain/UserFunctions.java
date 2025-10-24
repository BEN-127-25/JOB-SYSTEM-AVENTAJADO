package jobmain;

import config.config;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class UserFunctions {
    private static int interviewerId;
    static Scanner sc = new Scanner(System.in);
    static config database = new config();

    // =====================================================
    // 🔐 SECURE PASSWORD HASH FUNCTION (SHA-256)
    // =====================================================
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // convert bytes to hex
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage());
        }
    }

    public static void login() {
    System.out.print("Enter Email: ");
    String email = sc.next();
    System.out.print("Enter Password: ");
    String pass = sc.next();

    String hashedInput = hashPassword(pass);
    System.out.println("🔐 Your input password hash: " + hashedInput);

    String qry = "SELECT * FROM tbl_users WHERE Email = ? AND Password = ?";

    List<Map<String,Object>> result = database.fetchRecords(qry, email, pass);

    try {
        result = database.fetchRecords(qry, email, hashedInput);
    } catch (Exception e) {
        System.err.println("❌ Database error during login: " + e.getMessage());
        return;
    }

    if (result == null || result.isEmpty()) {
        System.out.println("❌ Invalid email or password.");
        return;  // stop execution to avoid NPE
    }

    Map<String, Object> user = result.get(0);

    if (user == null || user.get("Email") == null) {
        System.out.println("❌ User data is missing.");
        return;
    }

    String name = user.get("Email").toString();
    String stat = user.get("Status").toString();
    String role = user.get("Role").toString();

    if (stat.equalsIgnoreCase("Pending")) {
        System.out.println("⚠️ Account is Pending, please contact Admin!");
    } else if (stat.equalsIgnoreCase("Rejected")) {
        System.out.println("❌ Your account was rejected by the Admin.");
    } else {
        System.out.println("✅ LOGIN SUCCESS! Welcome, " + name + ".");
        if (role.equalsIgnoreCase("Admin")) {
            System.out.println("You are logged in as Admin.");
            AdminDashboard.showMenu();
        } else if (role.equalsIgnoreCase("Applicant")) {
            System.out.println("You are logged in as Applicant.");
            ApplicantsDashboard.showMenu(0);
        } else if (role.equalsIgnoreCase("Interviewer")) {
            System.out.println("You are logged in as Interviewer.");
          InterviewerDashboard.showMenu(interviewerId);
        } else {
            System.out.println("Unknown Role: " + role);
        }
    }
}



    // =====================================================
    // 📝 REGISTER FUNCTION
    // =====================================================
   public static void register() {
    config con = new config();

    System.out.print("Enter Your Name: ");
    String name = sc.next();

    String email;
    while (true) {
        System.out.print("Enter Email: ");
        email = sc.next();

        String checkQry = "SELECT * FROM tbl_users WHERE Email = ?";
        List<Map<String, Object>> checkResult = con.fetchRecords(checkQry, email);

        if (checkResult == null || checkResult.isEmpty()) {
            break;
        } else {
            System.out.println("⚠️ Email already exists, please enter another.");
        }
    }

    System.out.print("Enter Phone Number: ");
    String phone = sc.next();

    // Role selection with Interviewer option
    String role = "";
    while (true) {
        System.out.print("Enter Role (1 - Admin / 2 - Applicant / 3 - Interviewer): ");
        String input = sc.next();

        if (input.equals("1")) {
            role = "Admin";
            break;
        } else if (input.equals("2")) {
            role = "Applicant";
            break;
        } else if (input.equals("3")) {
            role = "Interviewer";
            break;
        } else {
            System.out.println("Invalid input! Please choose 1, 2, or 3.");
        }
    }

    System.out.print("Enter Password: ");
    String password = sc.next();

    // Hash password
    String hashedPassword = hashPassword(password);

    // All new users start as Pending
    String status = "Pending";

    String insertSQL = "INSERT INTO tbl_users (Name, Password, Email, Phone, Role, Status) VALUES (?, ?, ?, ?, ?, ?)";

    try {
        con.addRecord(insertSQL, name, hashedPassword, email, phone, role, status);
        System.out.println("✅ Registration successful! User role: " + role + ", Status: " + status);
    } catch (Exception e) {
        System.err.println("❌ Database error while registering: " + e.getMessage());
        e.printStackTrace();
    }
}


    // =====================================================
    // ✏️ UPDATE FUNCTION
    // =====================================================
    public static void updateUser() {
    // Display all users first
    System.out.println("\n=== CURRENT USERS ===");
    AdminDashboard.viewUsers(); // Show the table so the admin can see IDs

    System.out.print("\nEnter User ID to Update: ");
    int id = sc.nextInt();

    System.out.print("Enter New Name: ");
    String name = sc.next();
    System.out.print("Enter New Password: ");
    String password = sc.next();
    System.out.print("Enter New Email: ");
    String email = sc.next();
    System.out.print("Enter New Phone: ");
    String phone = sc.next();
    System.out.print("Enter New Role (Admin/Applicant/Interviewer): ");
    String role = sc.next();

    // Always hash password (whether it's numbers or text)
    String hashedPassword = hashPassword(password);
    String sql = "UPDATE tbl_users SET Name = ?, Password = ?, Email = ?, Phone = ?, Role = ? WHERE ID = ?";

    try {
        database.updateRecord(sql, name, hashedPassword, email, phone, role, id);
        System.out.println("✅ User updated successfully!");
    } catch (Exception e) {
        System.err.println("❌ Error updating user: " + e.getMessage());
        e.printStackTrace();
    }
}

    // =====================================================
    // 🗑️ DELETE FUNCTION
    // =====================================================
  public static void deleteUser() {
    // Display all users first
    System.out.println("\n=== CURRENT USERS ===");
    AdminDashboard.viewUsers(); // Show the table so the admin can see IDs

    System.out.print("\nEnter User ID to Delete: ");
    int id = sc.nextInt();

    String sql = "DELETE FROM tbl_users WHERE ID = ?";
    try {
        database.deleteRecord(sql, id);
        System.out.println("🗑️ User deleted successfully!");
    } catch (Exception e) {
        System.err.println("❌ Error deleting user: " + e.getMessage());
        e.printStackTrace();
    }
}
}
