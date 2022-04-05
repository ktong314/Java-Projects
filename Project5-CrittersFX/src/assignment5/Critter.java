

package assignment5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * See the PDF for descriptions of the methods and fields in this
 * class.
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {
	static int animating = 0;
	static int animationSteps = 0;
	static VBox commands1;
	static GridPane world1;
	static Text animeStats1;
	static Text animeStats2;

    /* START --- NEW FOR PROJECT 5 */
    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }

    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.WHITE;
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();

    protected final String look(int direction, boolean steps) 
    {
        int distance = 1;
    	int x = x_coord;
    	int y = y_coord;
    	energy -= Params.LOOK_ENERGY_COST;
    	if(steps)
    	{
    		distance = 2;
    	}
    	
    	//see where we are looking
    	if(direction == 0)			//right
    	{
    		x += distance;
    	}
    	else if(direction == 1)		//upper-right
    	{
    		x += distance;
    		y += distance;
    	}
    	else if(direction == 2)		//up
    	{
    		y += distance;
    	}
    	else if(direction == 3)		//upper-left
    	{
    		x -= distance;
    		y += distance;
    	}
    	else if(direction == 4)		//left
    	{
    		x -= distance;
    	}
    	else if(direction == 5)		//lower-left
    	{
    		x -= distance;
    		y -= distance;
    	}
    	else if(direction == 6)		//down
    	{
    		y -= distance;
    	}
    	else if(direction == 7)		//lower-right
    	{
    		x += distance;
    		y -= distance;
    	}
    	
    	for(int i = 0; i < population.size(); i++)
    	{
    		int populationX = population.get(i).x_coord;
    		int populationY = population.get(i).y_coord;
    		
    		if((populationX == x) && (populationY == y))
    		{
    			return population.get(i).toString();
    		}
    	}
    	
        return null;
    }

    public static String runStats(List<Critter> critters) {
        // TODO Implement this method
    	Map<String, Integer> specificNumber = new HashMap<String, Integer>();
        String stats = "";
    	if(critters.size() == 0)
        {
        	stats = "There are " + critters.size() + " critters. ";
        }
        else if(critters.size() == 1)
        {
        	stats = "There is " + critters.size() + " critter: ";

        }
        else
        {
        	stats = "There are " + critters.size() + " critters: ";
        }
    	for(int i = 0; i < critters.size(); i++)
    	{
    		Critter thisCritter = critters.get(i);
    		String critterString = thisCritter.toString();
    		
    		specificNumber.put(critterString, specificNumber.getOrDefault(critterString, 0) + 1);
    	}
    	
    	String comma = "";
    	for(String temp : specificNumber.keySet())
    	{
    		stats += comma + temp + ":" + specificNumber.get(temp);
    		comma = ", ";
    	}
    	
        return stats;
    }


    public static void displayWorld(GridPane pane) {
		Main.stage.setTitle("Critters 5");
    	Scene scene = new Scene(pane, 1800, 1000);
    	Main.stage.setScene(scene);
    	Main.stage.show();
    	GridPane world = new GridPane();
    	GridPane ui = new GridPane();
    	pane.add(world, 0, 0);
		pane.add(ui, 1000, 0);
		paintUI(ui, world);
		paintWorld(world);
    }
    
    private static void paintUI(GridPane ui, GridPane world) {
    	ui.setStyle("-fx-font-family: Times New Roman;");
    	
    	VBox btnbox = new VBox();
		btnbox.setPrefSize(160, 800);
		//btnbox.setStyle("-fx-background-color: #00000e");
		
		VBox commands = new VBox();
		commands.setPrefSize(120, 800);
		//commands.setStyle("-fx-background-color: #55555e");
    	
		VBox empty = new VBox();
		empty.setPrefSize(50, 800);
		//empty.setStyle("-fx-background-color: #55555e");
		
		
    	Button critters = new Button();
		critters.setText("Critters");
		critters.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				CrittersCommand(commands, world);
			}

		});
		
		Button step = new Button();
		step.setText("Step");
		step.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				StepCommand(commands, world);
			}

		});
		
		
		
		Button stats = new Button();
		stats.setText("Stats");
		stats.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event)
			{
				statsCommand(commands, world);
			}

		});
		Button animate = new Button();
		animate.setText("Animation");
		animate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) 
			{
				animationCommand(commands, world, critters, step, stats, animate);
			}

		});
		Button quit = new Button();
		quit.setText("Quit");
		quit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}

		});
		
		HBox c = new HBox(critters); c.setMinSize(160, 150);
		critters.setMinSize(160, 100); critters.setStyle("-fx-background-color: #0000FF; -fx-text-fill:#ecf0f1;");
		HBox st = new HBox(step); st.setMinSize(160, 150);
		step.setMinSize(160, 100); step.setStyle("-fx-background-color: #0000FF; -fx-text-fill:#ecf0f1;");
		HBox s = new HBox(stats); s.setMinSize(160, 150);
		stats.setMinSize(160, 100); stats.setStyle("-fx-background-color: #0000FF; -fx-text-fill:#ecf0f1;");
		HBox a = new HBox(animate); a.setMinSize(160, 150);
		animate.setMinSize(160, 100); animate.setStyle("-fx-background-color: #0000FF; -fx-text-fill:#ecf0f1;");
		HBox q = new HBox(quit); q.setMinSize(160, 150);
		quit.setMinSize(160, 100); quit.setStyle("-fx-background-color: #8B0000; -fx-text-fill:#ecf0f1;");
		
		
		
		btnbox.getChildren().add(c);
		btnbox.getChildren().add(st);
		btnbox.getChildren().add(s);
		btnbox.getChildren().add(a);
		btnbox.getChildren().add(q);
		ui.add(btnbox, 0, 0);
		ui.add(empty, 1, 0);
		ui.add(commands, 5, 0);
    }
    
    private static void CrittersCommand(VBox commands, GridPane world) {
    	commands.getChildren().clear();
    	
    	TextField input = new TextField("1");
    	
    	Text instructions = new Text("Enter how many Critters to create. \nChoose a species to create.");
    	
    	Button clover = new Button();
		clover.setText("Clover");
		clover.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { 
				
				try {
					int num = Integer.parseInt(input.getText());
					for(int i = 0; i < num; i++) {
						createCritter("Clover");
					}
					paintWorld(world);
				} catch (InvalidCritterException | NumberFormatException e) {
					e.printStackTrace();
				}
			}

		});
    	
    	
    	Button goblin = new Button();
		goblin.setText("Goblin");
		goblin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { 
				
				try {
					int num = Integer.parseInt(input.getText());
					for(int i = 0; i < num; i++) {
						createCritter("Goblin");
					}
					paintWorld(world);
				} catch (InvalidCritterException | NumberFormatException e) {
					e.printStackTrace();
				}
			}

		});
		
		Button critter1 = new Button();
		critter1.setText("Critter1");
		critter1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { 
				
				try {
					int num = Integer.parseInt(input.getText());
					for(int i = 0; i < num; i++) {
						createCritter("Critter1");
					}
					paintWorld(world);
				} catch (InvalidCritterException | NumberFormatException e) {
					e.printStackTrace();
				}
			}

		});
		
		Button critter2 = new Button();
		critter2.setText("Critter2");
		critter2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { 
				
				try {
					int num = Integer.parseInt(input.getText());
					for(int i = 0; i < num; i++) {
						createCritter("Critter2");
					}
					paintWorld(world);
				} catch (InvalidCritterException | NumberFormatException e) {
					e.printStackTrace();
				}
			}

		});
		
		Button critter3 = new Button();
		critter3.setText("Critter3");
		critter3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { 
				
				try {
					int num = Integer.parseInt(input.getText());
					for(int i = 0; i < num; i++) {
						createCritter("Critter3");
					}
					paintWorld(world);
				} catch (InvalidCritterException | NumberFormatException e) {
					e.printStackTrace();
				}
			}

		});
		
		Button clear = new Button();
		clear.setText("Clear World");
		clear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { 
				
				clearWorld();
				paintWorld(world);
			}

		});
		
		Text inputInstructions = new Text("Choose species by name (case-sensitive):");
		TextField nameInput = new TextField("Clover");
		Button unknown = new Button();
		unknown.setText("Add critter");
		unknown.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { 
				
				try {
					int num = Integer.parseInt(input.getText());
					String unknownName = nameInput.getText();
//					unknownName = unknownName.toLowerCase();
//					int length = unknownName.length();
//					char[] letters = new char[unknownName.length()];
//					letters = unknownName.toCharArray();
//					letters[0] = Character.toUpperCase(letters[0]);
//					unknownName = "";
//					for(int i = 0; i < length; i++)
//					{
//						unknownName += letters[i];
//					}
										
					for(int i = 0; i < num; i++) {
						createCritter(unknownName);
					}
					paintWorld(world);
				} catch (InvalidCritterException | NumberFormatException e) {
					e.printStackTrace();
				}
			}

		});
		
		commands.getChildren().add(instructions);
		commands.getChildren().add(input);
		commands.getChildren().add(clover);
		commands.getChildren().add(goblin);
		commands.getChildren().add(critter1);
		commands.getChildren().add(critter2);
		commands.getChildren().add(critter3);
		commands.getChildren().add(clear);
		
		commands.getChildren().add(inputInstructions);
		commands.getChildren().add(nameInput);
		commands.getChildren().add(unknown);
    }
    
    private static void StepCommand(VBox commands, GridPane world) {
    	commands.getChildren().clear();
    	Text instructions = new Text("Enter how many steps to take.");
    	
    	TextField input = new TextField("1");
    	
    	Button step = new Button();
		step.setText("Step");
		step.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { 
				
				try {
					int num = Integer.parseInt(input.getText());
					for(int i = 0; i < num; i++) {
						worldTimeStep();
					}
					paintWorld(world);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

		});
		
		commands.getChildren().add(instructions);
		commands.getChildren().add(input);
		commands.getChildren().add(step);
    	
    }
    
    private static void statsCommand(VBox commands, GridPane world)
    {
    	commands.getChildren().clear();
    	Text input = new Text("Current critter stats:");
    	Text output = new Text(runStats(population));
    	
    	commands.getChildren().add(input);
    	commands.getChildren().add(output);
    }
    
    private static void animationCommand(VBox commands, GridPane world, Button critters, Button steps, Button stats, Button animate)
    {
    	//TODO
    	commands.getChildren().clear();
    	commands1 = commands;
    	world1 = world;
    	AnimationTimer tm = new TimerMethod();
       	Text instructions = new Text("Enter how many steps to take every second.");
       	Text instructions2 = new Text("Stop the animation before pressing any other button.");
    	TextField input = new TextField("1");    	
    	animeStats1 = new Text("Current critter stats:");
    	animeStats2 = new Text(runStats(population));    	
    	
    	Button step = new Button();
		step.setText("Animate");
		step.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) { 
				
				try {
					
					animationSteps = Integer.parseInt(input.getText());
					
					if(animating == 0)
					{
						animating++;
						input.setDisable(true);
						critters.setDisable(true);
						steps.setDisable(true);
						stats.setDisable(true);
						animate.setDisable(true);
						step.setText("Stop animating");
						tm.start();
					}
					else
					{
						animating = 0;
						input.setDisable(false);
						critters.setDisable(false);
						steps.setDisable(false);
						stats.setDisable(false);
						animate.setDisable(false);
						step.setText("Animate");
						tm.stop();
					}
				} 
				catch (NumberFormatException e) 
				{
					e.printStackTrace();
				} 
			}
			

		});
    	
		commands.getChildren().add(instructions);
		commands.getChildren().add(instructions2);
		commands.getChildren().add(input);
		commands.getChildren().add(step);
		commands.getChildren().add(animeStats1);
		commands.getChildren().add(animeStats2);
    	
    }
    
    private static class TimerMethod extends AnimationTimer
    {
    	private long lastUpdate = 0;
    	@Override
    	public void handle(long now)
    	{
    		if(now - lastUpdate >= 1_000_000_000)
    		{
    			lastUpdate = now;
    			for(int i = 0; i < animationSteps; i++)
    			{
    				Critter.worldTimeStep();
    			}
    			
    			animeStats2.setText(runStats(population));
    			paintWorld(world1);
    		}
    	}
    }
    
	private static void paintGrid(GridPane world) {
		for (int i = 0; i < Params.WORLD_WIDTH; i++) {
			for (int j = 0; j < Params.WORLD_HEIGHT; j++) {
				Shape grid = new Rectangle(920/Params.WORLD_WIDTH, 920/Params.WORLD_HEIGHT);
				grid.autosize();
				grid.setFill(Color.rgb(0, 0, 0));
				grid.setStroke(Color.rgb(255, 255, 255));
				world.add(grid, i, j);
			}
		}
	}
    
    private static void paintWorld(GridPane world) {
    	world.getChildren().clear();
    	//System.out.println("test");
    	paintGrid(world);
		double x;
		if(800/Params.WORLD_WIDTH > 800/Params.WORLD_HEIGHT){
			x = 800/Params.WORLD_HEIGHT;
		} else {
			x = 800/Params.WORLD_WIDTH;
		}
		double z = x * 0.8;
		double y = x * 0.9;
		for(int i = 0; i < population.size(); i++) {
			Shape sprite = null;
			switch (population.get(i).viewShape()) {
			case CIRCLE:
				sprite = new Circle(x/2);
				break;
			case TRIANGLE:
				sprite = new Polygon();
				((Polygon) sprite).getPoints().addAll(
						y / 2.00, 0.0,
						0.0, y,
						y, y
				);
				break;
			case SQUARE:
				sprite = new Rectangle(x, x);
				break;
			case STAR:

				sprite = new Polygon();
				((Polygon) sprite).getPoints().addAll(
						z / 2.0, -(z / 12.0),
						z / 1.6, z / 4.0,
						z, z / 4.0,
						z / 1.33, z / 2.0,
						z / 1.2, z / 1.2,
						z / 2.0, z / 1.6,
						z / 6.0, z / 1.2,
						z / 4.0, z / 2.0,
						0.0, z / 4.0,
						z / 2.67, z / 4.0
						);
				break;
			case DIAMOND:
				sprite = new Polygon();
				((Polygon) sprite).getPoints().addAll(
						y / 2.0, 0.0,
						y, y /2.0,
						y / 2.0, y,
						0.0, y / 2.0
						);
				break;
			}
			sprite.setFill(population.get(i).viewFillColor());
			sprite.setStroke(population.get(i).viewOutlineColor());
			world.add(sprite, population.get(i).x_coord, population.get(i).y_coord);
		}
    }
    
    

	/* END --- NEW FOR PROJECT 5
			rest is unchanged from Project 4 */

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
     * critter_class_name must be the qualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
    	Critter newCritter = checkCritter(critter_class_name);
    	newCritter.energy = Params.START_ENERGY;
    	newCritter.x_coord = getRandomInt(Params.WORLD_WIDTH);
    	newCritter.y_coord = getRandomInt(Params.WORLD_HEIGHT);
    	population.add(newCritter);
    }
    
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
     *                           Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {
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

    protected final void walk(int direction) {
    	energy -= Params.WALK_ENERGY_COST;
    	move(direction, 1);	
    }

    protected final void run(int direction) {
    	energy -= Params.RUN_ENERGY_COST;
    	move(direction, 2);

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

    protected final void reproduce(Critter offspring, int direction) {
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
