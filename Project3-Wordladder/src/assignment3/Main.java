/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Kevin Tong>
 * <kyt259>
 * <17360>
 * <Kevin Pang>
 * <kwp535>
 * <17360>
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/sp-22-assignment-3-sp22-pr3-pair-11
 * Spring 2022
 */


package assignment3;
import java.util.*;
import java.io.*;
import java.lang.*;

public class Main {
	
	// static variables and constants only here.
	static Set<String> dictionary;			//set for all possible words
	static ArrayList<String> usedWordsDFS;		//arraylist for all the used words in DFS
	static ArrayList<ArrayList<String>> possibleLadders;
	static ArrayList<String> ladder;
	static ArrayList<String> input;
	static String in;
	static String out;

	static class NodeBFS {
		public String word;
		public NodeBFS previous;
		
		public NodeBFS(String word, NodeBFS previous) {
			this.word = word;
			this.previous = previous;
		}
	}
	

	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
		input = parse(kb);
		
		ArrayList<String> BFS = getWordLadderBFS(input.get(0), input.get(1));
		System.out.println("BFS Word Ladder");
		printLadder(BFS);
		System.out.println();
		
		ArrayList<String> DFS = getWordLadderDFS(input.get(0), input.get(1));
		System.out.println("DFS Word Ladder");
		printLadder(DFS);
		System.out.println();
	}
	
	public static void initialize() {
		
		dictionary = makeDictionary();		//make the dictionary
		usedWordsDFS = new ArrayList<>();
		possibleLadders = new ArrayList<>();
		ladder = new ArrayList<>();
		input = new ArrayList<>();
		
	}
		
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		input.clear();
		int counter = 2;
		while(counter > 0) {
			String word = keyboard.next();
			if(!(dictionary.contains(word.toUpperCase())))
				return null;
			if(word.equals("/quit"))
				return null;
			input.add(word.toLowerCase());
			counter--;
		}
		in = input.get(0);
		out = input.get(1);
		return input;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		ladder.clear();
		HashMap<String, ArrayList<String>> map = new HashMap<>();
		HashMap<String, Integer> depth = new HashMap<>();
		
		for(String s : dictionary) {
			map.put(s.toLowerCase(), new ArrayList<String>()); 
		}
		
		Queue<String> queue = new LinkedList<>();
		queue.add(start);
		depth.put(start, 0);
		
		while(!queue.isEmpty()) { //this entire while loop fills out the map with neighbors. the map 
			//the value will be an array of strings that have a 1 letter difference from the key
			boolean finished = false;
			for(int i = 0; i < queue.size(); i++) {
				String current = queue.remove();
				
				ArrayList<String> neighbors = new ArrayList<>();
				char word[] = current.toCharArray();
				for(char c = 'a'; c <= 'z'; c++) {
					for(int j = 0; j < word.length; j++) {
						if(word[j] == c) continue;
						char orig = word[j];
						word[j] = c;
						if(dictionary.contains(String.valueOf(word).toUpperCase())) {
							neighbors.add(String.valueOf(word));
						}
						word[j] = orig;
					}
					
				}
				
				for(String neighbor : neighbors) {
					map.get(current).add(neighbor); //the value will be an array of strings that have a 1 letter difference from the key
					if (!depth.containsKey(neighbor)) {
						depth.put(neighbor, depth.get(current) + 1);
						if(end.equals(neighbor)) {
							finished = true;
						}
						else {
							queue.offer(neighbor);
						}
					}
				}
				if(finished) {
					break;
				}
				
			}
		}
		
		dfs(start, end, map, depth);
		
		int minLen = Integer.MAX_VALUE;
		int lowestInd = 0;
		
		for(int i = 0; i < possibleLadders.size(); i++) { //this finds the smallest ladder out of all possible ladders
			if(possibleLadders.get(i).size() < minLen) {
				minLen = possibleLadders.get(i).size();
				lowestInd = i;
			}
		}
		
		if (possibleLadders.isEmpty())
		{
			in = start;
			out = end;
			return null;
		}
		ladder = possibleLadders.get(lowestInd);
		
		in = start;
		out = end;
		return ladder; // replace this line later with real return
	}
	
	public static void dfs(String start, String end, HashMap<String, ArrayList<String>> map, HashMap<String, Integer> depth) {
		//this method uses dfs to find all possible word ladders
		
		ladder.add(start);
		if(end.equals(start)) {
			possibleLadders.add(new ArrayList<String>(ladder));
		} else {
			for(String next: map.get(start)) {
				if(depth.get(next) == depth.get(start) + 1) {
					dfs(next, end, map, depth);				
				}
			}
		}
		ladder.remove(ladder.size() - 1);
		
	}
	
	
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
	
    	ladder.clear();
    	LinkedList<NodeBFS> queue = new LinkedList<>();
    	Set<String> unused = new HashSet<>();
    	unused.addAll(dictionary);
    	
    	NodeBFS NodeBFS = new NodeBFS(start, null);			//creating the head (no parent)
    	queue.add(NodeBFS);
    	while(!queue.isEmpty()) 					//keep going while there are nodes in the queue
		{
    		NodeBFS current = queue.remove();
    		
    		for(int i = 0; i < current.word.length(); i++) 
			{
    			char c = current.word.charAt(i);
    			char[] currentWord = current.word.toCharArray();
				for(char j = 'a'; j <= 'z'; j++)
				{
                    		if(j == c)
		    			{
                			continue;
                    		}
                    		currentWord[i] = j;					//change the letter at index i to the character in j
                    		String modifiedWord = String.valueOf(currentWord);
 
                    		if(modifiedWord.equals(end))
		    			{
          
                    			ladder = new ArrayList<>();
                        		ladder.add(end);
                        		NodeBFS currentNode = current;
                        		while(currentNode != null)			//backtrack until we reach start
						{
                            			ladder.add(0, currentNode.word);
                            			currentNode = currentNode.previous;		//add the backracked word to the beginning of ladder
                        		}
                        		return ladder;
                      
                    		}
 
                    		if(unused.contains(modifiedWord.toUpperCase()))		//if modifiedWord has not been seen before
		    			{
                        		NodeBFS node = new NodeBFS(modifiedWord, current);	//create a new node using modifiedWord
                        		queue.add(node);					//add that node to the queue
                       	 		unused.remove(modifiedWord.toUpperCase());		//remove modifiedWord from unused
                    		}
                	}
    		}	
    	}
    	if(ladder.isEmpty()) 
	{
		in = start;
		out = end;
    		return null;
    	}
	in = start;
	out = end;
    	return ladder;
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		if((ladder == null) || ladder.isEmpty())
		{
			System.out.println("no word ladder can be found between " + in + " and " + out);
			return;
		}
		for(int i = 0; i < ladder.size(); i++)
		{
			System.out.println(ladder.get(i));
		}	
	}
	// TODO
	// Other private static methods here


	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
