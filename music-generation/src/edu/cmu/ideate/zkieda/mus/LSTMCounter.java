package edu.cmu.ideate.zkieda.mus;

import java.util.HashMap;
import java.util.Iterator;

public class LSTMCounter {
	public int level; // level into the tree (root = highest numbered level)
	public HashMap<String, LSTMCounter> map; // links to child nodes, each link
												// is the next word
	public double count; // leaf node's count for an n-gram

	// For Good Turing Smoothing Counts
	public double gtcount; // leaf nodes' good-turing count for an n-gram
	public HashMap<Double, Double> numberOfLSTMsWithCount; // // The number of
															// ngrams that occur
															// x times

	public LSTMCounter(int level, HashMap<Double, Double> numberOfLSTMsWithCount) {
		this.level = level;
		this.numberOfLSTMsWithCount = numberOfLSTMsWithCount;

		if (level == 0) {
			// There are no links to child nodes, we are a leaf node
			this.map = null;
			// Set the initial count to 0.
			this.count = 0.0;
		} else {
			// We are not a leaf node, set up child node link hash.
			this.map = new HashMap<String, LSTMCounter>();
		}
	}

	public double insert(String[] ngram) {
		// Keep track of level 1 counts
		if (level == 1) {
			count++;
		}

		// Recursive base case - If this is a leaf, increment the count
		if (level == 0) {
			count++;
			return count;
		}

		// Recursive step - Find/create the next node to travel to and recurse
		LSTMCounter next;
		if (map.containsKey(ngram[ngram.length - level])) {
			next = map.get(ngram[ngram.length - level]);
		} else {
			next = new LSTMCounter(level - 1, numberOfLSTMsWithCount);
			map.put(ngram[ngram.length - level], next);
		}

		return next.insert(ngram);
	}

	public double count(String[] ngram) {
		// Recursive base case - If this is a leaf, return the count
		if (level == 0) {
			return count;
		}

		// Recursive step - Find the next node to travel to and recurse
		if (!map.containsKey(ngram[ngram.length - level])) {
			return 0.0; // we never saw this n-gram, so the count is 0.
		}
		return map.get(ngram[ngram.length - level]).count(ngram);
	}

	public double level1Count(String[] ngram) {
		// Recursive base case - One level above leaf nodes, sum all counts of
		// leaf nodes
		if (level == 1) {
			return count;
		}

		// Recursive step - Find the next node to travel to and recurse
		if (!map.containsKey(ngram[ngram.length - level])) {
			return 0; // we never saw this n-gram
		}
		return map.get(ngram[ngram.length - level]).level1Count(ngram);
	}

	public String generateNextWord(String[] ngram) {
		// Recursive base case - One level above leaf nodes, find a random next
		// word based on counts
		if (level == 1) {
			double totalCountForLevel = level1Count(ngram);

			// Generate a random distance into the counts to take a word from
			double rand = Math.random() * totalCountForLevel;

			// Go through the possible words and see how far our random number
			// gets us
			Iterator<String> i = map.keySet().iterator();
			String nextWord = "";
			while (i.hasNext() && rand >= 0) {
				nextWord = i.next();
				rand -= map.get(nextWord).count(ngram);
			}

			return nextWord;
		}

		// Recursive step - Find the next node to travel to and recurse
		return map.get(ngram[ngram.length - level]).generateNextWord(ngram);
	}

	// Generate Good Turing Counts based on original counts and the
	// numberOfLSTMsWithCount map
	public void makeGoodTuringCounts() {
		// One level above leaf nodes, do the same as for any other non-leaf,
		// but set the level 1 gtcount
		if (level == 1) {
			gtcount = 0;
			for (LSTMCounter ngc : map.values()) {
				ngc.makeGoodTuringCounts();
				gtcount += ngc.gtcount;
			}
			return;
		}

		// On leaf level, set the gtcount
		if (level == 0) {
			if (!numberOfLSTMsWithCount.containsKey(count + 1)) {
				numberOfLSTMsWithCount.put(count + 1, 0.0);
			}
			// c* = (c+1) * N(c+1) / N(c)
			gtcount = (count + 1) * (numberOfLSTMsWithCount.get(count + 1.0)) / (numberOfLSTMsWithCount.get(count));
			return;
		}

		// Recursive step - Recurse to each child
		for (LSTMCounter ngc : map.values()) {
			ngc.makeGoodTuringCounts();
		}
	}

	public double gtcount(String[] ngram) {
		// Recursive base case - If this is a leaf, return the count
		if (level == 0) {
			return gtcount;
		}

		// Recursive step - Find the next node to travel to and recurse
		if (!map.containsKey(ngram[ngram.length - level])) {
			return 0.0; // we never saw this n-gram, just return 0
		}
		return map.get(ngram[ngram.length - level]).gtcount(ngram);
	}

	public double level1GTCount(String[] ngram) {
		// Recursive base case - One level above leaf nodes, sum all counts of
		// leaf nodes
		if (level == 1) {
			return gtcount;
		}

		// Recursive step - Find the next node to travel to and recurse
		if (!map.containsKey(ngram[ngram.length - level])) {
			return 0.0; // we never saw this n-gram, just return 0
		}
		return map.get(ngram[ngram.length - level]).level1GTCount(ngram);
	}
}