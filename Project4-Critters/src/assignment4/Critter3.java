package assignment4;
		
		//this critter will not attack its own species
public class Critter3 extends Critter{
	 	@Override
	    public void doTimeStep() 
	    {
	 		int dir = Critter.getRandomInt(8);
	    	walk(dir);
	    }

	    @Override
	    public boolean fight(String opponent) 
	    {
	    	if(opponent.equals("X")) {
	    		walk(Critter.getRandomInt(8));
	    		return false;
	    	}	    	
	    	return true;
	    }

	    @Override
	    public String toString() 
	    {
	        return "X";
	    }

}
