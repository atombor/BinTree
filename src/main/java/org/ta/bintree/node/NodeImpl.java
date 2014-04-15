package org.ta.bintree.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Iterator;

/**
 *
 * @author doki
 */
public class NodeImpl implements Node {

	private String name;
	private Node left;
	private Node right;
	
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

	private void parseNodeDescription(String descr) throws ParseException {
		if (descr != null && descr.length() > 4) {
			//TODO: more syntax check
			descr = descr.replaceAll("\\s","");
			//Node Name
			final int firstCommaIndex = descr.indexOf(",");
			try {
				this.name = descr.substring(descr.indexOf("(")+1, firstCommaIndex);
				System.out.println("Name:" + this.name);

				//LEFT
				String leftNodeDescr = parseLeftDescriptor(descr);
				if (leftNodeDescr == null) {
					System.out.println(this.name + " --- left Node is empty.");
				}
				else {
					this.left = new NodeImpl(leftNodeDescr);
				}

				//RIGHT 
				String rightNodeDescr = parseRightDescriptor(descr, this.name, leftNodeDescr);
				if (rightNodeDescr == null) {
					System.out.println(this.name + " --- right Node is empty.");
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

	public NodeImpl() {
	}
	
	private NodeImpl(String description) throws ParseException {
		System.out.println(" - NEW NODE with description: " + description);
		parseNodeDescription(description);
	}

	private static String parseLeftDescriptor(String descr) throws ParseException {
		String leftNodeDescr = null;
		final int firstCommaIndex = descr.indexOf(",");
		//String restDescr = descr.substring(firstCommaIndex+1, descr.lastIndexOf(")"));

		final int leftBracketStart = descr.indexOf("(", 1);
		final int nextComma = descr.indexOf(",", firstCommaIndex+1);
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
		
		//(rootName,(level1LeftName,(level2LeftName1,,),(level2RightName1,,)),(level1RightName2,(level2LeftName,,),))
		char[] descrArr = descr.toCharArray();

		int i = 1;
		boolean beforeClosingBracket = true;
		while (beforeClosingBracket && i < descrArr.length) {
			char actChar = descrArr[i];
			if (actChar == '(') {
				numberOfOpenings++;
			}
			else if (actChar == ')') {
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
		final int rightBracketEnd = descr.lastIndexOf(")");
		if (rightBracketEnd > rightDescrStart) {
			rightNodeDescr = descr.substring(rightDescrStart, rightBracketEnd);
		}
		return rightNodeDescr;
	}
}

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