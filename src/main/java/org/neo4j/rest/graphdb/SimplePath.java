package org.neo4j.rest.graphdb;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;

import java.util.Iterator;

/**
 * @author Michael Hunger
 * @since 03.02.11
 */
public class SimplePath implements Path {
    Iterable<Relationship> relationships;
    Iterable<Node> nodes;
    private final Relationship lastRelationship;
    private int length;
    private Node startNode;
    private Node endNode;

    public SimplePath(Node startNode, Node endNode, Relationship lastRelationship, int length, Iterable<Node> nodes, Iterable<Relationship> relationships) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.lastRelationship = lastRelationship;
        this.length = length;
        this.nodes = nodes;
        this.relationships = relationships;
    }

    @Override
    public Node startNode() {
        return startNode;
    }

    @Override
    public Node endNode() {
        return endNode;
    }

    @Override
    public Relationship lastRelationship() {
        return lastRelationship;
    }

    @Override
    public Iterable<Relationship> relationships() {
        return relationships;
    }

    @Override
    public Iterable<Node> nodes() {
        return nodes;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public Iterator<PropertyContainer> iterator() {
        return new Iterator<PropertyContainer>()
        {
            Iterator<? extends PropertyContainer> current = nodes().iterator();
            Iterator<? extends PropertyContainer> next = relationships().iterator();

            @Override
            public boolean hasNext()
            {
                return current.hasNext();
            }

            @Override
            public PropertyContainer next()
            {
                try
                {
                    return current.next();
                }
                finally
                {
                    Iterator<? extends PropertyContainer> temp = current;
                    current = next;
                    next = temp;
                }
            }

            @Override
            public void remove()
            {
                next.remove();
            }
        };
    }
}
