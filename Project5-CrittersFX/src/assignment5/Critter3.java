

package assignment5;


public class Critter3 extends Critter{
	//this critter is smart and avoids other critters except clovers
	@Override
	public String toString() {
        return "3";
    }
	
	@Override
	public CritterShape viewShape() {
		return CritterShape.TRIANGLE;
	}
	
	@Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return javafx.scene.paint.Color.YELLOW;
    }

	@Override
	public void doTimeStep() {
		boolean moved = false;
    	for(int i = 0; i < 8; i++) { //the critter will look if there are any clovers surrounding it
    		if(look(i, false) == "@") {
    			walk(i);
    			moved = true;
    			break; 			
    		}
    		if(look(i, true) == "@") {
    			run(i);
    			moved = true;
    			break; 			
    		}		
    	}
    	if(!moved) {
    		for(int i =0; i < 16; i++) { //if there are no clovers surrounding it, look for any other critter
        		int dir = Critter.getRandomInt(8);
        		if(look(dir, true) != null) {
        			moved = true;
        			run(dir);
        			break;
        		}
        		if(look(dir, false) != null) {
        			moved = false;
        			walk(dir);
        			break;
        		}
        	}
    	}
    	if(!moved) { //if no critters
    		int dir = Critter.getRandomInt(8);
    		walk(dir);
    	}
	}

	@Override
	public boolean fight(String oponent) {
		return true;
	}
}
