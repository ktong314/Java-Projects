

package assignment4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* 
 * See the PDF for descriptions of the methods and fields in this
 * class. 
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    private int energy = 0;

    private int x_coord;
    private int y_coord;

    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name) throws InvalidCritterException 
    {
        // TODO: Complete this method
        Critter newCritter = checkCritter(critter_class_name);
    	newCritter.energy = Params.START_ENERGY;
    	newCritter.x_coord = getRandomInt(Params.WORLD_WIDTH);
    	newCritter.y_coord = getRandomInt(Params.WORLD_HEIGHT);
    	population.add(newCritter);
    }
    
      /*
     * Check to if the Critter exists,
     * throw an exception if it doesnt,
     * return the Critter type if it does
     */
    private static Critter checkCritter(String name) throws InvalidCritterException
    {
    	try
    	{
    		return (Critter) Class.forName(myPackage + "." + name).newInstance();
    	}
    	catch(InstantiationException | IllegalAccessException | ClassNotFoundException e)
    	{
    		throw new InvalidCritterException("InvalidCritterException");
    	}
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *        Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        // TODO: Complete this method
        ArrayList<Critter> typeList = new ArrayList<Critter>();
    	Critter critterType = checkCritter(critter_class_name);
    	for(int i = 0; i < population.size(); i++)
    	{
    		if(critterType.getClass().isInstance(population.get(i)))
    		{
    			typeList.add(population.get(i));
    		}
    	}
        return typeList;
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        if(!(population.isEmpty()))
        {
            population.clear();
        }
    }

    public static void worldTimeStep() {
    	for(int i = 0; i < population.size(); i++) {
    		population.get(i).doTimeStep();
    		population.get(i).energy = population.get(i).energy - Params.REST_ENERGY_COST;
    		if(population.get(i).energy < 1) {
    			population.remove(i);
    			i--;
    		}
    	}
    	
    	for(int i = 0; i < population.size(); i++) { //this is the doEncounters() method
    		for(int j = i + 1; j < population.size(); j++) {
    			if((population.get(i).x_coord == population.get(j).x_coord) && (population.get(i).y_coord == population.get(j).y_coord)) {
    				boolean iFight = population.get(i).fight(population.get(j).toString());
    				boolean jFight = population.get(j).fight(population.get(i).toString());
    				Critter iCritter = population.get(i);
    				Critter jCritter = population.get(j);
    				if(iCritter.x_coord == jCritter.x_coord && iCritter.y_coord == jCritter.y_coord && jCritter.energy > 0 && iCritter.energy > 0) {
    					int ipower, jpower;
    					if(iFight) {
    						ipower = getRandomInt(iCritter.energy);
    					} else {
    						ipower = 0;
    					}
    					if(jFight) {
    						jpower = getRandomInt(jCritter.energy);
    					} else {
    						jpower = 0;
    					}
    					if(ipower >= jpower) { //critter j dies
    						iCritter.energy = iCritter.energy + jCritter.energy/2;
    						population.remove(j);
    						j--;
    					} else { //critter i dies
    						jCritter.energy = jCritter.energy + iCritter.energy/2;
    						population.remove(i);
    						i--;
    						break;
    					}
    				}
    				if (jCritter.energy <= 0) { 
						population.remove(j);
						j--;
					}
					if (iCritter.energy <= 0) {
						population.remove(i);
						i--;
						break;
					}
    			}
    		}
    	}
    	
    	
    	for (int i = 0; i < Params.REFRESH_CLOVER_COUNT; i++) { //creates Clovers
			try {
				createCritter("Clover");
			} catch (InvalidCritterException e) {
				e.toString();
			}
		}
    	
    	for(int i = 0; i < babies.size(); i++) {//adds babies to population
    		population.add(babies.get(i));
    	}
    	babies.clear();
    	
    }

    public static void displayWorld() {
    	String[][] world;
    	world = createBoundaries();
    	for(int i = 0; i < population.size(); i++) {
    		world[population.get(i).y_coord + 1][population.get(i).x_coord + 1] = population.get(i).toString();
    	}
    	for(int y = 0; y < world.length; y++) {
    		for(int x = 0; x < world[0].length; x++) {
    			System.out.print(world[y][x]);
    		}
    		System.out.println();
    	}
    }
    
    private static String[][] createBoundaries() {
    	String[][] boundaries = new String[Params.WORLD_HEIGHT + 2][Params.WORLD_WIDTH + 2];
    	for(int y = 0; y < boundaries.length; y++) {
    		for(int x = 0; x < boundaries[0].length; x++) {
    			if((y == 0 && x == 0) || (y == 0 && x == boundaries[0].length - 1) || (y == boundaries.length - 1 && x == 0) || (y == boundaries.length - 1 && x == boundaries[0].length - 1)) {
    				boundaries[y][x] = "+";
    			}
    			else if(y == 0 || y == boundaries.length - 1) {
    				boundaries[y][x] = "-";
    			}
    			else if(x == 0 || x == boundaries[0].length - 1) {
    				boundaries[y][x] = "|";
    			}
    			else {
    				boundaries[y][x] = " ";
    			}
    		}
    	}
		return boundaries;
	}


    /**
     * Prints out how many Critters of each type there are on the
     * board.
     *
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }

    protected final void walk(int direction) 
    {
        // TODO: Complete this method
	energy -= Params.WALK_ENERGY_COST;
    	move(direction, 1);		//walk moves 1 position
    }

    protected final void run(int direction) 
    {
        // TODO: Complete this method
	energy -= Params.RUN_ENERGY_COST;
    	move(direction, 2);		//run moves 2 positions
    }
	
    private void move(int direction, int num)
    {
    	if(direction == 0)			//right
    	{
    		x_coord += num;
    	}
    	else if(direction == 1)		//upper right
    	{
    		x_coord += num;
    		y_coord += num;
    	}
    	else if(direction == 2)		//up
    	{
    		y_coord += num;
    	}
    	else if(direction == 3)		//upper left
    	{
    		x_coord -= num;
    		y_coord += num;
    	}
    	else if(direction == 4)		//left
    	{
    		x_coord -= num;
    	}
    	else if(direction == 5)		//lower left
    	{
    		x_coord -= num;
    		y_coord -= num;
    	}
    	else if(direction == 6)		//down
    	{
    		y_coord -= num;
    	}
    	else if(direction == 7)		//lower right
    	{
    		x_coord += num;
    		y_coord -= num;
    	}
    	else
    	{
    		//TODO: might need an error message if direction is not 1-7, dunno
    	}
    	
    	if(x_coord > (Params.WORLD_WIDTH - 1))		//wrap around to the left if x is too far right
    	{
    		x_coord -= (Params.WORLD_WIDTH - 1);
    	}
    	if(x_coord < 0)								//wrap around to the right
    	{
    		x_coord += (Params.WORLD_WIDTH - 1);
    	}
    	if(y_coord > (Params.WORLD_HEIGHT - 1))		//wrap around to the bottom
    	{
    		y_coord -= (Params.WORLD_HEIGHT - 1);
    	}
    	if(y_coord < 0)								//wrap around to the top
    	{
    		y_coord += (Params.WORLD_HEIGHT - 1);
    	}
    }

    protected final void reproduce(Critter offspring, int direction) 
    {
        // TODO: Complete this method
	if(energy < Params.MIN_REPRODUCE_ENERGY)
    	{
    		return;
    	}
    	
    	offspring.energy = (int) Math.floor(energy / 2);		//energy of the offspring is half of the parent rounded down
    	energy = (int) Math.ceil(energy / 2);					//energy of the parent is half of the parent rounded up
    	offspring.x_coord = x_coord;						//initial position of offspring is same as parent
    	offspring.y_coord = y_coord;
    	offspring.move(direction, 1);
    	babies.add(offspring);
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
