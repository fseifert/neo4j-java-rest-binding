package org.neo4j.rest.graphdb.index;

import org.neo4j.graphdb.Node;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.RestNode;
import org.neo4j.rest.graphdb.RestRequest;

import java.util.Map;

/**
 * @author mh
 * @since 24.01.11
 */
public class RestNodeIndex extends RestIndex<Node> {
    public RestNodeIndex( RestRequest restRequest, String indexName, RestGraphDatabase restGraphDatabase ) {
        super( restRequest, indexName, restGraphDatabase );
    }

    @Override
    public Class<Node> getEntityType() {
        return Node.class;
    }

    @Override
    public void remove(Node entity, String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Node entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Node createEntity(Map<?, ?> item) {
        return new RestNode((Map<?, ?>) item, restGraphDatabase);
    }
}
