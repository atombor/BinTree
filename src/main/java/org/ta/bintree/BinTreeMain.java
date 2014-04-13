package org.ta.bintree;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import org.ta.bintree.node.NodeImpl;

/**
 *
 * @author doki
 */
public class BinTreeMain {


	public static void main(String[] args) {
	
		System.out.println("Enter node string:");
		NodeImpl rootNode = new NodeImpl();

		try {
			rootNode.load(new InputStreamReader(System.in));
			
		}
		catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		catch (ParseException pe) {
			System.out.println(pe.getMessage());
		}
		
	}
	
}
