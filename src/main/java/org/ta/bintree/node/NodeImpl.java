package org.ta.bintree.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Iterator;

/**
 * Implements the Node interface to parse text input, build the binary tree,
 * and makes it possible to write the node names to a given output using an iterator.
 * 
 * @author Tombor Attila
 */
public class NodeImpl implements Node {

	private String name;
	private Node left;
	private Node right;
	private static final String COMMA = ",";
	private static final String BRACKET_OPENING = "(";
	private static final String BRACKET_CLOSING = ")";
	private static final char BRACKET_OPENING_CHAR = BRACKET_OPENING.charAt(0);
	private static final char BRACKET_CLOSING_CHAR =  BRACKET_CLOSING.charAt(0);
	
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Node getLeft() {
		return this.left;
	}

	@Override
	public void setLeft(Node left) {
		this.left = left;
	}

	@Override
	public Node getRight() {
		return this.right;
	}

	@Override
	public void setRight(Node right) {
		this.right = right;
	}

	@Override
	public Iterator<Node> widthIterator() {
		return new NodeIterator(this);
	}

	@Override
	public void load(Reader input) throws IOException, ParseException {
	    BufferedReader bufferRead = new BufferedReader(input);
	    String descr = bufferRead.readLine();
		
		parseNodeDescription(descr);
	}
	
	@Override
	public void save(Writer output) throws IOException {
		Iterator<Node> nodeIterator = widthIterator();
		
		while (nodeIterator.hasNext()) {
			output.write(nodeIterator.next().getName() + " ");
		}
		output.write("\n");
		output.flush();
	}	

	/**
	 * Simple constructor to create the root element. The root node object need
	 * to start the parser with its the load method.
	 */
	public NodeImpl() {
		//System.out.println("Root node created.");
	}
	
	/**
	 * Constructor for internal use of the class, to create child elements 
	 * dynamically.
	 * 
	 * @param description part of the input string that represents one node
	 * @throws ParseException 
	 */
	private NodeImpl(String description) throws ParseException {
		//System.out.println("New node with description: " + description);
		parseNodeDescription(description);
	}
	
	private void parseNodeDescription(String descr) throws ParseException {
		if (descr != null && descr.length() > 4) {
			
			descr = descr.replaceAll("\\s","");
			
			// Node Name
			final int firstCommaIndex = descr.indexOf(COMMA);
			try {
				this.name = descr.substring(descr.indexOf(BRACKET_OPENING)+1, firstCommaIndex);
				//System.out.println(" - name: " + this.name);

				// left node
				String leftNodeDescr = parseLeftDescriptor(descr);
				if (leftNodeDescr == null) {
					//System.out.println(" - " + this.name + ": left Node is empty.");
				}
				else {
					this.left = new NodeImpl(leftNodeDescr);
				}

				// right node
				String rightNodeDescr = parseRightDescriptor(descr, this.name, leftNodeDescr);
				if (rightNodeDescr == null) {
					//System.out.println(" - " + this.name + ": right Node is empty.");
				}
				else {
					this.right = new NodeImpl(rightNodeDescr);
				}
			}
			catch (StringIndexOutOfBoundsException e) {
				throw new ParseException("Invalid description: " + descr, 0);
			}
			
		} 
		else {
			throw new ParseException("Invalid description: " + descr, 0);
		}
	}

	private static String parseLeftDescriptor(String descr) throws ParseException {
		String leftNodeDescr = null;
		final int firstCommaIndex = descr.indexOf(COMMA);

		final int leftBracketStart = descr.indexOf(BRACKET_OPENING, 1);
		final int nextComma = descr.indexOf(COMMA, firstCommaIndex+1);
		if (nextComma < leftBracketStart || leftBracketStart < 0) {
		}
		else {
			leftNodeDescr = descr.substring(leftBracketStart, findClosingBracket(descr)+1);
		}
		return leftNodeDescr;
	}
	
	private static int findClosingBracket(String descr) throws ParseException {
		int closeIndex = 0;
		int numberOfOpenings = 0;
		int numberOfClosings = 0;
		
		char[] descrArr = descr.toCharArray();

		int i = 1;
		boolean beforeClosingBracket = true;
		while (beforeClosingBracket && i < descrArr.length) {
			char actChar = descrArr[i];
			if (actChar == BRACKET_OPENING_CHAR) {
				numberOfOpenings++;
			}
			else if (actChar == BRACKET_CLOSING_CHAR) {
				beforeClosingBracket = ++numberOfClosings != numberOfOpenings;
				closeIndex = i;
			}
			i++;
		}
		if (numberOfOpenings != numberOfClosings) {
			throw new ParseException("Error in node descriptor format: " + descr, 0);
		}
		
		return closeIndex;
	}
	
	private static String parseRightDescriptor(String descr, String name, String leftNodeDescr) {
		String rightNodeDescr = null;

		int rightDescrStart = name.length()+3;
		rightDescrStart += (leftNodeDescr == null)? 0 : leftNodeDescr.length();
		final int rightBracketEnd = descr.lastIndexOf(BRACKET_CLOSING);
		
		if (rightBracketEnd > rightDescrStart) {
			rightNodeDescr = descr.substring(rightDescrStart, rightBracketEnd);
		}
		return rightNodeDescr;
	}
}

/**
 * Iterator that is able to go through the whole binary tree (if started from
 * the root node).
 * Next value is the actual node, than the left child node, finally the right ones.
 * 
 * @author Tombor Attila
 */
class NodeIterator implements Iterator<Node>{

	private final Node node;
	private boolean isNextTheLocalNode = true;
	private Iterator<Node> leftIterator = null;
	private Iterator<Node> rightIterator = null;
	
	NodeIterator(Node node) {
		this.node = node;
		if (node.getLeft() != null) {
			 this.leftIterator = node.getLeft().widthIterator();
		}
		if (node.getRight()!= null) {
			 rightIterator = node.getRight().widthIterator();
		}
		
	}
	
	private Iterator<Node> getActIterator() {
		if (leftIterator != null && leftIterator.hasNext()) {
			return leftIterator;
		}
		else if (rightIterator != null && rightIterator.hasNext()) {
			return rightIterator;
		}
		return null;
	}

	@Override
	public boolean hasNext() {
		if (isNextTheLocalNode) {
			return true;
		}
		else {
			Iterator<Node> actIterator = getActIterator();
			if (actIterator != null) {
				return actIterator.hasNext();
			}
		}
		return false;
	}

	@Override
	public Node next() {
		if (isNextTheLocalNode) {
			this.isNextTheLocalNode = false;
			return this.node;
		}
		else {
			Iterator<Node> actIterator = getActIterator();
			if (actIterator != null) {
				return actIterator.next();
			}
			return null;
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove is not supported."); 
	}
	
}