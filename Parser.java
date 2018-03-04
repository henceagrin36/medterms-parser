/*input
*/

import java.util.*;
import java.io.*;

public class Parser {

	private static List<Word>[] words;

	private static final int NOUN = 0;
	private static final int ADJ = 1;

	private static final int NOUN_1 = 1;
	private static final int NOUN_2 = 2; 
	private static final int NOUN_3 = 3;
	private static final int NOUN_4 = 4;
	private static final int NOUN_5 = 5;

	private static final int ADJ_1_2 = 6;
	private static final int ADJ_3 = 7;

	public static class Word {
		int word_type;
		String latin;
		int declension;
		String gender;
		String definition;

		public Word(int type, String latin, int declension, String gender, String definition) {
			this.word_type = type;
			this.latin = latin;
			this.declension = declension;
			this.gender = gender;
			this.definition = definition;
		}

		public String toString() {
			String res = latin + "\t";
			switch (declension) {
				case NOUN_1:
					res += "1st";
					break;
				case NOUN_2:
					res += "2nd";
					break;
				case NOUN_3:
					res += "3rd";
					break;
				case NOUN_4:
					res += "4th";
					break;
				case NOUN_5:
					res += "5th";
					break;
				case ADJ_1_2:
					res += "1st/2nd";
					break;
				case ADJ_3:
					res += "3rd";
					break;
				default:
					res += "unknown";
					break;
			}
			res += "\t";
			if (!gender.equals("")) {
				res += gender + "\t";
			}
			res += definition;
			return res;
		}
	}

	public static void main(String[] args) {
		words = new List[2];
		words[0] = new ArrayList<Word>();
		words[1] = new ArrayList<Word>();

		Scanner in = new Scanner(System.in);
		
		while (in.hasNextLine()) {
			String line = in.nextLine();
			parse(line);
		}

		System.out.println();
		for (Word noun : words[0]) {
			System.out.println(noun);
		}
		System.out.println();
		for (Word adj : words[1]) {
			System.out.println(adj);
		}
	}

	private static void parse(String line) {
		line = line.replaceAll(",", "");
		line = line.replaceAll("\\s+"," ");
		line = line.replaceAll("\\(.*?\\)", "");
		line = line.trim();
		String[] word = line.split(" ");

		//System.out.println(Arrays.deepToString(word));

		try {
			String latin = word[0];

			List<String> declensions = new ArrayList<>();
			declensions.add(word[1]);
			declensions.add(word[2]);
			int[] declension = findDeclension(declensions);

			if (declension[0] == NOUN) {
				String gender = findGender(word[2]);
				String definition = word[3];

				words[0].add(new Word(declension[0], latin, declension[1], gender, definition));
			} else if (declension[0] == ADJ) {
				String definition;
				if (declension[1] == ADJ_1_2) {
					definition = word[3];
				} else {
					definition = word[2];
				}

				words[1].add(new Word(declension[0], latin, declension[1], "", definition));
			} else {
				System.err.println("unknown word type");
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.println("misformatted word");
		}
	}

	private static int[] findDeclension(List<String> declensions) {
		String a = declensions.get(0);
		String b = declensions.get(1);

		if (a.substring(a.length() - 2).equals("ae")) {
			return new int[] {NOUN, NOUN_1};
		} 
		else if (a.substring(a.length() - 1).equals("i")) {
			return new int[] {NOUN, NOUN_2};
		} 
		else if (a.substring(a.length() - 2).equals("is")) {
			return new int[] {NOUN, NOUN_3};
		} 
		else if (a.substring(a.length() - 2).equals("us")) {
			return new int[] {NOUN, NOUN_4};
		} 
		else if (a.substring(a.length() - 2).equals("ei")) {
			return new int[] {NOUN, NOUN_5};
		} 
		else if (a.substring(a.length() - 1).equals("a") 
			&& b.substring(b.length() - 2).equals("um")) {
			return new int[] {ADJ, ADJ_1_2};
		} 
		else if (a.substring(a.length() - 1).equals("e")) {
			return new int[] {ADJ, ADJ_3};
		} 
		else {
			return new int[] {-1, -1};
		}
	}

	private static String findGender (String s) {
		char c = s.charAt(0);
		switch (c) {
			case 'm':
				return "masculine";
			case 'f':
				return "feminine";
			case 'n':
				return "neuter";
			default:
				return "unknown";
		}
	}
}