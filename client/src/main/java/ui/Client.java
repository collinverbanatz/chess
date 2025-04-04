package ui;

import dataaccess.DataAccessException;
import models.GameData;
import client.ServerFacade;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import models.ListGameResult;
import models.CreateResult;
import models.RegisterResult;
import websocket.commands.UserGameCommand;

public class Client {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static boolean inGame = false;
    static ServerFacade serverFacade;


    public static void main(String[] args) {
        preLogin();
    }

    public static void preLogin() {
        System.out.println("Welcome to 240 Chess. \n");
        serverFacade = new ServerFacade();
        while (!loggedIn) {
            System.out.println("Register: <USERNAME> <PASSWORD> <EMAIL>. -To create an account");
            System.out.println("Login: <USERNAME> <PASSWORD>. -To play chess.");
            System.out.println("Quit: -Quit playing chess");
            System.out.println("Help: -Help with playing chess \n");
            System.out.println("Enter Command:");
            String clientResponse = scanner.nextLine().trim().toLowerCase();
            switch (clientResponse) {
                case ("register"):
                    printRegister();
                    break;
                case ("login"):
                    printLogin();
                    break;
                case ("quit"):
                    System.out.println("goodbye");
                    System.exit(0);
                    break;
                case ("help"):
                    printHelp();
                    break;
                case ("clear"):
                    clearHandler();
                    break;
                default:
                    System.out.println("Not a valid command. Try again. \n");
            }


//            loggedIn = true;
        }
    }

    private static void clearHandler() {
        try {
            serverFacade.clear();
        } catch (Exception e) {
            System.err.println("couldn't clear");
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
        String authToken;
        try {
            RegisterResult rr = serverFacade.login(userName, password);
            authToken = rr.getAuthToken();
        } catch (Exception e) {
            System.err.println("invalid login");
            return;
        }
        System.out.println("Successful login. You are now logged in.");
        loggedIn = true;
        postLogin(authToken);
    }

    private static void printRegister() {
        System.out.println("Enter username:");
        String userName = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();

//        implement serverFacade here
        String authToken;
        try {
            RegisterResult rr = serverFacade.register(userName, password, email);
            authToken = rr.getAuthToken();
        } catch (IOException e) {
            System.err.println("that user name is already taken");
            return;
        }
        System.out.println("Successful Register. You are now logged in.");
        loggedIn = true;
        postLogin(authToken);
    }

    private static void postLogin(String authToken) {
        while (!inGame) {
            System.out.println("Help:-with possible commands");
            System.out.println("Create: <NAME> -a game");
            System.out.println("List: -games");
            System.out.println("Play: <ID> -game");
            System.out.println("Observe: <ID> -game:");
            System.out.println("Logout: -when you are done:");
            System.out.println("Enter Command:");
            String clientResponse = scanner.nextLine().trim().toLowerCase();

            switch (clientResponse) {
                case ("help"):
                    helpHandler();
                    break;
                case ("create"):
                    createGameHandler(authToken);
                    break;
                case ("list"):
                    listHandler(authToken);
                    break;
                case ("play"):
                    playHandler(authToken);
                    break;
                case ("observe"):
                    observeHandler(authToken);
                    break;
                case ("logout"):
                    logoutHandler(authToken);
                    break;
                default:
                    System.out.println("Not a valid command. Try again. \n");

            }
        }

    }

    private static void logoutHandler(String authToken) {
        try {
            serverFacade.logout(authToken);
            System.out.println("you have logged out");
            loggedIn = false;
            preLogin();
        } catch (Exception e) {
            System.err.println("couldn't logout");
        }
    }

    private static void observeHandler(String authToken) {
        System.out.println("Enter a game number to observe:");
        int gameNumber = Integer.parseInt(scanner.nextLine());
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        boolean isWhite = true;
        DrawChessBoard.drawChessBoard(out, isWhite);
        try {
            serverFacade.connect(authToken, gameNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        inGame = true;
    }

    private static void playHandler(String authToken) {
        boolean isWhite = false;
        boolean isColor = true;

        while (isColor) {
            System.out.println("Enter a game number:");
            String clientResponse = scanner.nextLine();

            System.out.println("Which color do you want:");
            String clientColor = scanner.nextLine().trim().toUpperCase();
            int gameID = getGameID(authToken, clientResponse);

//            System.out.println(gameID);
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            if (clientColor.equals("WHITE")) {
                isWhite = true;
                try {
                    serverFacade.joinGame(authToken, gameID, clientColor);
                    DrawChessBoard.drawChessBoard(out, isWhite);
                    isColor = false;
                    inGame = true;
                    serverFacade.connect(authToken, gameID);
                    gamePlay(authToken, gameID);

                } catch (IOException e) {
                    System.err.println("couldn't join game");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (clientColor.equals("BLACK")) {
                isWhite = false;
                try {
                    serverFacade.joinGame(authToken, gameID, clientColor);
                    DrawChessBoard.drawChessBoard(out, isWhite);
                    isColor = false;
                    inGame = true;
                    serverFacade.connect(authToken, gameID);
                    gamePlay(authToken, gameID);

                } catch (IOException e) {
                    System.err.println("couldn't join game");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("not a color");
            }
        }

    }

    private static int getGameID(String authToken, String gameNumber) {
        try {
            ListGameResult listGameResults = serverFacade.listGame(authToken);
            int gameCounter = 0;
            int number = Integer.parseInt(gameNumber);
            for (GameData game : listGameResults.getGames()) {
                gameCounter++;
                if (gameCounter == number) {
                    return game.getGameID();
                }
            }
        } catch (Exception e) {
            System.err.println("couldn't list games");
        }
        return 0;
    }

    private static void listHandler(String authToken) {
//        implement listing game by calling serverFacade
        try {
            ListGameResult listGameResult = serverFacade.listGame(authToken);
            int count = 1;
            for (GameData game : listGameResult.getGames()) {
                System.out.print(count + " GameName: " + game.getGameName());
                if (game.getWhiteUsername() != null) {
                    System.out.print(" White name: " + game.getWhiteUsername());
                } else {
                    System.out.print(" White name: None");
                }
                if (game.getBlackUsername() != null) {
                    System.out.println(" Black name: " + game.getBlackUsername());
                } else {
                    System.out.println(" Black name: None");
                }

                count++;
            }
//                System.out.println(new Gson().toJson(listGameResult));
        } catch (Exception e) {
            System.err.println("couldn't list games");
        }

    }

    private static void createGameHandler(String authToken) {
        System.out.println("Enter a game Name:");
        String clientResponse = scanner.nextLine();

//    implement creating a game
        try {
            CreateResult createResult = serverFacade.createGame(authToken, clientResponse);
        } catch (IOException e) {
            System.err.println("couldn't create game");
        }
    }

    private static void helpHandler() {
        System.out.println("\n Help menu");
        System.out.println("-Logout: Log out of your account");
        System.out.println("-Create: Creates a new game to play");
        System.out.println("-List: Lists all games to play ");
        System.out.println("-Play: Join a game to play");
        System.out.println("-Observe: Watch an ongoing game");
    }

    private static void gamePlay(String authToken, int gameID) throws Exception {
        while (inGame) {
            System.out.println("Help:-with possible commands");
            System.out.println("Redraw: chess board");
            System.out.println("Highlight: legal moves");
            System.out.println("Leave: chess game");
            System.out.println("Move: Makes move");
            System.out.println("Resign: game:");
            System.out.println("Enter Command:");
            String clientResponse = scanner.nextLine().trim().toLowerCase();

            switch (clientResponse) {
                case ("help"):
                    gamePlayPrint();
                    break;
                case ("redraw"):
                    break;
                case ("highlight"):

                    break;
                case ("leave"):
                    leaveHandler(authToken, gameID);
                    break;
                case ("move"):

                    break;
                case ("resign"):

                    break;
                default:
                    System.out.println("Not a valid command. Try again. \n");
            }
        }
    }

    private static void leaveHandler(String authToken, int gameID) {
        inGame = false;
        try{
            serverFacade.leave(UserGameCommand.CommandType.LEAVE, authToken,gameID);
        } catch (DataAccessException e) {
            System.err.println("couldn't leave game");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        postLogin(authToken);
    }

    private static void gamePlayPrint(){
        System.out.println("\n Help menu");
        System.out.println("-Redraw: Redraws the chess board");
        System.out.println("-Highlight: Highlights legal moves you can make");
        System.out.println("-Leave: Leaves the current game");
        System.out.println("-Move: Input which piece you want to move and where");
        System.out.println("-Resign: You forfeit the game \n");
    }
}
