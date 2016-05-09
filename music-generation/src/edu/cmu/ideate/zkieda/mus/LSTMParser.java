package edu.cmu.ideate.zkieda.mus;

import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class LSTMParser {

	public Element rootElement;
	public HashSet<String> samples = new HashSet<String>();
	public boolean useUpperCase;

	public static void main(String[] args) {
		LSTMParser p = new LSTMParser("data/fbistest.xml");
		HashSet<String> x = p.parse();
		System.out.println(x.toArray()[0]);
		System.out.println();
		System.out.println(x.toArray()[1]);
	}

	public LSTMParser(String filename) {
		this(filename, false);
	}

	public LSTMParser(String filename, boolean useUpperCase) {
		// initializing the xml parser
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(filename);
			rootElement = doc.getDocumentElement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.useUpperCase = useUpperCase;
	}

	public HashSet<String> parse() {
		NodeList docnodes = rootElement.getElementsByTagName("DOC");
		for (int i = 0; i < docnodes.getLength(); i++) {
			Element doc = (Element) docnodes.item(i);
			Node text = doc.getElementsByTagName("TEXT").item(0);
			Node paragraph = text.getLastChild();
			samples.addAll(Arrays.asList(parseTextNode(paragraph)));
		}
		return samples;
	}

	private String[] parseTextNode(Node node) {
		String text = node.getNodeValue();
		text = text.replaceAll("\\.\\s", "\\.\\. ");
		text = text.trim();
		if (!useUpperCase) {
			text = text.toLowerCase();
		}
		String[] textArray = text.split("\\.\\s");
		return textArray;
	}
}