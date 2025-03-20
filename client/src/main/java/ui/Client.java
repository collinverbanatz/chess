package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Client {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static boolean inGame = false;


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
        postLogin();
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
         postLogin();
    }

    private static void postLogin() {
        while(!inGame){
            System.out.println("Help:-with possible commands");
            System.out.println("Create: <NAME> -a game");
            System.out.println("List: -games");
            System.out.println("Play: <ID> -game");
            System.out.println("Observe: <ID> -game:");
            System.out.println("Logout: -when you are done:");
            System.out.println("Enter Command:");
            String clientResponse = scanner.nextLine().trim().toLowerCase();

            switch (clientResponse){
                case("help"):
                    helpHandler();
                    break;
                case("create"):
                    createGameHandler();
                    break;
                case("list"):
                    listHandler();
                    break;
                case("play"):
                    playHandler();
                    break;
                case("observe"):
                    observeHandler();
                    break;
                case("logout"):
                    break;
                default:
                    System.out.println("Not a valid command. Try again. \n");

            }
        }

    }

    private static void observeHandler() {
        System.out.println("Enter a game number to observe:");
        int gameNumber = Integer.parseInt(scanner.nextLine());
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        boolean isWhite = true;
        DrawChessBoard.drawChessBoard(out, isWhite);
        inGame = true;
    }

    private static void playHandler() {
        System.out.println("Enter a game number:");
        String clientResponse = scanner.nextLine();

        System.out.println("Which color do you want:");
        String clientColor = scanner.nextLine().trim().toLowerCase();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        boolean isWhite;
        if(clientColor.equals("white")){
            isWhite = true;
        }
        else{
            isWhite = false;
        }
        DrawChessBoard.drawChessBoard(out, isWhite);
        inGame = true;

//        use ServerFacade to join the game

    }

    private static void listHandler() {
//        implement listing game by calling serverFacade
    }

    private static void createGameHandler() {
        System.out.println("Enter a game Name:");
        String clientResponse = scanner.nextLine();

//    implement creating a game
    }

    private static void helpHandler() {
        System.out.println("\n Help menu");
        System.out.println("-Logout: Log out of your account");
        System.out.println("-Create: Creates a new game to play");
        System.out.println("-List: Lists all games to play ");
        System.out.println("-Play: Join a game to play");
        System.out.println("-Observe: Watch an ongoing game");
    }


}
