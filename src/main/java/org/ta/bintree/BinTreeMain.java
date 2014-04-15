package org.ta.bintree;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import org.ta.bintree.node.NodeImpl;

/**
 * Asks input string from the console, and gives it to the NodeImpl class
 * to parse, and build the binary tree.
 * In the end it lists the node names, in the same order as they are in the input.
 * 
 * @author Tombor Attila
 */
public class BinTreeMain {


	public static void main(String[] args) {

		NodeImpl rootNode = new NodeImpl();
		System.out.println("Enter node string:");

		try {
			rootNode.load(new InputStreamReader(System.in));
			
			System.out.println("\nResult of Node save:");
			rootNode.save(new OutputStreamWriter(System.out));
		}
		catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		catch (ParseException pe) {
			System.err.println(pe.getMessage());
		}
		
	}
	
}
