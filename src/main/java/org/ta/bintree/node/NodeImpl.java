/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ta.bintree.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void load(Reader input) throws IOException, ParseException {

	    BufferedReader bufferRead = new BufferedReader(input);
	    String descr = bufferRead.readLine();
		
		parseNodeDescription(descr);
		
	}

	private void parseNodeDescription(String descr) throws ParseException {
		// (rootName,(level1LeftName,,),(level1RightName,(level2RightName,,),))
		if (descr != null && descr.length() > 4) {
			//rootNode
			final int firstCommaIndex = descr.indexOf(",");
			this.name = descr.substring(descr.indexOf("(")+1, firstCommaIndex);
			System.out.println("Name:" + this.name);

			
			// LEFT 			
			String leftNodeDescr = null;
			String restDescr = descr.substring(firstCommaIndex+1, descr.lastIndexOf(")"));
			
			final int leftBracketStart = restDescr.indexOf("(");
			final int nextComma = restDescr.indexOf(",");
			if (nextComma < leftBracketStart || leftBracketStart < 0) {
				// left value is not a node
				this.left = null;
				System.out.println("Name:" + this.name + " - left Node is empty.");
			}
			else {
				leftNodeDescr = restDescr.substring(leftBracketStart, restDescr.indexOf(")")+1);
				this.left = new NodeImpl(leftNodeDescr);
/*
				if (leftNodeDescr.length() > 0) {
				}
				else {
					System.out.println("Name:" + this.name + " - left description is empty.");
				}
*/				
			}

			//RIGHT 
			System.out.println(restDescr);
			int rightDescrStart = (leftNodeDescr == null)? 1 : leftNodeDescr.length()+1;
			restDescr = restDescr.substring(rightDescrStart);
			int rightBracketEnd = restDescr.lastIndexOf(")")+1;
			if (rightBracketEnd > 0) {
				String rightNodeDescr = restDescr.substring(rightDescrStart, rightBracketEnd);
				if (rightNodeDescr.length() > 0) {
					this.right = new NodeImpl(rightNodeDescr);
				}
			}
			else {
				this.right = null;
				System.out.println("Name:" + this.name + " - right Node is empty.");
			}
			
			
			
			
		} 
		else {
			throw new ParseException("Invalid description: " + descr, 0);
		}
	}

/*	
	private NodeImpl(String name, Node leftNode, Node rightNode) {
		//TODO: validation
	}
*/	
	public NodeImpl() {
		
	}
	
	private NodeImpl(String description) throws ParseException {
		System.out.println("New Node with descreption: " + description);
		
		parseNodeDescription(description);
	}
	
	@Override
	public void save(Writer output) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
