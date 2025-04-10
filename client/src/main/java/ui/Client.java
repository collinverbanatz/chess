package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import models.CreateResult;
import models.GameData;
import models.ListGameResult;
import models.RegisterResult;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static boolean inGame = false;
    private static boolean gameOver = false;
    static ServerFacade serverFacade;
    public static ChessGame lastChessGame;
    public static boolean lastWasWhite;
    private static boolean isObserver = false;



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
        String clientResponse = scanner.nextLine();
        int gameID = getGameID(authToken, clientResponse);

        try {
            serverFacade.connect(authToken, gameID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        inGame = true;
        isObserver = true;
        gamePlay(authToken, gameID, true);
        isObserver = false;
    }

    private static void playHandler(String authToken) {
//        boolean isWhite = false;

        System.out.println("Enter a game number:");
        String clientResponse = scanner.nextLine();

        String clientColor;
        while (true) {
            System.out.println("Which color do you want:");
            clientColor = scanner.nextLine().trim().toUpperCase();
            if (!clientColor.equals("WHITE") && !clientColor.equals("BLACK")) {
                System.out.println("not a color");
                continue;
            }
            break;
        }
        int gameID = getGameID(authToken, clientResponse);
        try {
            serverFacade.joinGame(authToken, gameID, clientColor);

            inGame = true;
            serverFacade.connect(authToken, gameID);
            gamePlay(authToken, gameID, clientColor.equals("WHITE"));
        } catch (IOException e) {
            System.err.println("couldn't join game");
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public static void printBoard() {
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

            boolean isRedraw = false;

            DrawChessBoard.drawChessBoard(out, lastWasWhite, lastChessGame.getBoard(), isRedraw);
    }

    private static void gamePlay(String authToken, int gameID, boolean isWhite) {
        System.out.println("Help:-with possible commands");
        System.out.println("Redraw: chess board");
        System.out.println("Highlight: legal moves");
        System.out.println("Leave: chess game");
        if (!isObserver) {
            System.out.println("Move: Makes move");
            System.out.println("Resign: game:");
        }
        while (inGame) {
            System.out.println("Enter Command:");
            String clientResponse = scanner.nextLine().trim().toLowerCase();

            if(gameOver){
                resignedLoop();
            }

            switch (clientResponse) {
                case ("help"):
                    gamePlayPrint();
                    break;
                case ("redraw"):
                    redrawHandler(authToken,gameID, isWhite);
                    break;
                case ("highlight"):
                    highlightHandler();
                    break;
                case ("leave"):
                    leaveHandler(authToken, gameID);
                    break;
                case ("move"):
                    makeMoveHandler(authToken, gameID, isWhite);
                    break;
                case ("resign"):
                    resignHandler(authToken,gameID);
                    break;
                default:
                    System.out.println("Not a valid command. Try again. \n");
            }
        }
    }

    private static void makeMoveHandler(String authToken, int gameID, boolean isWhite) {
        System.out.println("Enter moves (enter piece position first then available position) <a2 a3>):");
        String clientMoves = scanner.nextLine().trim().toLowerCase();
        String[] moves = clientMoves.split("\\s+");
        if (moves.length < 2) {
            System.out.println("Invalid move");
            return;
        }
        String start = moves[0];
        if (!ChessPosition.isValidPosition(start)) {
            System.out.println("Invalid starting position");
            return;
        }
        ChessPosition startPosition = ChessPosition.fromPosition(start);
        String end = moves[1];
        if (!ChessPosition.isValidPosition(end)) {
            System.out.println("Invalid end position");
            return;
        }
        ChessPosition endPosition = ChessPosition.fromPosition(end);
        ChessPiece movingPiece = lastChessGame.getBoard().getPiece(startPosition);
        if (movingPiece == null) {
            System.out.println("No piece at " + moves[0]);
            return;
        }
        boolean isPromoting = false;
        if (movingPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (isWhite && endPosition.getRow() == 8) {
                isPromoting = true;
            } else if (!isWhite && endPosition.getRow() == 1) {
                isPromoting = true;
            }
        }
        ChessPiece.PieceType promotionType = null;
        if (isPromoting) {
            if (moves.length < 3) {
                System.out.println("Enter promotion piece with move");
                return;
            }
            String promotionPiece = moves[2];
            List<String> validPromotions = List.of("queen", "bishop", "knight", "rook");
            if (!validPromotions.contains(promotionPiece.toLowerCase())) {
                System.out.println("Promotion pieces must be one of: " + validPromotions);
                return;
            }
            promotionType = ChessPiece.PieceType.valueOf(promotionPiece.toUpperCase());
        }
        ChessMove move = new ChessMove(startPosition, endPosition, promotionType);

        try {
            serverFacade.makeMoves(authToken, gameID, move);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void highlightHandler() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        DrawChessBoard.drawChessBoard(out, lastWasWhite, lastChessGame.getBoard(), true);

    }


    private static void redrawHandler(String authToken, int gameID, boolean isWhite) {
        printBoard();
    }

    private static void resignHandler(String authToken, int gameID) {
        try {
            serverFacade.resign(authToken, gameID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void resignedLoop() {
        while(!gameOver){
            System.out.println("Leave: chess game");
            String clientResponse = scanner.nextLine().trim().toLowerCase();
            if (clientResponse.equals("leave")){
                inGame = false;
                gameOver = true;
            }
            else{
                System.out.println("Not a valid command. Try again.");
            }
        }
    }

    private static void leaveHandler(String authToken, int gameID) {
        inGame = false;
        try{
            serverFacade.leave(authToken,gameID);
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
        if (!isObserver) {
            System.out.println("-Move: Input which piece you want to move and where");
            System.out.println("-Resign: You forfeit the game \n");
        }
    }
}
