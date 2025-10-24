

package jobmain;

import config.config;
import java.util.Scanner;

public class Jobmain {
    private static char cont;

    public static void main(String[] args) {
        config.connectDB();
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("\n========== JOB MANAGEMENT SYSTEM ==========");
            System.out.println("1. LOGIN");
            System.out.println("2. REGISTER ACCOUNTS");
            System.out.println("3. EXIT");;
            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    UserFunctions.login();
                    break;
                case 2:
                    UserFunctions.register();
                    break;
                case 3:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            System.out.print("Do you want to continue? (Y/N): ");
            cont = sc.next().charAt(0);
        } while (cont == 'Y' || cont == 'y');

        System.out.println("Thank you! Program ended.");
    }
}
