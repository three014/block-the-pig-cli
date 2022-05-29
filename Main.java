import java.util.Scanner;
import java.util.Random;

class Main {

    static final int cellCount = 55;

    public static void main(String[] args) {
        printWelcomeScreen();
    }

    public static void printWelcomeScreen() {
        Scanner sc = new Scanner(System.in);
        char q = 'q';
        System.out.println("----------------- BLOCK THE PIG -----------------\n");
        System.out.print("Press any key to start, or press q to exit: ");
        if (q != sc.next().charAt(0)) {
            startGame(sc);
        }
    }

    public static boolean customRandom() {
        Random r = new Random();

        if (r.nextInt(1000) <= 236) {
            return true;
        }
        return false;
    }

    public static void startGame(Scanner sc) {
        int numPlayerTurns;
        int score;
        int cellInitCounter = 0;
        int cellLine = 0;
        // initializing the cells
        HexCell[] cell = new HexCell[cellCount];
        for (int i = 0; i < cell.length; i++) {
            if ((i % 5 == 0) && (i != 0)) {
				cellLine++;
            }
            cell[i] = new HexCell(i, cellLine);
            if ((customRandom() == true) && (i != (cell.length / 2)) && (cellInitCounter < 14)) {
                cell[i].block();
                cellInitCounter++;
            }
        }
        // initializing the pig
        PigEntity pig = new PigEntity(cell);
    
        numPlayerTurns = 0;
        cell[pig.getPigPosition()].pigAction(1);

        while (true) {
            updateGameScreen(cell, numPlayerTurns);
            cell[grabPlayerChoice(sc, cell)].block();
            numPlayerTurns++;
            score = pig.deliberate(cell);
            if (score == 1) { // returns 1 if it's currently sitting on border cell
                cell[pig.getPrevPigPosition()].pigAction(0);
                cell[pig.getPigPosition()].pigAction(1);
                updateGameScreen(cell, numPlayerTurns);
                System.out.println();
                System.out.println("PIG ESCAPED, YOU LOSE D:");
                break;
            }
            else if (score == 2) { // returns 2 if it's surrounded by blocked cells
                updateGameScreen(cell, numPlayerTurns);
                System.out.println();
                System.out.println("YOU WON :D");
                break;
            }
            else {
                cell[pig.getPrevPigPosition()].pigAction(0);
                cell[pig.getPigPosition()].pigAction(1);
            }
        }
    }
	
	public static int grabPlayerChoice(Scanner sc, HexCell[] cell) {
        int selection;

        System.out.print("Enter next position to block: ");
        selection = sc.nextInt();
        while (true) {
            if ((selection < 0) || (selection >= cell.length)) {
                System.out.println("Invalid choice.");
            }
            else if (cell[selection].isBlocked()) {
                System.out.println("Cell is already blocked.");
            }
            else {
                break;
            }
            System.out.print("Enter next position to block: ");
            selection = sc.nextInt();
        }
        
        return selection;
	}

    public static void updateGameScreen(HexCell[] cell, int turns) {
        int line = 0;

        for (int i = 0; i < 50; i++) { // Flushing the screen with newlines to make it look blank
            System.out.println();
        }
        System.out.println("Number of Player Turns: " + turns);
        System.out.println();
        for (int i = 0; i < cell.length; i++) { 
            if ((i % 5 == 0) && (i != 0)) {
				System.out.println();
				line++;
				if (line % 2 != 0) {
				System.out.print("     ");
			    }
			}
			System.out.print(cell[i].index()); 
			System.out.print("       ");
        }
        System.out.println("\n");
    }
}