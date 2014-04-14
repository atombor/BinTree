package org.ta.bintree.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Iterator;
import jdk.nashorn.internal.objects.NativeArray;

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
			output.flush();
		}
	}	

	private void parseNodeDescription(String descr) throws ParseException {
		if (descr != null && descr.length() > 4) {
			//TODO: more syntax check
			descr = descr.replaceAll("\\s","");
			//Node Name
			final int firstCommaIndex = descr.indexOf(",");
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

	private static String parseLeftDescriptor(String descr) {
		String leftNodeDescr = null;
		int firstCommaIndex = descr.indexOf(",");
		String restDescr = descr.substring(firstCommaIndex+1, descr.lastIndexOf(")"));

		final int leftBracketStart = restDescr.indexOf("(");
		final int nextComma = restDescr.indexOf(",");
		if (nextComma < leftBracketStart || leftBracketStart < 0) {
		}
		else {
			leftNodeDescr = restDescr.substring(leftBracketStart, restDescr.indexOf(")")+1);
		}
		return leftNodeDescr;
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

	Node node;
	private Iterator<Node> leftIterator = null;
	private Iterator<Node> rightIterator = null;
	
	NodeIterator(Node node) {
		System.out.println("ITERATE: " + node.getName());
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
		Iterator<Node> actIterator = getActIterator();
		if (actIterator != null) {
			return actIterator.hasNext();
		}
		return false;
	}

	@Override
	public Node next() {
		Iterator<Node> actIterator = getActIterator();
		if (actIterator != null) {
			return actIterator.next();
		}
		return null;
	}
	
}