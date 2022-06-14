import java.io.Reader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Hangman {
    public ArrayList<String> words = new ArrayList<String>();
    String FILE = "words.txt";
    String word;
    int LIVES = 5;
    String key = "";

    ArrayList<String> guesses; //String arraylist with all correct guesses
    ArrayList<String> hints; //String arraylist with all given hints

    int tally; //Number of letters left to guess
    int HINTS = 0;

    public static void main(String[] args) {
        Hangman h = new Hangman();
        h.readWords(); //read in all words to words ArrayList

        h.game(); //run the game
    }

    public void readWords() {
        boolean w;

        try { 
            BufferedReader reader = new BufferedReader(new FileReader(FILE));
            String line = reader.readLine(); 

            while (line != null) {
                w = true;

                for (int i = 0; i < line.length(); i++) {
                    if (!Character.isLetter(line.charAt(i))) {
                        w = false;
                        break;
                    }
                }

                if (w) {
                    words.add(line);   
                }

                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println(e); 
        } catch (IOException e) {
            System.out.println(e);     
        }  
    }

    public void printWords() {
        for (int i = 0; i < words.size(); i++) {
            System.out.println(i + ": " + words.get(i));
        }
    }

    public void game() {
        System.out.println("Guess a letter...");

        boolean right = false;
        boolean won = false;

        Random r = new Random();
        word = words.get(r.nextInt(words.size()));
        tally = word.length();
        guesses = new ArrayList<String>(word.length());

        //System.out.println(word);

        initKey(); //initializes string same length of word with underscores
        printKey(); //prints key

        String guess;

        while (true) {
            guess = guess();
            right = false;

            //for each letter in word, see if guess matches anywhere - 
            for (int i = 0; i < word.length(); i++) {
                if (Character.toLowerCase(guess.charAt(0)) == Character.toLowerCase(word.charAt(i))) {
                    right = true;
                    System.out.println("Correct");
                    guesses.add(guess);
                    tally = updateKey(guess);
                    printKey();
                    break;
                }
            }

            if (!right) {
                LIVES--;
                System.out.println("Incorrect, " + LIVES + " lives remaining.");
                printKey();
            }

            if (LIVES <= 0 || tally <= 0) {
                break;   
            }
        }

        if (tally <= 0) {
            System.out.println("You guessed the word " + word + 
                " correctly with " + HINTS + " hints.");   
        } else {
            System.out.println("You fucked up.");
        }
    }

    public void initKey() {
        for (int i = 0; i < word.length(); i++) {
            key += "_";
        }
    }

    public void printKey() {
        System.out.println(key);
    }

    public String guess() {
        String guess;
        Scanner s = new Scanner(System.in);
        boolean check = true;

        while (true) {
            guess = s.nextLine(); 
            check = true;

            if (guess.length() != 0) {
                for (int i = 0; i < guesses.size(); i++) {
                    if (Character.toLowerCase(guess.charAt(0)) == guesses.get(i).charAt(0)) {
                        System.out.println("You have already guessed " + guess + ".");
                        printKey();
                        check = false;
                        break;
                    }
                }

                if (check) {
                    if (guess.equalsIgnoreCase("hint")) {
                        giveHint();
                    } else if (guess.length() > 1) {
                        System.out.println("Enter a single character.");
                        printKey();
                    } else if (!Character.isLetter(guess.charAt(0))) {
                        System.out.println("Please enter a character.");   
                        printKey();
                    } else {
                        break;   
                    }
                }
            } else {
                System.out.println("Not a valid character.");
                printKey();
            }
        }
        return guess;
    }

    public int updateKey(String guess) {
        for (int i = 0; i < word.length(); i++) {
            if (Character.toLowerCase(guess.charAt(0)) == Character.toLowerCase(word.charAt(i))) {
                tally--;
                key = (key.substring(0, i) + guess + key.substring(i + 1, word.length()));   
            }
        }

        return tally;
    }

    public void giveHint() {
        Random rand = new Random();
        hints = new ArrayList<String>(word.length());
        
        int r;
        boolean check = true;

        while (true) {
            r = rand.nextInt(word.length());

            for (int i = 0; i < hints.size(); i++) {
                if (hints.get(i).equalsIgnoreCase(String.valueOf(word.charAt(r)))) {
                    check = false;
                    break;
                }
            }

            if (check) {
                if (!Character.isLetter(key.charAt(r))) {
                    break;
                }
            }
        }

        hints.add(String.valueOf(word.charAt(r)));

        HINTS++;
        System.out.println("Your hint is the letter \'" + word.charAt(r) + "\'.");
    }
}
