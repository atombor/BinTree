package org.ta.bintree.node;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Iterator;

/**
 *
 * @author doki
 */
public interface Node {

	String getName();

	void setName(String name);

	Node getLeft();

	void setLeft(Node node);

	Node getRight();

	void setRight(Node node);

	Iterator<Node> widthIterator();

	void load(Reader input) throws IOException, ParseException;

	void save(Writer output) throws IOException;
}
