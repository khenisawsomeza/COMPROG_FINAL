import java.io.*;
import java.util.*;

public class Data {
    // This class handles the data loading and saving for the game library application.
    private static FileWriter user_writer; 
    private static Scanner user_input;
    private static Scanner game_input;

    // This is the list of users and games in the application
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Game> games = new ArrayList<>();

    // This is the current user logged in to the application
    private static User current_user;

    // This is the main function that loads the data from the files
    public static void load() throws FileNotFoundException, IOException {
        // scanner for userData.txt and gameData.txt
        File InitialuserFile = new File("userData.txt");
        File InitialgameFile = new File("gameData.txt");
        user_input = new Scanner(InitialuserFile);
        game_input = new Scanner(InitialgameFile);

        //loads the games into the program from the gameData.txt file
        while (game_input.hasNextLine()) {
            String name = game_input.next();
            String company = game_input.next();
            Double price = Double.parseDouble(game_input.next());
            String genres = game_input.next();
            String discount_data = game_input.next(); // Read discount data
            boolean availability = game_input.nextBoolean(); // Read availability data

            // Create a new Game instance and add it to the games list
            Game game = new Game(name, company, price, genres, discount_data, availability);
            games.add(game); // Add the game to the games list
        }

        //loads the users into the the program from the userData.txt file
        while (user_input.hasNextLine()) {
            
            // get first line of userData.txt (username, id)
            String user_name = user_input.next(); // Read the first word (username) from the current line in userData.txt
            String id = user_input.next(); // Read ID from userData.txt
            user_input.nextLine(); // Move to the next line

            // Create FileReader and Scanner for user info, games, wishlist, and history
            File userFile = new File(id + ".txt");           
            File userLibraryFile = new File(id + "_library.txt");            
            File userWishlistFile = new File(id + "_wishlist.txt");          
            File userHistoryFile = new File(id + "_purchasehistory.txt");        
            Scanner info_scanner = new Scanner(userFile);
            Scanner games_scanner = new Scanner(userLibraryFile);
            Scanner wishlist_scanner = new Scanner(userWishlistFile);
            Scanner history_scanner = new Scanner(userHistoryFile);

            // Read user info from user info file
            String user_password = info_scanner.next(); // Read password from user info file
            Double user_credits = info_scanner.nextDouble(); // Read credits from user info file
            String user_history = ""; // Initialize user history

            // Read user history from user history file 
            while (history_scanner.hasNextLine()) {
                String line = history_scanner.nextLine();
                String[] historyData = line.split(" ");
                String gameName = historyData[0];
                String price = historyData[1];//String.format("$%.2f", Double.parseDouble(historyData[1]));
                String date = historyData[2];
                String time = historyData[3];

                // Append the formatted data to user_history
                user_history += gameName + " " + price + " " + date + " " + time + "\n";
            }

            // Remove the trailing newline character if user_history is not empty
            if (!user_history.isEmpty()) {
                user_history = user_history.substring(0, user_history.length() - 1);
            }

            // Create a new User instance and add it to the users list
            User user = new User(id, user_name, user_password, user_credits, convertGamesFromFile(userLibraryFile), convertGamesFromFile(userWishlistFile), user_history);
            users.add(user); // Add the user to the users list
            
            // close the scanners
            info_scanner.close();
            games_scanner.close();
            wishlist_scanner.close();
            history_scanner.close();
        }
    }

    // gets the games from the game file
    public static ArrayList<Game> convertGamesFromFile(File file) throws FileNotFoundException, IOException { 
        // This method reads the game names from a file and returns an ArrayList of Game objects
        Scanner libraryScanner = new Scanner(file);
        ArrayList<Game> ownedGames = new ArrayList<>();
    
        // Read each line from the file and check if the game exists in the games list
        while (libraryScanner.hasNextLine()) {
            String gameName = libraryScanner.nextLine();
            for (Game game : games) {
                if (game.getName().equals(gameName)) {
                    ownedGames.add(game);
                    break;
					}
				}
        }
        
        libraryScanner.close();
        return ownedGames;
    }

    // saves the user data in the different files of the user
    public static void save() throws FileNotFoundException, IOException {
        for (User user : users) {

            // Create FileWriter for user info, games, wishlist, and history
            FileWriter info_writer = new FileWriter(user.getUserFile());
            FileWriter games_writer = new FileWriter(user.getUserLibraryFile());
            FileWriter wishlist_writer = new FileWriter(user.getUserWishlistFile());
            FileWriter history_writer = new FileWriter(user.getUserHistoryFile());

            // Write user info
            info_writer.write(user.getPassword() + " " + user.getCredits());
            info_writer.close();

            // Write user library (owned games)
            games_writer.write(formatArrayList(user.getOwnedGames()));
            games_writer.close();

            // Write user wishlist
            wishlist_writer.write(formatArrayList(user.getWishlist()));
            wishlist_writer.close();

            // Write user history
            history_writer.write(user.getPurchaseHistory());
            history_writer.close();
        }
    }

    // updates the userData.txt file with the current users
    public static void updateUserData () throws IOException {
        // Update the userData.txt file
        File userDataFile = new File("userData.txt");
        FileWriter users_writer = new FileWriter(userDataFile);
        for (User u : users) {
            users_writer.write(u.getUsername() + " " + u.getId() + "\n");
        }
        users_writer.close();
    }

    // adds a new user to data
    public static User addUser(String username, String password) throws FileNotFoundException, IOException {
        String id = Data.generateUniqueID(); // generate a unique ID for the new user
        User user = new User(id, username, password); // create a new user object

        users.add(user); // add the new user to the users list

        // update the userData.txt file
        updateUserData();

        //create the user file
        File userFile = user.getUserFile(); //file name
        userFile.createNewFile();
        //write the initial data
        user_writer = new FileWriter(userFile);
        user_writer.write(password + " " + 0.0);
        user_writer.close();
        
        // Create the library file for the user
        File libraryFile = user.getUserLibraryFile();
        libraryFile.createNewFile();

        // Create the wishlist file for the user
        File wishlistFile = user.getUserWishlistFile();
        wishlistFile.createNewFile();

        // Create the purchase history file for the user
        File purchaseHistoryFile = user.getUserHistoryFile();
        purchaseHistoryFile.createNewFile();

        return user; // Return the new user object
    }

    // removes a user from the data
    public static void removeUser(User user) throws FileNotFoundException, IOException {

        // Remove the user from the users list
        users.remove(user);
        updateUserData();

         // Delete the user's files
       File UserToDelete = user.getUserFile();
       if (UserToDelete.exists()) {
           UserToDelete.delete();
       }
       File libraryToDelete = user.getUserLibraryFile();
       if (libraryToDelete.exists()) {
           libraryToDelete.delete();
       }
       File wishlistToDelete = user.getUserWishlistFile();
       if (wishlistToDelete.exists()) {
           wishlistToDelete.delete();
       }
       File historyToDelete = user.getUserHistoryFile();
       if (historyToDelete.exists()) {
           historyToDelete.delete();
       }
    }
    
    // finds a user by username
    public static User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user; // Return the matching User object
            }
        }
        return null; // Return null if no match is found
    }

    // randomly generates a unique account number
    public static String generateUniqueID() {
        Random random = new Random();
        ArrayList<String> usedId = new ArrayList<>();
        String id;

        for (User user : users) {
            usedId.add(user.getId());
        }

        do {
            id = String.format("%010d", random.nextLong() % 1_000_000_0000L);
            if (id.startsWith("-")) {
                id = id.substring(1); // Remove negative sign if any
            }
        } while (usedId.contains(id));

        return id;
    }

    private static String formatArrayList(ArrayList<Game> list) {
        String result = "";
        for (Game game : list) {
            result += game.getName() + "\n";
        }
        return result;
    }

    public static void setCurrentUser(User newUser) {
        current_user = newUser;
    }

    public static User getCurrentUser() {
        return current_user;
    }

    public static void unsetCurrentUser() {
        current_user = null;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }
}

