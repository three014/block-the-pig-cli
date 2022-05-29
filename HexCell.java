import java.util.HashMap;

class HexCell {
    
	private String cellIndex;
    private boolean isBlocked = false;
    private boolean isPigSittingOnIt = false;
    private boolean isBorder;
    private int line;
    private HashMap<String, Integer> surroundingCells = new HashMap<String, Integer>(6);
	
	public HexCell(int index, int line) { // initializing the cell
		this.cellIndex = String.format("%03d", index);
        this.line = line;

        if ( (index <= 9) || (index % 10 == 0) || ((index + 1) % 10 == 0) || (index >= 45) ) {
            this.isBorder = true;
        }
        else {
            this.isBorder = false;

            // link non-border cell to surrounding cells to create a linked grid of some sorts
            if (line % 2 == 1) {
                this.surroundingCells.put("north",     index - 10);
                this.surroundingCells.put("northwest", index -  5);
                this.surroundingCells.put("northeast", index -  4);
                this.surroundingCells.put("southwest", index +  5);
                this.surroundingCells.put("southeast", index +  6);
                this.surroundingCells.put("south",     index + 10);
            }
            else if (line % 2 == 0) {
                this.surroundingCells.put("north",     index - 10);
                this.surroundingCells.put("northwest", index -  6);
                this.surroundingCells.put("northeast", index -  5);
                this.surroundingCells.put("southwest", index +  4);
                this.surroundingCells.put("southeast", index +  5);
                this.surroundingCells.put("south",     index + 10);
            }
        }
	}
	
    // set of methods to return data about this cell
	public String index() {
		if (this.isBlocked) {
            return "---";
        }
        if (this.isPigSittingOnIt) {
            return "PIG";
        }
        return this.cellIndex;
	}
    public int getLine() {
        return this.line;
    }
    public int getSurroundingCells (String direction) {
        return this.surroundingCells.get(direction);
    }
    public boolean isBlocked() {
        if (this.isBlocked || this.isPigSittingOnIt) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isBorder() {
        if (this.isBorder == true) {
            return true;
        }
        else {
            return false;
        }
    }
    
    // set of methods to perform actions on this cell
    public void block() {
        this.isBlocked = true;
    }
    public void pigAction(int action) {
        if (action == 1) { // pig is currently sitting on cell
            this.isPigSittingOnIt = true;
        }
        else if (action == 0) { // pig is no longer sitting on cell
            this.isPigSittingOnIt = false;
        }
    }
}