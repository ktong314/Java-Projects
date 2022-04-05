package assignment4;

import assignment4.Critter;

/**
 * Critter that acts like a bomber of sorts
 * 
 * This critter tries to produce offspring each time before it fights
 * so that its bloodline will live on
 */
public class Critter1 extends Critter {

    @Override
    public void doTimeStep() 
    {
    	int dir = Critter.getRandomInt(8);
    	walk(dir);
    }

    @Override
    public boolean fight(String opponent) 
    {
    	Critter1 bomb = new Critter1();
    	reproduce(bomb, Critter.getRandomInt(8));
        return true;
    }

    @Override
    public String toString() 
    {
        return "B";
    }
}
