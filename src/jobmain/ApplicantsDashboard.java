package jobmain;

import java.util.Scanner;

public class ApplicantsDashboard {

    public static void showMenu(int applicantId) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== APPLICANT DASHBOARD ===");
            System.out.println("1. View Available Jobs");
            System.out.println("2. Apply for a Job");
            System.out.println("3. View My Applications");
            System.out.println("4. View My Interview Schedule");
            System.out.println("5. Update My Profile");
            System.out.println("6. Withdraw an Application");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            // validate input
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // clear invalid input
                System.out.print("Enter your choice: ");
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n=== AVAILABLE JOBS ===");
                    ApplicantsFunctions.viewAvailableJobs();
                    break;
                case 2:
                    System.out.println("\n=== APPLY FOR A JOB ===");
                    ApplicantsFunctions.applyForJob(applicantId);
                    break;
                case 3:
                    System.out.println("\n=== MY APPLICATIONS ===");
                    ApplicantsFunctions.viewMyApplications(applicantId);
                    break;
                case 4:
                    System.out.println("\n=== MY INTERVIEW SCHEDULE ===");
                    ApplicantsFunctions.viewMyInterviews(applicantId);
                    break;
                case 5:
                    System.out.println("\n=== UPDATE PROFILE ===");
                    ApplicantsFunctions.updateProfile(applicantId);
                    break;
                case 6:
                    System.out.println("\n=== WITHDRAW APPLICATION ===");
                    ApplicantsFunctions.deleteApplication(applicantId);
                    break;
                case 7:
                    System.out.println("Logging out... Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 7);
    }
}
