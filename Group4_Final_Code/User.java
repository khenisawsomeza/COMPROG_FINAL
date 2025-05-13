import java.util.ArrayList;
import java.io.File;

public class User {
    // User attributes
    private String id;
	private String username;
	private String password;
	private double credits;
	private ArrayList<Game> ownedGames;
    private ArrayList<Game> wishlist;
    private String purchaseHistory;

    // File paths
    private File user_file;
    private File user_library;
    private File user_wishlist;
    private File user_history; 

    // Constructor
    // This constructor is used when loading user data from a file
    public User(String id, String user_new, String pass_new, Double credits_new, ArrayList<Game> ownedGames_new, ArrayList<Game> wishlist, String history) {
		setId(id);
		setUsername(user_new);
		setPassword(pass_new);
		setCredits(credits_new);
		setOwnedGames(ownedGames_new);
		setWishlist(wishlist);
		setPurchaseHistory(history);
        this.user_file = new File(id + ".txt");
        this.user_library = new File(id + "_library.txt");
        this.user_wishlist = new File(id + "_wishlist.txt");
        this.user_history = new File(id + "_purchasehistory.txt");
	}

    // This constructor is used when creating a new user
    public User(String id_new, String user_new, String pass_new) {
		setId(id_new);
		setUsername(user_new);
		setPassword(pass_new);
		setCredits(0);
		setOwnedGames(new ArrayList<>());
		setWishlist(new ArrayList<>());
		setPurchaseHistory("");
        this.user_file = new File(id_new + ".txt");
        this.user_library = new File(id_new + "_library.txt");
        this.user_wishlist = new File(id_new + "_wishlist.txt");
        this.user_history = new File(id_new + "_purchasehistory.txt");
	}

    // setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public void setOwnedGames(ArrayList<Game> ownedGames) {
        this.ownedGames = ownedGames;
    }

    public void setWishlist(ArrayList<Game> wishlist) {
        this.wishlist = wishlist;
    }

    public void setPurchaseHistory(String purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }


    // getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getCredits() {
        return credits;
    }

    public ArrayList<Game> getOwnedGames() {
        return ownedGames;
    }

     public ArrayList<Game> getWishlist() {
        return wishlist;
    }

    public File getUserFile() {
        return user_file;
    }

    public File getUserLibraryFile() {
        return user_library;
    }

    public File getUserWishlistFile() {
        return user_wishlist;
    }

    public File getUserHistoryFile() {
        return user_history;
    }

    public String getPurchaseHistory() {
        return purchaseHistory;
    }

    public void addGame(Game game) {
		ownedGames.add(game); // Add the game to the owned games list

		// Append purchase information to the history
		String purchaseInfo = String.format("%s $%.2f %s %s",
			game.getName(), // Game name
			game.getPriceFinal(), // Final price
			java.time.LocalDate.now().toString(), // Current date
			java.time.LocalTime.now().withNano(0).toString() // Current time (without nanoseconds)
		);

        // Append purchase information to the purchase history
        if (purchaseHistory.isEmpty()) {
            purchaseHistory += purchaseInfo;
        } else {
            purchaseHistory += "\n" + purchaseInfo;
        }
	}

    // Add a game to the wishlist if it's not already there
    public void addGameToWishlist(Game game) {
        if (!wishlist.contains(game)) {
            wishlist.add(game);
        }
    }

    // Remove a game from the owned games list
    public void removeGame(Game game) {
		ownedGames.remove(game);
	}
}
