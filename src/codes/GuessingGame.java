import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class GuessingGame
{
    private static final int MIN = 1;
    private static final int MAX = 10;
    private static final int COUNTING_RULE = 1;
    private static final String EXIT = "q";

    private static int TOTAL_GUESSES = 0;
    private static int TOTAL_SUM  = 0;
    private static boolean PLAY_AGAIN = true;

    public static void main(String[] args) throws FileNotFoundException
    {
        playGame();
    }

    private static void playGame() throws FileNotFoundException {
        final Scanner scan;
        final Random random;

        //scan = new Scanner(new File("src/guesses.txt"));
        scan = new Scanner(System.in);
        random = new Random();

        int computerNumber = random.nextInt(MAX - MIN + COUNTING_RULE) + MIN;

        while (PLAY_AGAIN)
        {
            System.out.printf("Enter between %d and %d (%s to exit): \n", MIN, MAX, EXIT);

            if (scan.hasNext())
            {
                processUserInput(scan, computerNumber);
            }
        }
    }


    private static void processUserInput(Scanner scan, int computerNumber)
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

    private static void handleUserGuess(int userNumber,
                                        int computerNumber)
    {
        TOTAL_GUESSES++;
        TOTAL_SUM += Math.abs(userNumber - computerNumber);

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
        //displayScore();
    }

    private static void handleNonNumericInput(String userInput) {
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

    private static void displayScore() {
        final double averageGameScore;

        if(TOTAL_GUESSES > 0)
        {
            averageGameScore = (double)TOTAL_SUM / TOTAL_GUESSES;
        }
        else
        {
            averageGameScore = 0;
        }
        System.out.printf("It took you %d guesses to guess %d numbers. %.1f guesses average.\n", TOTAL_GUESSES, TOTAL_SUM, averageGameScore);
    }
}