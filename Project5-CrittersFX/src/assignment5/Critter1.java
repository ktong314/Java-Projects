

package assignment5;



public class Critter1 extends Critter{
	
	//bomb critter
	@Override
	public String toString() {
        return "1";
    }
	
	@Override
	public CritterShape viewShape() {
		return CritterShape.STAR;
	}
	
	@Override
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.RED;
    }

	@Override
	public void doTimeStep() {
		int dir = Critter.getRandomInt(8);
		boolean adjacent = false;
    	for(int i = 0; i < 8; i++) { //the critter will look if there are any surrounding critters and go towards it
    		if(look(i, false) != null) {
    			walk(i);
    			adjacent = true;
    			break;
    		}
    	}
    	if(!adjacent)//if there are no adjacent critters it will just walk in a random direction
    		walk(dir);
	}

	@Override
	public boolean fight(String oponent) {
		if(!oponent.equals("@")) {
			for(int i = 0; i < 8; i++) { //the critter will look if there are any empty areas around it and reproduce there
	    		if(look(i, false) == null) {
	    			Critter1 bomb = new Critter1();
	    	    	reproduce(bomb, i);
	    			break;
	    		}
	    	}
		}
        return true;
	}

}
