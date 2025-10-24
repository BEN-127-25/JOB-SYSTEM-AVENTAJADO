package jobmain;

import java.util.Scanner;

public class InterviewerDashboard {
    

   private static int interviewerId;
    public static void showMenu(int interviewerId) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== INTERVIEWER DASHBOARD ===");
            System.out.println("1. View All Applicants");
            System.out.println("2. Assign Applicant for Interview");
            System.out.println("3. View My Scheduled Interviews");
            System.out.println("4. Update Interview Status");
            System.out.println("5. Delete Interview");
            System.out.println("6. View Job Listings");
            System.out.println("7. Add Job");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Enter a number between 1-8:");
                sc.next();
            }

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    InterviewerFunctions.viewApplicants();
                    break;
                case 2:
                    InterviewerFunctions.assignApplicant(interviewerId);
                    break;
                case 3:
                    InterviewerFunctions.viewMyInterviews(interviewerId);
                    break;
                case 4:
                    InterviewerFunctions.updateInterviewStatus(interviewerId);
                    break;
                case 5:
                    InterviewerFunctions.deleteInterview(interviewerId);
                    break;
                case 6:
                    InterviewerFunctions.viewJobs();  // Show all available jobs
                    break;
                case 7:
                    InterviewerFunctions.addJob(interviewerId);  
                    break;
                case 8:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 8); // Exit if 8 is selected
    }


}
