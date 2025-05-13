import java.util.Scanner;

abstract class Display {

    // Scanner for user input
    private static Scanner input = new Scanner(System.in);

    //prints a string
    public static void print(String message) {
        System.out.print(message);
    }
    
    //prints a string with new line
    public static void println(String message) {
        System.out.println(message);
    }

    //prints message with a box design
    public static void printBoxed(String message, int width) {
        Display.printRepeat("-", width);
        Display.println(String.format("%"+width/2+"s", message));
        Display.printRepeat("-", width);
    }

    //clears the console
    public static void printClear() {
        for (int i = 0; i < 50 ; i++) {
            System.out.print("\n");
        }
    }

    //repeates a string to be printed
    public static void printRepeat(String repeat, int count) {
        for (int i = 0; i < count ; i++) {
            System.out.print(repeat);
        }

        System.out.println();
    }

    // gets int from user
    public static int getInputInt(String message, boolean newLineInput) {
        // determine if newline is needed
        if (newLineInput) {
			println(message);
			print("\nInput: ");
        } else {
			print(message);
        };

        //checks if user is entering a int
        if (!input.hasNextInt()) {
			input.nextLine();
			return -1;
        }

        int input_msg = input.nextInt();
        input.nextLine();

        return input_msg;
    }

    // gets string from user
    public static String getInputStr(String message, boolean newLineInput) {
        // determine if newline is needed
        if (newLineInput) {
			println(message);
			print(" \nInput: ");
        } else print(message);

        String input_msg = input.nextLine();
        return input_msg;
    }

    // gets double from user
    public static double getInputDouble(String message, boolean newLineInput) {
        // determine if newline is needed
        if (newLineInput) {
			println(message);
			print("\nInput: ");
        } else {
			print(message);
        };

        //checks if user is entering a double
        if (!input.hasNextDouble()) {
			input.nextLine();
			return -1;
        }

        double input_msg = input.nextDouble();
        return input_msg;
    }

    //asks for input and check if valid (FOR INT) for navigation input
    public static int getValidInput(String message, int[] valids) {
        int input_msg = -1;
        boolean checker = false; //to check if the initial messages is already printed

        // checks if user is entering a int from the list of valid inputs
        do {
			if (!checker) // if the message is not printed yet
				input_msg = getInputInt(message, true);
			else if (checker) {
				input_msg = getInputInt("Input: ",  false);
			} 

            //check if entered input is valid
			for (int i = 0; i < valids.length; i++) {
				if (valids[i] != input_msg) continue;
				return input_msg;
			}

			checker = true;
			input_msg = -1;
			Display.println("Invalid Input");
        } while (input_msg < 0);

        return -1;
    }

    //asks for input and check if valid (FOR STRING) for navigation input
    public static String getValidInput(String message, String[] valids) {
        String input_msg = "";
        boolean checker = false; //to check if the initial messages is already printed

        // checks if user is entering a string from the list of valid inputs
        do {
			if (!checker) // if the message is not printed yet
				input_msg = getInputStr(message, true);
			else if (checker) {
				input_msg = getInputStr("Input: ", false);
			} 

            //check if entered input is valid
			for (int i = 0; i < valids.length; i++) {
				if (!valids[i].matches(input_msg)) continue;
				return input_msg;
			}

			checker = true;
			input_msg = "";
			Display.println("Invalid Input");
        } while (input_msg.isEmpty());

        return "";
    }

    //asks for input and check if valid (FOR DOUBLE) (if there is a error message) for user input
    public static double getValidDoubleInput(String message, String invalidMsg) {
        double input_msg = -1;
        boolean run = false;

        // checks if user is entering a double
        do {
			if (!run) 
				input_msg = getInputDouble(message, true);
			else if (run) {
				input_msg = getInputDouble("Input: ", false);
			} 

			if (input_msg >= 0) return input_msg;

			run = true;
			input_msg = -1;
			Display.println(invalidMsg);
        } while (input_msg < 0);

        return -1;
    }

    //asks for input and check if valid (FOR INT) (if there is a error message) for user input
    public static int getValidIntInput(String message, String invalidMsg) {
        int input_msg = -1;
        boolean run = false;

        do {
			if (!run) 
				input_msg = getInputInt(message, true);
			else if (run) {
				input_msg = getInputInt("Input: ", false);
			} 

			if (input_msg >= 0) return input_msg;

			run = true;
			input_msg = -1;
			println(invalidMsg);
        } while (input_msg < 0);

        return -1;
    }
}
