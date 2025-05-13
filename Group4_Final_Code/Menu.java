import java.util.*;
import java.io.*;

public class Menu extends Display {
    
	// Startup Menu //
	public static int showStartUp() throws FileNotFoundException, IOException {
		printClear();
		printBoxed("Game Library v.0.0.0", 100);

		int[] valids = { 1, 2, 3 };
		int input = getValidInput("1: Enter Credentials\n2: Sign Up\n3. Exit", valids);

		Data.unsetCurrentUser();

		switch (input) {
			case 1:
				return showSignIn();
			case 2:
				return showSignUp();
			case 3:
				return -1;
			default:
				return -1;
		}
	}

    //sign up menu//
    private static int showSignUp() throws FileNotFoundException, IOException {
		//print headers
		printClear();
		printBoxed("Create An Account", 100);
	
        // get credentials
		String username = getInputStr("Enter Username: ", false);
		String password = getInputStr("Enter Password: ", false);
		String passwordConfirm = getInputStr("Enter Password To Confirm: ", false);

        // check if user already exists
		User user_existing = Data.findUserByUsername(username);
		boolean valid = true;
		int invalidType = -1;

		if (user_existing != null) {
			invalidType = 0;
			valid = false;
		} else if (!password.equals(passwordConfirm)) {
			invalidType = 1;
			valid = false;
		} else if (username.isEmpty() || username.isBlank() || password.isEmpty() || password.isBlank()) {
			invalidType = 2;
			valid = false;
		}
        
        // if user is not valid
		if (!valid) {
			printClear();
			int[] valids = { 1, 2 };
			String errorMsg = "";

			if (invalidType == 0)
				errorMsg = "Username Already Taken";
			else if (invalidType == 1)
				errorMsg = "Password and Password Confirm Are Unequal";
			else if (invalidType == 2)
				errorMsg = "Username or Password is invalid";

			switch (getValidInput(errorMsg + ": \n1. Retry \n2. Exit", valids)) {
				case 1:
					return showSignUp();
				case 2:
					return 0;
			}
		} else { // if user is valid
            User user = Data.addUser(username, password); // add user to Data
            Data.setCurrentUser(user); // set current user
		}
	
		return 1;
	}

	//show sign in menu
    private static int showSignIn() throws FileNotFoundException, IOException {
		//print headers
		printClear();
		printBoxed("Login To Account", 100);

		// get credentials
		String username = getInputStr("Enter Username: ", false);
		String password = getInputStr("Enter Password: ", false);

		// validate credentials
		User user = Data.findUserByUsername(username);
		if (user == null || !user.getPassword().equals(password)) {
			printClear();
			int[] valids = { 1, 2 };
			int input = getValidInput("Invalid Username Or Password: \n1. Retry \n2. Exit", valids);
			switch (input) {
				case 1:
					return showSignIn();
				case 2:
					return 0;
			}
		} else { // if user is valid
			Data.setCurrentUser(user); // set current user
		}

		return 1;
	}

	// show main menu options
    public static int showMainMenu() {
		User user = Data.getCurrentUser(); // get current user

		//print headers
		printClear();
		printBoxed("Main Menu", 100);

		// print user info
		println("Username: " + user.getUsername());
		println(String.format("Credits: $%.2f", user.getCredits()));
		println(" ");

		// show menu options
		int[] valids = { 1, 2, 3, 4, 5};
		int input = getValidInput(
			"1: Account Actions \n2. Browse Store \n3. Browse Library \n4. Browse Wishlist \n5. Logout",
			valids
		);

		return input;
	}

	//Account Actions Menu
	public static int showAccountActions() throws FileNotFoundException, IOException {
		User user = Data.getCurrentUser(); // get current user

		//print headers
		printClear();
		printBoxed("Account Actions", 100);

		// print user info
		println("Username: " + user.getUsername());
		println(String.format("Credits: $%.2f", user.getCredits()));
		println(" ");

		// show menu options
		int[] valids = { 1, 2, 3, 4, 5, 6 };
		int input = getValidInput(
			"1: Add Credits\n2: Change Username\n3. Change Password\n4. Show Purchase History\n5. Delete Account\n6. Back",
			valids
		);
		switch (input) {
			case 1: 
				showAddCredits(user);
				break;
			case 2: 
				showChangeUser(user);
				break;
			case 3: 
				showChangePass(user);
				break;
			case 4:
				showPurchaseHistory(user);
				break;
			case 5:
				return showDelAcc(user);
		}
		return 1;
	}

	// add credits menu
	private static void showAddCredits(User user) throws FileNotFoundException, IOException {
		//print headers
		printClear();
		printBoxed("Add Credits", 100);

		// print user info
		println("Username: " + user.getUsername());
		println(String.format("Credits: $%.2f", user.getCredits()));
		println(" ");

		// show menu options
		int[] valids = {1, 2};
		int input = getValidInput("1: Enter Amount \n2: Back", valids);
		if (input == 2) return;
		
		
		printClear();
		printBoxed("Add Credits", 100);

		// Ask for the amount of credits to add
		double amount = getValidDoubleInput("Enter the amount to add: ", "Invalid Credits To Add");
		
		// Update the user's credits
		user.setCredits(user.getCredits() + amount);

		// print confirmation message
		printClear();
		printBoxed("Credits Added Successfully!", 100);
		println("New Credits: $" + user.getCredits());
		getInputStr("Press Enter to continue...", true);
	}

	// Change username Menu
	private static void showChangeUser(User user) throws FileNotFoundException, IOException {
		//print headers
		printClear();
		printBoxed("Change Username", 100);

		// print user info
		println("Username: " + user.getUsername());
		println(String.format("Credits: $%.2f", user.getCredits()));
		println(" ");

		// show menu options
		int[] valids = {1, 2};
		int input = getValidInput("1: Enter New Username \n2: Back", valids);
		if (input == 2) return; // Exit the method

		//print headers
		printClear();
		printBoxed("Change Username", 100);

		// Prompt for the new username
		String newUsername = getInputStr("Enter New Username: ", false);

		// Check if the username is already taken
		if (Data.findUserByUsername(newUsername) != null) {
			printClear();
			printBoxed("Error", 100);
			println("Username already taken. Please try again.");
			getInputStr("Press Enter to continue...", true);
			return;
		}
	
		// Update the username in the User and Data classes
		user.setUsername(newUsername);
		Data.updateUserData();

		// print confirmation message
		printClear();
		printBoxed("Username Changed Successfully!", 100);
		println("New Username: " + user.getUsername());
		getInputStr("Press Enter to continue...", true);
	}

	// Change password Menu
	private static void showChangePass(User user) throws FileNotFoundException, IOException {
		//print headers
		printClear();
		printBoxed("Change Password", 100);

		// print user info
		println("Username: " + user.getUsername());
		println(String.format("Credits: $%.2f", user.getCredits()));
		println(" ");
	
		// show menu options
		int[] valids = {1, 2};
		int input = getValidInput("1: Enter New Password \n2: Back", valids);
	
		if (input == 2) return;
	
		//print headers
		printClear();
		printBoxed("Change Password", 100);

		// Prompt for the new password
		String newPassword = getInputStr("Enter New Password: ", false);
	
		// Confirm the new password
		String confirmPassword = getInputStr("Confirm New Password: ", false);
	
		// Check if the passwords match
		if (!newPassword.equals(confirmPassword)) {
			printClear();
			printBoxed("Error", 100);
			println("Passwords do not match. Please try again.");
			getInputStr("Press Enter to continue...", true);
			return;
		}
	
		// Update the password in the User and Data classes
		user.setPassword(newPassword);

		// print confirmation message
		printClear();
		printBoxed("Password Changed Successfully!", 100);
		getInputStr("Press Enter to continue...", true);
	}

	// Delete account menu
	public static int showDelAcc(User user) throws FileNotFoundException, IOException {
		//print headers
		printClear();
		printBoxed("Delete Account", 100);

		// print user info
		println("Username: " + user.getUsername());
		println(String.format("Credits: $%.2f", user.getCredits()));
		println(" ");
	
		// show menu options
		int[] valids = {1, 2};
		int input = getValidInput("Are you sure you want to delete your account?\n1: Yes\n2: No", valids);
	
		// if user chose not to delete the account
		if (input == 2) { 
			return 1;
		}
	
		// Confirm deletion by asking for the user's password
		printClear();
		printBoxed("Confirm Deletion", 100);
		String confirmationPassword = getInputStr("Enter your password to confirm account deletion: ", false);
	
		// Check if the entered password matches the user's password
		if (!confirmationPassword.equals(user.getPassword())) {
			printClear();
			printBoxed("Error", 100);
			println("Account deletion canceled. Password confirmation failed.");
			getInputStr("Press Enter to continue...", true);
			return 1;
		}
	
		// Delete the account from the Data class
		Data.removeUser(user);
	
		// Unset the current user
		Data.unsetCurrentUser();

		// print confirmation message
		printClear();
		printBoxed("Account Deleted Successfully!", 100);
		getInputStr("Press Enter to continue...", true);
	
		return -1;
	}

	// Store Menu
	public static void showStore() {
		User user = Data.getCurrentUser();

		//print headers
		printClear();
		printBoxed("Games Store", 100);

		// initialize variables for pagination
		ArrayList<Game> games = Data.getGames(); // Start with the full list of games
		int pageSize = 15; // Number of games per page
		int currentPage = 0; // Start at the first page
		int totalPages = (int) Math.ceil((double) games.size() / pageSize);
	
		// Main loop for the store menu
		while (true) {
			//print headers
			printClear();
			printBoxed("Games Store", 100);
			println(String.format("Credits: $%.2f", user.getCredits()));
			println(" ");

			// Check if there are no games
			if (games.isEmpty()) {
				println("No games available in the store.");
				getInputStr("Press Enter to go back...", true);
				return;
			}
	
			// Display games for the current page
			int start = currentPage * pageSize;
			int end = Math.min(start + pageSize, games.size());
			ArrayList<Game> gamesToDisplay = new ArrayList<>(games.subList(start, end));
			displayGamesTable(gamesToDisplay, start); // Display the games in a table format
	
			// Display navigation options
			println(" ");
			println("1: Previous Page");
			println("2: Next Page");
			println("3: Query");
			println("4: Buy a Game");
			println("5: Add to wishlist");
			println("6: Back");

			// Get valid input based on available options
			int[] valids = {1, 2, 3, 4, 5, 6};
			int input = getValidInput("", valids);

			// Handle user input
			if (input == 1) {
				currentPage = Math.max(0, currentPage - 1); // Go to the previous page
			} else if (input == 2) {
				currentPage = Math.min(totalPages - 1, currentPage + 1); // Go to the next page
			} else if (input == 3) {
				games = showQueryWindow(games, Data.getGames()); // Update the games list based on the query
				currentPage = 0; // Reset to the first page after a query
				totalPages = (int) Math.ceil((double) games.size() / pageSize); // Recalculate total pages
			} else if (input == 4) {
				buyGame(user, games, currentPage, pageSize); // Call the buyGame method
			} else if (input == 5) {
				wishlistGame(user, games, currentPage, pageSize);
			} else if (input == 6) {
				return; // Exit the store
			}
		}
	}

	// Buy Game Menu
	private static void buyGame(User user, ArrayList<Game> games, int currentPage, int pageSize) {
		//print headers
		printClear();
		printBoxed("Buy a Game", 100);
		println(String.format("Credits: $%.2f", Data.getCurrentUser().getCredits()));
		println(" ");

		// Display games for the current page
		int start = currentPage * pageSize;
		int end = Math.min(start + pageSize, games.size());
		ArrayList<Game> gamesToDisplay = new ArrayList<>(games.subList(start, end));
		displayGamesTable(gamesToDisplay, start);

		// Add an extra blank line before prompting
		println(" ");
		// Prompt the user to enter the ID of the game they want to buy
		int gameId = getValidIntInput("Enter the ID of the game you want to buy (or 0 to cancel): ", "Invalid ID.");

		// Allow the user to cancel the purchase
		if (gameId == 0) {
			return; // Exit the buy game process
		}

		// Validate the game ID
		if (gameId < 1 || gameId > games.size()) {
			printClear();
			printBoxed("Error", 100);
			println("Invalid game ID. Please try again.");
			getInputStr("Press Enter to continue...", true);
			return; // Exit the method
		}

		// Get the selected game
		Game selectedGame = games.get(gameId - 1);

		// check if the game is already available
		if (selectedGame.getAvailability() == false) {
			printClear();
			printBoxed("Error", 100);
			println("This game is not yet available for purchase.");
			getInputStr("Press Enter to continue...", true);
			return; // Exit the method
		}

		// Check if the user already owns the game
		if (user.getOwnedGames().contains(selectedGame)) {
			printClear();
			printBoxed("Error", 100);
			println("You already own this game.");
			getInputStr("Press Enter to continue...", true);
			return; // Exit the method
		}

		// Extract price details
		double basePrice = selectedGame.getPriceBase(); // Base price without tax or discount
		double taxedPrice = selectedGame.getPriceTaxxed(); // Price with 12% tax
		double finalPrice = selectedGame.getPriceFinal(); // Price after tax and discount
		String discountInfo = "N/A"; // Default discount info

		// Check if a discount is applied
		if (selectedGame.getDiscountPercent()>0) {
			discountInfo = selectedGame.getDiscountPercent() + "%";
		}

		// Display price details
		printClear();
		printBoxed("Price Details", 100);
		println("Base Price: $" + String.format("%.2f", basePrice));
		println("Taxed Price (12%): $" + String.format("%.2f", taxedPrice));
		println("Final Price (After Discount " + discountInfo + "): $" + String.format("%.2f", finalPrice));
		println(" ");

		// Confirm purchase
		int[] valids = {1, 2};
		int confirm = getValidInput("1: Confirm Purchase\n2: Cancel", valids);

		if (confirm == 2) {
			return; // Exit the buy game process
		}

		// Check if the user has enough credits
		if (user.getCredits() < finalPrice) {
			printClear();
			printBoxed("Error", 100);
			println("Insufficient credits to buy this game.");
			getInputStr("Press Enter to continue...", true);
			return; // Exit the method
		}

		// Deduct the price from the user's credits
		user.setCredits(user.getCredits() - finalPrice);

		// Add the game to the user's owned games
		user.addGame(selectedGame);

		// Confirm the purchase
		printClear();
		printBoxed("Purchase Successful!", 100);
		println("You have successfully purchased: " + selectedGame.getName());
		println(String.format("Remaining Credits: $%.2f", user.getCredits()));
		getInputStr("Press Enter to continue...", true);
	}

	// Wishlist Game Menu
	private static void wishlistGame(User user, ArrayList<Game> games, int currentPage, int pageSize) {
		//print headers
		printClear();
		printBoxed("Add a Game to Wishlist", 100);
		println(String.format("Credits: $%.2f", Data.getCurrentUser().getCredits()));
		println(" ");

		// Display games for the current page
		int start = currentPage * pageSize;
		int end = Math.min(start + pageSize, games.size());
		ArrayList<Game> gamesToDisplay = new ArrayList<>(games.subList(start, end));
		displayGamesTable(gamesToDisplay, start);

		// Add an extra blank line before prompting
		println(" ");

		// Prompt the user to enter the ID of the game they want to add to their wishlist
		int gameId = getValidIntInput("Enter the ID of the game to add to your wishlist (or 0 to cancel): ", "Invalid ID.");

		// Allow the user to cancel the process
		if (gameId == 0) {
			return; // Exit the wishlist process
		}

		// Validate the game ID
		if (gameId < 1 || gameId > games.size()) {
			printClear();
			printBoxed("Error", 100);
			println("Invalid game ID. Please try again.");
			getInputStr("Press Enter to continue...", true);
			return; // Exit the method
		}

		// Get the selected game
		Game selectedGame = games.get(gameId - 1);

		// Check if the game is already available
		if (selectedGame.getAvailability() == true) {
			printClear();
			printBoxed("Error", 100);
			println("This game is already available for purchase.");
			getInputStr("Press Enter to continue...", true);
			return; // Exit the method
		}

		// Check if the game is already in the user's wishlist
		if (user.getWishlist().contains(selectedGame)) {
			printClear();
			printBoxed("Error", 100);
			println("This game is already in your wishlist.");
			getInputStr("Press Enter to continue...", true);
			return; // Exit the method
		}

		// Add the game to the user's wishlist
		user.addGameToWishlist(selectedGame);

		// Confirm the addition
		printClear();
		printBoxed("Game Added to Wishlist", 100);
		println("Successfully added: " + selectedGame.getName() + " to your wishlist.");
		getInputStr("Press Enter to continue...", true);
	}

	// Library Menu
	public static void showLibrary() {
		User user = Data.getCurrentUser(); // get current user

		//display headers
		printClear();
		printBoxed("Games Library", 100);

		// loads the user's owned games
		ArrayList<Game> ownedGames = user.getOwnedGames(); // Use the correct ArrayList<Game> from User
	
		// initialize variables for pagination
		int pageSize = 15;
		int currentPage = 0;
		int totalPages = (int) Math.ceil((double) ownedGames.size() / pageSize);
	
		// Main loop for the library menu
		while (true) {
			//print headers
			printClear();
			printBoxed("Games Library", 100);

			//check if there are no games
			if (ownedGames.isEmpty()) {
				println("No games in your library.");
				getInputStr("Press Enter to go back...", true);
				return;
			}
	
			// Display games for current page
			int start = currentPage * pageSize;
			int end = Math.min(start + pageSize, ownedGames.size());
			ArrayList<Game> gamesToDisplay = new ArrayList<>(ownedGames.subList(start, end));
			displayGamesTable(gamesToDisplay, start);

			// Display navigation options
			println(" ");
			println("1: Previous Page");
			println("2: Next Page");
			println("3: Query");
			println("4: Remove Game");
			println("5: Back");

			// Get valid input based on available options
			int[] valids = {1, 2, 3, 4, 5};
			int input = getValidInput("", valids);
			switch (input) {
				case 1:
					currentPage = Math.max(0, currentPage - 1);
					break;
				case 2:
					currentPage = Math.min(totalPages - 1, currentPage + 1);
					break;
				case 3:
					ownedGames = showQueryWindow(ownedGames, user.getOwnedGames());
					currentPage = 0;
					totalPages = (int) Math.ceil((double) ownedGames.size() / pageSize);
					break;
				case 4:
					removeGame(user, ownedGames, currentPage, pageSize);
					return;
				case 5:
					return;
			}
		}
	}

	// Remove Game Menu from library
	private static void removeGame(User user, ArrayList<Game> ownedGames, int currentPage, int pageSize) {
		//print headers
		printClear();
		printBoxed("Remove Game", 100);
		println(" ");

		// Initialize variables for pagination
		int start = currentPage * pageSize;
		ArrayList<Game> gamesToDisplay = new ArrayList<>(ownedGames.subList(start, 
		Math.min(start + pageSize, ownedGames.size())));
		displayGamesTable(gamesToDisplay, start);

		// Add an extra blank line before prompting
		println(" ");
		// Prompt the user to enter the ID of the game they want to remove
		int gameId = getValidIntInput("Enter the ID of the game to remove (or 0 to cancel): ", "Invalid ID.");

		// Allow the user to cancel the process
		if (gameId == 0) return;
	
		// Validate the game ID
		if (gameId < 1 || gameId > ownedGames.size()) {
			printClear();
			printBoxed("Error", 100);
			println("Invalid game ID.");
			getInputStr("Press Enter to continue...", true);
			return;
		}
	
		// Get the selected game
		Game selectedGame = ownedGames.get(gameId - 1);
	
		// Remove the game from the user's owned games
		user.removeGame(selectedGame);
	
		// print confirmation message
		printClear();
		printBoxed("Game Removed", 100);
		println("Successfully removed: " + selectedGame.getName());
		getInputStr("Press Enter to continue...", true);
	}

	// Wishlist Menu
	public static void showWishlist() {
		User user = Data.getCurrentUser(); // get current user

		///print headers
		printClear();
		printBoxed("Games Wishlist", 100);

		// loads the user's wishlist
		ArrayList<Game> Wishlist = user.getWishlist(); // Use the correct ArrayList<Game> from User
	
		// initialize variables for pagination
		int pageSize = 15;
		int currentPage = 0;
		int totalPages = (int) Math.ceil((double) Wishlist.size() / pageSize);

		// Main loop for the wishlist menu
		while (true) {
			//print headers
			printClear();
			printBoxed("Games Wishlist", 100);

			//check if there are no games
			if (Wishlist.isEmpty()) {
				println("No games in your Wishlist.");
				getInputStr("Press Enter to go back...", true);
				return;
			}
	
			// Display games for current page
			int start = currentPage * pageSize;
			int end = Math.min(start + pageSize, Wishlist.size());
			ArrayList<Game> gamesToDisplay = new ArrayList<>(Wishlist.subList(start, end));
			displayGamesTable(gamesToDisplay, start);

			// Display navigation options
			println(" ");
			println("1: Previous Page");
			println("2: Next Page");
			println("3: Query");
			println("4: Back");

			// Get valid input based on available options
			int[] valids = {1, 2, 3, 4};
			int input = getValidInput("", valids);
			switch (input) {
				case 1:
					currentPage = Math.max(0, currentPage - 1);
					break;
				case 2:
					currentPage = Math.min(totalPages - 1, currentPage + 1);
					break;
				case 3:
					Wishlist = showQueryWindow(Wishlist, user.getWishlist());
					currentPage = 0;
					totalPages = (int) Math.ceil((double) Wishlist.size() / pageSize);
					break;
				case 4:
					return;
			}
		}
	}

	// Show purchase history 
	public static void showPurchaseHistory(User user) {
		//print headers
		printClear();
		printBoxed("Purchase History", 100);

		// get user's purchase historyq
		String history = user.getPurchaseHistory();

		//check if there are no purchases
		if (history.isEmpty()) {
			println("No purchase history available.");
			getInputStr("Press Enter to go back...", true);
			return;
		}

		// Print table header
		println(String.format("%-15s %-10s %-25s %-10s", "Date", "Time", "Game Name", "Price"));
		println(String.format("%-15s %-10s %-25s %-10s", "---------------", "----------", "--------------------", "----------"));

		// Split history into lines and process each line
		String[] historyEntries = history.split("\n");
		for (String entry : historyEntries) {
			String[] parts = entry.split(" ");
			String gameName = parts[0];
			String price = String.format("$%.2f", Double.parseDouble(parts[1].replace("$", "")));
			String date = parts[2];
			String time = parts[3];

			// Print each entry in the table
			println(String.format("%-15s %-10s %-25s %-10s", date, time, gameName, price));
		}

		// show options to go back
		println(" ");
		getInputStr("Press Enter to go back...", true);
	}

	// Display games in a table format
	private static void displayGamesTable(ArrayList<Game> games, int startIndex) {
		// Print table header
		println(String.format("%-5s %-35s %-25s %-10s %-20s %-15s %-30s %-15s", "ID", "Name", "Company", "Price", "Discount %", "Final Price", "Genres", "Availability"));
		println(String.format("%-5s %-35s %-25s %-10s %-20s %-15s %-30s %-15s", "---", "-----------------------------", "---------------", "----------", "--------------------", "----------", "------------------------------", "----------------"));

		// Print each game in the table
		for (Game game : games) {
			// Extract discount data
			double finalPrice = game.getPriceFinal(); // Default to taxed price
			double discountPercent = game.getDiscountPercent();

			// format and display the games based on the discount/final price
			println(String.format(
				"%-5d %-35s %-25s $%-9.2f %-20s $%-15.2f %-30s %-15s",
				startIndex + games.indexOf(game) + 1, // ID is the index in Data's games ArrayList + 1
				game.getName(), // Name
				game.getCompany(), // Company
				game.getPriceTaxxed(), // Price before discount
				discountPercent > 0 ? discountPercent + "%" : "N/A", // Discount percentage (only if valid), // Discount percentage (with % if valid)
				finalPrice, // Final price after discount
				String.join(", ", game.getGenres()), // Genres
				game.getAvailability() // Availability
			));
		}
	}

	// Query Window for filtering and sorting games
	private static ArrayList<Game> showQueryWindow(ArrayList<Game> currentGames, ArrayList<Game> fullList) {
		while (true) {
			//print headers
			printClear();
			printBoxed("Query Games", 100);

			// display navigation options
			println("1: Filter by Game Name");
			println("2: Filter by Price Range");
			println("3: Sort by Genre");
			println("4: Sort by Company");
			println("5: Sort Alphabetically");
			println("6: Sort by Availability");
			println("7: Reset to Full List");
			println("8: Back");

			// Get valid input based on available options
			int[] valids = {1, 2, 3, 4, 5, 6, 7, 8};
			int input = getValidInput("", valids);
			switch (input) {
				case 1: // Filter by game name
					//print headers
					printClear();
					printBoxed("Filter by Game Name", 100);
					String searchName = getInputStr("Enter game name to filter by: ", false);

					// Check if game name if available
					ArrayList<Game> searchResults = new ArrayList<>();
					for (Game game : fullList) {
						if (game.getName().toLowerCase().contains(searchName.toLowerCase())) {
							searchResults.add(game);
						}
					}
					
					// Check if any games were found
					if (searchResults.isEmpty()) {
						println("No games found with the name: " + searchName);
						getInputStr("Press Enter to continue...", true);
					} else {
						return searchResults;
					}
					break;
	
				case 2: // Filter by price range
					//print headers
					printClear();
					printBoxed("Filter by Price Range", 100);

					// Prompt for minimum and maximum price
					double minPrice = getValidDoubleInput("Enter minimum price: ", "Invalid price.");
					print("");
					double maxPrice = getValidDoubleInput("Enter maximum price: ", "Invalid price.");

					// Check availability of games in the price range
					ArrayList<Game> priceResults = new ArrayList<>();
					for (Game game : fullList) {
						if (game.getPriceFinal() >= minPrice && game.getPriceFinal() <= maxPrice) {
							priceResults.add(game);
						}
					}
					
					// Check if any games were found
					if (priceResults.isEmpty()) {
						println("No games found in the price range: $" + minPrice + " - $" + maxPrice);
						getInputStr("Press Enter to continue...", true);
					} else {
						return priceResults;
					}
					break;
	
				case 3: // Sort by genre
					//print headers
					printClear();
					printBoxed("Sort by Genre", 100);
					String genre = getInputStr("Enter genre to filter by: ", false);
	
					// check if ther is any game with the genre
					ArrayList<Game> genreResults = new ArrayList<>();
					for (Game game : fullList) {
						String genres = game.getGenres().toString().toLowerCase();
						if (genres.contains(genre.toLowerCase())) {
							genreResults.add(game);
						}
						System.out.println(genres);
					}
	
					// Check if any games were found
					if (genreResults.isEmpty()) {
						println("No games found in the genre: " + genre);
						getInputStr("Press Enter to continue...", true);
					} else {
						return genreResults;
					}
					break;
	
				case 4: // Sort by company
					//print headers
					printClear();
					printBoxed("Sort by Company", 100);

					// Prompt for company name
					String company = getInputStr("Enter company to filter by: ", false);
					
					// Check if there is any game with the company
					ArrayList<Game> companyResults = new ArrayList<>();
					for (Game game : fullList) {
						if (game.getCompany().toLowerCase().contains(company.toLowerCase())) {
							companyResults.add(game);
						}
					}
					
					// Check if any games were found
					if (companyResults.isEmpty()) {
						println("No games found for the company: " + company);
						getInputStr("Press Enter to continue...", true);
					} else {
						return companyResults;
					}
					break;
	
				case 5: // Sort alphabetically
					//print headers
					printClear();
					printBoxed("Sort Alphabetically", 100);

					// Sort the games alphabetically by name
					fullList.sort((g1, g2) -> g1.getName().compareToIgnoreCase(g2.getName()));
					println("Games sorted alphabetically.");
					getInputStr("Press Enter to continue...", true);
					return fullList;

				case 6: // Sort by availability
					//print headers
					printClear();
					printBoxed("Sort by Availability", 100);

					// Prompt for availability option
					int available = getValidInput("1: Available\n2: Coming Soon", new int[]{1, 2});

					// Initialize the list for available games
					ArrayList<Game> availableResults = new ArrayList<>();
					
					// filter the games based on availability
					switch (available) {
						case 1:
							for (Game game : fullList) {
								if (game.getAvailability() == true) {
									availableResults.add(game);
								}
							}
							return availableResults;
						case 2:
							for (Game game : fullList) {
								if (game.getAvailability() == false) {
									availableResults.add(game);
								}
							}
							return availableResults;
							
					}
					break;

				case 7: // Reset to full list
					return new ArrayList<>(fullList); // Return a copy of the full list
	
				case 8: // Go back
					return fullList;
			}
		}
	}

}