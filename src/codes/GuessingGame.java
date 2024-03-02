import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * A simple guessing game where the player tries to guess a random number chosen by the computer.
 *
 * @author Felipe Andres, Yeongsuk Oh, Samson Ordonez
 */
public class GuessingGame
{
    private static final int MIN = 1;
    private static final int MAX = 10;
    private static final int COUNTING_RULE = 1;
    private static final int TOTAL_RANGE  = (MAX - MIN) + COUNTING_RULE;
    private static final int MAX_GUESSES = 5;
    private static final String EXIT = "q";
    private static final String FILE_LOC_APPENDER = "src/";

    private static int COUNT_GUESSES = 0;
    private static boolean PLAY_AGAIN = true;

    /**
     * Main method to start the game.
     *
     * @param args Command-line arguments.
     * @throws FileNotFoundException If a file required for the game is not found.
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        playGame();
    }

    /**
     * Method to initiate and play the game.
     * @throws FileNotFoundException If a file required for the game is not found.
     */
    private static void playGame() throws FileNotFoundException
    {
        final Scanner scan;
        final Random random;

        scan = new Scanner(System.in);
        random = new Random();

        int computerNumber = random.nextInt(MAX - MIN + COUNTING_RULE) + MIN;

        while(true)
        {
            System.out.println("Do you want to input guesses directly or read from a file? (Type 'direct' or 'file')");
            String userInput = scan.nextLine().toLowerCase();

            if(userInput.equals("direct"))
            {
                playDirect(scan, computerNumber);
                break;
            }
            else if(userInput.equals("file"))
            {
                playFromFile(computerNumber);
                break;
            }
            else
            {
                System.out.println("Invalid choice. Please type 'direct' or 'file'.");
            }
        }
    }

    /**
     * Method to play the game where the user inputs guesses directly.
     *
     * @param scan           Scanner object for user input.
     * @param computerNumber The randomly generated number by the computer.
     */
    private static void playDirect(final Scanner    scan,
                                   int              computerNumber)
    {
        while(PLAY_AGAIN)
        {
            int guesses = 0;
            while (guesses < MAX_GUESSES)
            {
                System.out.printf("Enter a number between %d and %d (%s to exit): \n", MIN, MAX, EXIT);
                if (scan.hasNext()) {
                    processUserInput(scan, computerNumber);
                    guesses++;
                    if (COUNT_GUESSES == MAX_GUESSES) {
                        break;
                    }
                }
            }
            if (guesses >= MAX_GUESSES) {
                System.out.println("You have used all your guesses. Let's try again.");
                COUNT_GUESSES = 0;
                computerNumber = new Random().nextInt(MAX - MIN + COUNTING_RULE) + MIN;
            }
        }
    }


    /**
     * Method to play the game where the user inputs guesses from a file.
     *
     * @param computerNumber The randomly generated number by the computer.
     */
    private static void playFromFile(final int computerNumber)
    {
        final Scanner scan;
        String fileName;

        scan = new Scanner(System.in);

        while(true)
        {
            System.out.println("Enter the filename with '.txt' extension, or type 'menu' to return to the main menu:");
            fileName = scan.nextLine();

            if(fileName.equalsIgnoreCase("menu"))
            {
                return; // Return to main menu
            }

            if(!fileName.endsWith(".txt"))
            {
                System.out.println("Invalid file name. Please add '.txt' extension.");
                continue; // Restart the loop
            }

            try
            {
                File file = new File(FILE_LOC_APPENDER + fileName);
                Scanner fileScanner = new Scanner(file);

                while (fileScanner.hasNextLine())
                {
                    String line = fileScanner.nextLine();

                    // Attempt to parse the line to an integer
                    try
                    {
                        int userNumber;

                        userNumber = Integer.parseInt(line);

                        handleUserGuess(userNumber, computerNumber);
                    }
                    catch(NumberFormatException e)
                    {
                        System.out.println("Invalid number format in file.");
                    }
                }

                fileScanner.close(); // Close the scanner
                break; // Exit loop if file is found and processed
            }
            catch (FileNotFoundException e)
            {
                System.out.println("File not found: " + fileName);
            }
        }
    }


    /**
     * Method to process user input for direct guessing.
     *
     * @param scan           Scanner object for user input.
     * @param computerNumber The randomly generated number by the computer.
     */
    private static void processUserInput(final Scanner scan,
                                         final int computerNumber)
    {
        if(scan.hasNextInt())
        {
            int userNumber = scan.nextInt();
            handleUserGuess(userNumber, computerNumber);
        }
        else
        {
            handleNonNumericInput(scan.nextLine());
        }
    }

    /**
     * Method to handle user's guess.
     *
     * @param userNumber     The number guessed by the user.
     * @param computerNumber The randomly generated number by the computer.
     */
    private static void handleUserGuess(final int userNumber,
                                        final int computerNumber)
    {
        COUNT_GUESSES++;

        if(userNumber == computerNumber)
        {
            System.out.println("Correct");
            displayScore();
        }
        else if (userNumber > MAX || userNumber < MIN)
        {
            System.out.format("Please enter a number between %d and %d.\n", MIN, MAX);
        }
        else if(userNumber < computerNumber)
        {
            System.out.format("You have guessed: %d, which is smaller than the random number.\n", userNumber);
        }
        else
        {
            System.out.format("You have guessed: %d, which is larger than the random number.\n", userNumber);
        }
    }

    /**
     * Method to handle non-numeric user input.
     *
     * @param userInput The non-numeric input provided by the user.
     */
    private static void handleNonNumericInput(final String userInput) {
        if(userInput.equalsIgnoreCase(EXIT))
        {
            PLAY_AGAIN = false;
            System.out.println("Thanks for playing");
            System.exit(0);
        }
        else
        {
            System.err.println("Invalid input! You have typed " + userInput);
        }
    }


    /**
     * Method to display the game's score and statistics.
     */
    private static void displayScore() {
        final double averageGameScore;

        if (COUNT_GUESSES > 0)
        {
            averageGameScore = (double) COUNT_GUESSES / TOTAL_RANGE;
        }
        else
        {
            averageGameScore = 0;
        }

        final double percentageGuessed;
        final int uniqueNumbers;
        
        percentageGuessed = (double) COUNT_GUESSES / TOTAL_RANGE * 100;
        uniqueNumbers = MAX - MIN + COUNTING_RULE;


        System.out.printf("The computer picked %d different numbers.\n", uniqueNumbers);
        System.out.printf("You guessed %d of them with %d guesses.\n", COUNT_GUESSES, COUNT_GUESSES);
        System.out.printf("You averaged %.1f guesses and guessed %.0f%% of the numbers.\n", averageGameScore, percentageGuessed);
    }
}