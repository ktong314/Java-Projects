

package assignment5;



public class Critter2 extends Critter{
	
	//this critter is smart and avoids other critters except clovers
	@Override
	public String toString() {
        return "2";
    }
	
	@Override
	public CritterShape viewShape() {
		return CritterShape.DIAMOND;
	}
	
	@Override
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.BLUE;
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
    	}
    	if(!moved) {
    		for(int i = 0; i < 16; i++) { //if there are no clovers surrounding it, look for empty spaces to go to
        		int dir = Critter.getRandomInt(8);
        		if(look(dir, false) == null) {
        			moved = true;
        			walk(dir);
        			break;
        		}
        	}
    	}
    	if(getEnergy() < 25) { //if critter is about to die reproduce
    		int dir = Critter.getRandomInt(8);
    		Critter1 child = new Critter1();
	    	reproduce(child, dir);
    	}
	}

	@Override
	public boolean fight(String oponent) {
		if (oponent.equals("@")) {
			return true;
		}
		return false;
	}

}
