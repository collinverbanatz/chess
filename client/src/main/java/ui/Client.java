package ui;

import java.util.Locale;
import java.util.Scanner;

public class Client {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean loggedIn = false;


    public static void main(String[] args){
        preLogin();
    }

    public static void preLogin(){
        System.out.println("Welcome to 240 Chess. \n");
        while (!loggedIn){
            System.out.println("Register: <USERNAME> <PASSWORD> <EMAIL>. -To create an account");
            System.out.println("Login: <USERNAME> <PASSWORD>. -To play chess.");
            System.out.println("Quit: -Quit playing chess");
            System.out.println("Help: -Help with playing chess \n");
            System.out.println("Enter Command:");
            String clientResponse = scanner.nextLine().trim().toLowerCase();
            switch (clientResponse){
                case("register"):
                    printRegister();
                    break;
                case("login"):
                    printLogin();
                    break;
                case("quit"):
                    System.out.println("goodbye");
                    System.exit(0);
                    break;
                case("help"):
                    printHelp();
                    break;
                default:
                    System.out.println("Not a valid command. Try again. \n");
            }


//            loggedIn = true;
        }
    }

    private static void printHelp() {
        System.out.println("Chess Help.");
        System.out.println("You can:");
        System.out.println("Login with a username and password");
        System.out.println("Register with a username, password, and email");
        System.out.println("Quite leave the application \n");
    }


    private static void printLogin() {
        System.out.println("Enter username:");
        String userName = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        //        implement serverFacade here
        System.out.println("Successful login. You are now logged in.");
        loggedIn = true;
    }

    private static void printRegister() {
        System.out.println("Enter username:");
        String userName = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();

//        implement serverFacade here
        System.out.println("Successful Register. You are now logged in.");
         loggedIn = true;
    }



}
