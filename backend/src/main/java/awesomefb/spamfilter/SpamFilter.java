package awesomefb.spamfilter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Huy
 *
 */
public class SpamFilter {

	private static SpamFilter instance = null;

	private String fileNameHamData = "datatraining/ham.data";
	private String fileNameSpamData = "datatraining/spam.data";

	private HashMap<String, Word> words; // A HashMap to keep track of all words
	private String splitregex; // How to split the String into tokens
	private Pattern wordregex; // Regex to eliminate junk (although we really should welcome the junk)

	SpamFilter() {
		words = new HashMap<String, Word>();
		splitregex = "\\W";
		wordregex = Pattern.compile("\\w+");

		train();
	}

	// Singleton
	public static SpamFilter getInstance() {
		if (instance == null)
			instance = new SpamFilter();
		return instance;
	}

	// Return value boolean, is cmt a Spam?
	public boolean isSpam(String comment) {
			return analyze(comment);
	}

	// Run classification
	void train() {
		
		trainGoodData();
		trainBadData();
		finalizeTraining();
		
	}

	// Train Ham Data
	void trainGoodData() {
		FileReader file;
		try {
			file = new FileReader(fileNameHamData);

			try (BufferedReader reader = new BufferedReader(file)) {

				String line = null;
				int goodTotal = 0;

				while ((line = reader.readLine()) != null) {
					String[] tokens = line.split(splitregex);

					// For every word token
					for (int i = 0; i < tokens.length; i++) {
						String word = tokens[i].toLowerCase();
						Matcher m = wordregex.matcher(word);
						if (m.matches()) {
							goodTotal++;
							// If it exists in the HashMap already
							// Increment the count
							if (words.containsKey(word)) {
								Word w = (Word) words.get(word);
								w.countGood();
								// Otherwise it's a new word so add it
							} else {
								Word w = new Word(word);
								w.countGood();
								words.put(word, w);
							}
						}
					}
				}

				// Go through all the words and divide
				// by total words
				Iterator<Word> iterator = words.values().iterator();
				while (iterator.hasNext()) {
					Word word = (Word) iterator.next();
					word.calcGoodProb(goodTotal);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// Train Spam Data
	void trainBadData() {
		try {
			FileReader file = new FileReader(fileNameSpamData);

			try (BufferedReader reader = new BufferedReader(file)) {

				String line = null;
				int spamTotal = 0;

				while ((line = reader.readLine()) != null) {
					String[] tokens = line.split(splitregex);

					// For every word token
					for (int i = 0; i < tokens.length; i++) {
						String word = tokens[i].toLowerCase();
						Matcher m = wordregex.matcher(word);
						if (m.matches()) {
							spamTotal++;
							// If it exists in the HashMap already
							// Increment the count
							if (words.containsKey(word)) {
								Word w = (Word) words.get(word);
								w.countBad();
								// Otherwise it's a new word so add it
							} else {
								Word w = new Word(word);
								w.countBad();
								words.put(word, w);
							}
						}
					}
				}

				Iterator<Word> iterator = words.values().iterator();
				while (iterator.hasNext()) {
					Word word = (Word) iterator.next();
					word.calcBadProb(spamTotal);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// For every word, calculate the Spam probability
	void finalizeTraining() {
		Iterator<Word> iterator = words.values().iterator();
		while (iterator.hasNext()) {
			Word word = (Word) iterator.next();
			word.finalizeProb();
		}
	}

	// Reference from Paul Graham.
	// This method is derived from Paul Graham:
	// http://www.paulgraham.com/spam.html
	boolean analyze(String comment) {
		// Create an arraylist of 15 most "interesting" words
		// Words are most interesting based on how different their Spam
		// probability is from 0.5
		ArrayList<Word> interesting = new ArrayList<Word>();

		// For every word in the String to be analyzed
		String[] tokens = comment.split(splitregex);

		for (int i = 0; i < tokens.length; i++) {
			String s = tokens[i].toLowerCase();
			Matcher m = wordregex.matcher(s);
			if (m.matches()) {

				Word w;

				// If the String is in our HashMap get the word out
				if (words.containsKey(s)) {
					w = (Word) words.get(s);
					// Otherwise, make a new word with a Spam probability of
					// 0.4;
				} else {
					w = new Word(s);
					w.setPSpam(0.4f);
				}

				// We will limit ourselves to the 15 most interesting word
				int limit = 15;
				// If this list is empty, then add this word in!
				if (interesting.isEmpty()) {
					interesting.add(w);
					// Otherwise, add it in sorted order by interesting level
				} else {
					for (int j = 0; j < interesting.size(); j++) {
						// For every word in the list already
						Word nw = (Word) interesting.get(j);
						// If it's the same word, don't bother
						if (w.getWord().equals(nw.getWord())) {
							break;
							// If it's more interesting stick it in the list
						} else if (w.interesting() > nw.interesting()) {
							interesting.add(j, w);
							break;
							// If we get to the end, just tack it on there
						} else if (j == interesting.size() - 1) {
							interesting.add(w);
						}
					}
				}

				// If the list is bigger than the limit, delete entries
				// at the end (the more "interesting" ones are at the
				// start of the list
				while (interesting.size() > limit)
					interesting.remove(interesting.size() - 1);

			}
		}

		// Apply Bayes' rule (via Graham)
		float pposproduct = 1.0f;
		float pnegproduct = 1.0f;
		// For every word, multiply Spam probabilities ("Pspam") together
		// (As well as 1 - Pspam)
		for (int i = 0; i < interesting.size(); i++) {
			Word w = (Word) interesting.get(i);
			// System.out.println(w.getWord() + " " + w.getPSpam());
			pposproduct *= w.getPSpam();
			pnegproduct *= (1.0f - w.getPSpam());
		}

		// Apply formula
		float pspam = pposproduct / (pposproduct + pnegproduct);
		System.out.println("\nSpam rating: " + pspam);

		// If the computed value is great than 0.9 we have a Spam!!
		if (pspam > 0.9f)
			return true;
		else
			return false;

	}
}
