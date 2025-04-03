package Controller;

import java.util.Scanner;

public class Controller {
    private Scanner scanner;

    public Controller() {
        scanner = new Scanner(System.in);
        System.out.println("Press 1 to register. Press 2 to login");
        int choice = scanner.nextInt();
        if(choice == 1){
            register();
        }
    }

    public void register(){
        System.out.println("Enter your name: ");
        String name = scanner.next();
        System.out.println("Enter your email: ");
        String email = scanner.next();
        System.out.println("Enter your password: ");
        String password = scanner.next();
        System.out.println("Enter your location");
        String location = scanner.next();

        System.out.println("Enter 1 if you want to be a user. Enter 2 if you want to be a organizer. Enter 3 if you want to be both");
        int choice = scanner.nextInt();

    }
}
