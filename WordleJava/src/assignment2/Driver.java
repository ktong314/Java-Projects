package assignment2;

import java.util.Arrays;
import java.util.ArrayList;

import java.util.Scanner;

public class Driver {
	
	public static class Game {
		static ArrayList<Character> green = new ArrayList<>();
		static ArrayList<Character> yellow = new ArrayList<>();
		static ArrayList<Character> absent = new ArrayList<>();
		static char[] unused = new char[]{'a','b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		static int[] check = new int[26];
		
		public static void runGame(GameConfiguration config, Scanner in){
			Dictionary dict = new Dictionary("4_letter_words.txt");
			if (config.wordLength == 5) {
				dict = new Dictionary("5_letter_words.txt");
			} else if (config.wordLength == 6) {
				dict = new Dictionary("6_letter_words.txt");
			}
			System.out.println("Hello! Welcome to Wordle.");
			while (true) {
				green = new ArrayList<>();
				yellow = new ArrayList<>();
				absent = new ArrayList<>();
				check = new int[26];
				unused = new char[]{'a','b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
				System.out.println("Do you want to play a new game? (y/n)");
				String s1 = in.nextLine();
				if (s1.equals("y")) {
					String word = dict.getRandomWord();
					//word = "energy";
					for(int i = 0; i < word.length(); i++) {
						check[word.charAt(i)-97] += 1;
					}
					
					int guesses = config.numGuesses;
					if (config.testMode) {
						System.out.println(word);
					}
					String[] pastGuesses = new String[config.numGuesses + 1];
					int zz = 0;
					while (guesses > 0) {
						
						System.out.println("Enter your guess:");
						String guess = in.nextLine();
						if (guess.equals("[history]")) {
							printHistory(pastGuesses, word, dict);
							continue;
						}
						if (dict.containsWord(guess)){
							pastGuesses[zz] = guess;
							zz++;
						}
						if (guess.length() != config.wordLength) {
							System.out.println("This word has an incorrect length. Please try again.");
							continue;
						}
						if (!dict.containsWord(guess)) {
							System.out.println("This word is not in the dictionary. Please try again.");
							continue;
						}
						boolean win = wordCheck(guess, word);
						if (win) {
							System.out.println("Congratulations! You have guessed the word correctly.");
							
							break;
						}

						if (guesses-- > 1) {
							System.out.println("You have " + guesses + " guess(es) remaining.");
						} else {
							System.out.println("You have run out of guesses.");
							System.out.println("The correct word was \"" + word + "\".");
							
							
							break;
						}

					}
				} else if (s1.equals("n")) {
					return;
				}
			}
		}
		
		public static boolean wordCheck(String guess, String word) {
			char[] lol = new char[guess.length()];
			int[] character = new int[26];
			
			for(int i = 0; i < word.length(); i++) {
				character[word.charAt(i)-97] += 1;
			}
			for(int i = 0; i < guess.length(); i++) {
				if(guess.charAt(i) == word.charAt(i)) {
					lol[i] = 'G';
					character[word.charAt(i)-97] -= 1;
					if(!green.contains(guess.charAt(i)) || check[word.charAt(i)-97] > 0) {
						green.add(guess.charAt(i));
						unused[guess.charAt(i)-97] = '0';
						check[word.charAt(i)-97] -= 1;
					}
					if(yellow.contains(guess.charAt(i)) && check[word.charAt(i)-97] == 0) {
						yellow.remove(new Character(guess.charAt(i)));
					}
					if(absent.contains(guess.charAt(i))) {
						absent.remove(new Character(guess.charAt(i)));
					}
				}
			}
			
			for (int i = 0; i < guess.length(); i++) {
				for (int j = 0; j < word.length(); j++) {
					if(lol[i] == 'G') {
						continue;
					}
					if (guess.charAt(i) == word.charAt(j) && character[word.charAt(j)-97] > 0) {
						character[word.charAt(j)-97] -= 1;
						lol[i] = 'Y';
						if(!yellow.contains(guess.charAt(i))) {
							if(!green.contains(guess.charAt(i)) || check[guess.charAt(i)-97] > 0) {
								yellow.add(guess.charAt(i));
								unused[guess.charAt(i)-97] = '0';
							}
						}
						if(absent.contains(guess.charAt(i))) {
							absent.remove(new Character(guess.charAt(i)));
						}
						break;
					} else {
						lol[i] = '_';
						if(unused[guess.charAt(i)-97] != '0') {
							absent.add(guess.charAt(i));
							unused[guess.charAt(i)-97] = '0';
						}
					}

				}
			}
			

			boolean correct = true;
			for (int i = 0; i < guess.length(); i++) {
				if (lol[i] != 'G')
					correct = false;
				System.out.print(lol[i]);
			}
			System.out.println();
			if (correct) {
				return true;
			}
			return false;
		}

		public static void printHistory(String[] history, String word, Dictionary dict) {
			for (int i = 0; i < history.length; i++) {
				if (history[i] != null) {
					if (dict.containsWord(history[i])) {
						System.out.print(history[i] + "->");
						printCheck(history[i], word);
					}
				}
			}
			System.out.println("--------");
			System.out.print("Green: ");
			for(int i = 0; i < green.size(); i++) {
				System.out.print(green.get(i));
				if(i < green.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();

			System.out.print("Yellow: ");
			for(int i = 0; i < yellow.size(); i++) {
				System.out.print(yellow.get(i));
				if(i < yellow.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
			
			System.out.print("Absent: ");
			for(int i = 0; i < absent.size(); i++) {
				System.out.print(absent.get(i));
				if(i < absent.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
			
			System.out.print("Unused: ");
			for(int i = 0; i < unused.length; i++) {
				if(unused[i] != '0') {
					System.out.print(unused[i]);
					if(i < unused.length - 1) {
						System.out.print(", ");
					}
				}
			}
			System.out.println();
		}
		
		public static void printCheck(String guess, String word) {
			char[] lol = new char[guess.length()];
			int[] character = new int[26];
			
			for(int i = 0; i < word.length(); i++) {
				character[word.charAt(i)-97] += 1;
			}
			for(int i = 0; i < guess.length(); i++) {
				if(guess.charAt(i) == word.charAt(i)) {
					lol[i] = 'G';
					character[word.charAt(i)-97] -= 1;
				}
			}
			
			for (int i = 0; i < guess.length(); i++) {
				for (int j = 0; j < word.length(); j++) {
					if(lol[i] == 'G') {
						continue;
					}
					if (guess.charAt(i) == word.charAt(j) && character[word.charAt(j)-97] > 0) {
						character[word.charAt(j)-97] -= 1;
						lol[i] = 'Y';
						break;
					} else {
						lol[i] = '_';
					}

				}
			}
			

			for (int i = 0; i < guess.length(); i++) {
				System.out.print(lol[i]);
			}
			System.out.println();	
		}
	}
	public static void start(GameConfiguration config) {
		// TODO: complete this method
		// We will call this method from our JUnit test cases.
		Scanner in = new Scanner(System.in);
		Game.runGame(config, in);
	}
	

	public static void main(String[] args) {
		// Use this for your testing. We will not be calling this method.
		GameConfiguration game1 = new GameConfiguration(6, 9, true);
		start(game1);
	}
}
