import java.io.FileNotFoundException;
import java.io.IOException;

public class GameLibrary {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Display.println(" ");
        Data.load();

        while (true) {

            int startup_input = Menu.showStartUp();

            if (startup_input < 0) break;
            else if (startup_input == 0) continue;


            
            while (true) {
                int input_msg = Menu.showMainMenu();
                boolean logOut = false;

                if (input_msg == 5) break;

                switch (input_msg) {
                    case 1:
                        if (Menu.showAccountActions() < 0) logOut = true; // Logout (automatically log out when user deletes account)
                        break;
                    case 2: 
                        Menu.showStore();
                        break;
                    case 3: 
                        Menu.showLibrary();
                        break;
                    case 4:
                        Menu.showWishlist();
                        break;
                }

                if (logOut) break;
            }
            
        }

        Data.save();
    }   
}