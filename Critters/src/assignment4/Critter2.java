package assignment4;

import assignment4.Critter.TestCritter;

/**
 * Critter that acts on its whims
 * 
 * This critter can only move turn in angles of
 * 90 degrees, so it can only move up, down, left, or right.
 * 
 * If its feeling up to it, the critter will reproduce after moving
 * If its feeling brave, the critter will fight, otherwise it'll run away
 */
public class Critter2 extends Critter {

    @Override
    public void doTimeStep() 
    {
    	int dir = Critter.getRandomInt(8);
    	while((dir % 2) != 0)
    	{
    		dir = Critter.getRandomInt(8);
    	}
    	walk(dir);
    	
    	if(Critter.getRandomInt(100) % 3 == 0)
    	{
    		Critter2 whim = new Critter2();
    		reproduce(whim, Critter.getRandomInt(8));
    	}
    }

    @Override
    public boolean fight(String opponent) 
    {
    	int rand = Critter.getRandomInt(100);
    	if(rand % 5 == 0)
    	{
    		return true;
    	}
    	else
    	{
            int temp = Critter.getRandomInt(8);
    		while(temp % 2 != 0)
    		{
    			temp = Critter.getRandomInt(8);
    		}
    		run(temp);
    		return false;
    	}
    }

    @Override
    public String toString() 
    {
        return "W";
    }
}
