package assignment4;
	//
	//if this critter is low on energy, it will reproduce
	//this critter will never fight unless against a clover or its own species
	//
public class Critter4 extends Critter{
	public void doTimeStep() 
    {
		if(getEnergy() <= 25) {
			Critter4 survive = new Critter4();
			reproduce(survive, Critter.getRandomInt(8));
		}
 		int dir = Critter.getRandomInt(8);
    	walk(dir);
    }

    @Override
    public boolean fight(String opponent) 
    {
    	if(opponent.equals("@") || opponent.equals("S"))
    		return true;
    	return false;
    }

    @Override
    public String toString() 
    {
        return "S";
    }
}
