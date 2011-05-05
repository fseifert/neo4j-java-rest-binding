package org.neo4j.rest.graphdb;

import org.neo4j.graphdb.*;

import java.net.URI;
import java.util.Map;

public class RestRelationship extends RestEntity implements Relationship {

    RestRelationship( URI uri, RestGraphDatabase graphDatabaseService ) {
        super( uri, graphDatabaseService );
    }

    RestRelationship( String uri, RestGraphDatabase graphDatabase ) {
        super( uri, graphDatabase );
    }

    public RestRelationship( Map<?, ?> data, RestGraphDatabase graphDatabase ) {
        super( data, graphDatabase );
    }

    @Override
    public Node getEndNode() {
        return node( (String) getStructuralData().get( "end" ) );
    }

    @Override
    public Node[] getNodes() {
        return new Node[]{
                node( (String) getStructuralData().get( "start" ) ),
                node( (String) getStructuralData().get( "end" ) )
        };
    }

    @Override
    public Node getOtherNode( Node node ) {
        long nodeId = node.getId();
        String startNodeUri = (String) getStructuralData().get( "start" );
        String endNodeUri = (String) getStructuralData().get( "end" );
        if ( getEntityId( startNodeUri ) == nodeId ) {
            return node( endNodeUri );
        } else if ( getEntityId( endNodeUri ) == nodeId ) {
            return node( startNodeUri );
        } else {
            throw new NotFoundException( node + " isn't one of start/end for " + this );
        }
    }

    private RestNode node( String uri ) {
        return new RestNode( uri, getGraphDatabase() );
    }

    @Override
    public Node getStartNode() {
        return node( (String) getStructuralData().get( "start" ) );
    }

    @Override
    public RelationshipType getType() {
        return DynamicRelationshipType.withName( (String) getStructuralData().get( "type" ) );
    }

    @Override
    public boolean isType( RelationshipType type ) {
        return type.name().equals( getStructuralData().get( "type" ) );
    }
}
