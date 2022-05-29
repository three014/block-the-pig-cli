import java.util.HashMap;
import java.util.Random;

class PigEntity {

    private boolean isPigOnBorder = false;
    private int startingCell;
    private int[] cellPosition = new int[2]; // position of the pig; 1 = current position, 2 = previous position
    private HashMap<String, Integer> surroundingCells = new HashMap<String, Integer>(6);
    private String[] direction = {
        "north",
        "northwest",
        "northeast",
        "southwest",
        "southeast",
        "south"
    };
    private void updateSurroundingCells(int line) { // updates the pig's immediately surrounding cells based on a 55-hexagon-cell grid
        if (line % 2 == 1) { 
            this.surroundingCells.put(this.direction[0], this.cellPosition[1] - 10);
            this.surroundingCells.put(this.direction[1], this.cellPosition[1] -  5);
            this.surroundingCells.put(this.direction[2], this.cellPosition[1] -  4);
            this.surroundingCells.put(this.direction[3], this.cellPosition[1] +  5);
            this.surroundingCells.put(this.direction[4], this.cellPosition[1] +  6);
            this.surroundingCells.put(this.direction[5], this.cellPosition[1] + 10);
        }
        else if (line % 2 == 0) {
            this.surroundingCells.put(this.direction[0], this.cellPosition[1] - 10);
            this.surroundingCells.put(this.direction[1], this.cellPosition[1] -  6);
            this.surroundingCells.put(this.direction[2], this.cellPosition[1] -  5);
            this.surroundingCells.put(this.direction[3], this.cellPosition[1] +  4);
            this.surroundingCells.put(this.direction[4], this.cellPosition[1] +  5);
            this.surroundingCells.put(this.direction[5], this.cellPosition[1] + 10);
        }
        
    }

    public PigEntity(HexCell[] cell) { // initializing the pig
        this.startingCell = (cell.length / 2);
        this.cellPosition[1] = this.startingCell;
        this.updateSurroundingCells(cell[this.cellPosition[1]].getLine());
    }

    // self-explanatory
    public int getPigPosition() {
        return this.cellPosition[1];
    }
    public int getPrevPigPosition() {
        return this.cellPosition[0];
    }

    public int deliberate(HexCell[] cell) { // pigAI
        Random r = new Random();
        int tempSwap;
        int tempPos;
        int check;
        int min = 200;
        int line;
        int[] distToBorder = new int[6];
        String closestDir = "";
        
        if (this.cellPosition[1] == this.startingCell) { // if game just started, pig by default moves to the first available position
            while (true) {
                int j = r.nextInt(6);
                if (cell[this.surroundingCells.get(this.direction[j])].isBlocked() == false) {
                    closestDir = this.direction[j];
                    break;
                }
            }
        }
        else {
            check = didPigWin(cell);
            if (check == 2) { // checks if pig got blocked, in which the game ends here with a player win
                return 2;
            }

            for (int i = 0; i < this.direction.length; i++) { // otherwise, pig recalculates distance to closest border every turn
                tempPos = this.surroundingCells.get(this.direction[i]);
                if (cell[tempPos].isBlocked()) { // if the cell directly adjecent to the pig is blocked, then AI heavily discourages pig from choosing that direction
                    distToBorder[i] += 70;
                }
                else {
                    distToBorder[i] += 1;
                }
                while (cell[tempPos].isBorder() == false) { // checks every direction until it reaches a border cell, to calculate distance to each direction's border
                    tempPos = cell[tempPos].getSurroundingCells(this.direction[i]);
                    if (cell[tempPos].isBlocked() == true) {
                        distToBorder[i] += 5;
                    }
                    else {
                        distToBorder[i] += 1;
                    }

                }
            }

            // determines which direction had the least distance between pig and border
            for (int i = 0; i < distToBorder.length; i++) { // calculates the minimum distance that is not in a blocked direction
                if ((distToBorder[i] < min) && (cell[this.surroundingCells.get(this.direction[i])].isBlocked() == false)) {
                    min = distToBorder[i];
                }
            }
            for (int i = 0; i < distToBorder.length; i++) { // matches the minimum distance to its direction (I fixed the problem where the pig would move to a cell that was blocked)
                if ((distToBorder[i] == min) && (cell[this.surroundingCells.get(this.direction[i])].isBlocked() == false)) {
                    closestDir = this.direction[i];
                    break;
                }
            }
        }
        
        // update the cell position
        tempSwap = this.cellPosition[1];
        this.cellPosition[1] = this.surroundingCells.get(closestDir);
        this.cellPosition[0] = tempSwap;
        line = cell[this.cellPosition[1]].getLine();
        this.updateSurroundingCells(line);

        // checks if the pig's move made it win the game 
        check = didPigWin(cell);
        if (check == 1) {
            return 1;
        }
        else { // otherwise, lets Main know that no one has won/lost yet, so game continues
            return 0;
        }
    }

    public int didPigWin(HexCell[] cell) {
        int totalBlockage;
        
        if (cell[this.cellPosition[1]].isBorder()) { // if pig is sittin on border
            return 1;
        }
        else {
            totalBlockage = 0;
            for (String i : this.direction) {
                if (cell[this.surroundingCells.get(i)].isBlocked()) { // if pig got blocked
                    totalBlockage++;
                }
            }
            if (totalBlockage >= 6) {
                return 2;
            }
            else {
                return 0;
            }
        }
    }
    
}