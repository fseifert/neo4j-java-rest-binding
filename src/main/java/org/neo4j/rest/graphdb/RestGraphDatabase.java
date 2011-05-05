package org.neo4j.rest.graphdb;

import org.jboss.resteasy.client.ClientResponse;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.event.KernelEventHandler;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.kernel.Config;
import org.neo4j.kernel.RestConfig;
import org.neo4j.rest.graphdb.index.RestIndexManager;

import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.Map;

public class RestGraphDatabase extends AbstractGraphDatabase {
    private RestRequest restRequest;
    private long propertyRefetchTimeInMillis = 1000;


    public RestGraphDatabase( URI uri ) {
        restRequest = new RestRequest( uri );
    }

    public RestGraphDatabase( URI uri, String user, String password ) {
        restRequest = new RestRequest( uri, user, password );
    }

    @Override
    public Transaction beginTx() {
        return new Transaction() {
            @Override
            public void success() {
            }

            @Override
            public void finish() {

            }

            @Override
            public void failure() {
            }
        };
    }

    @Override
    public <T> TransactionEventHandler<T> registerTransactionEventHandler( TransactionEventHandler<T> tTransactionEventHandler ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TransactionEventHandler<T> unregisterTransactionEventHandler( TransactionEventHandler<T> tTransactionEventHandler ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KernelEventHandler registerKernelEventHandler( KernelEventHandler kernelEventHandler ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KernelEventHandler unregisterKernelEventHandler( KernelEventHandler kernelEventHandler ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IndexManager index() {
        return new RestIndexManager( restRequest, this );
    }

    @Override
    public Node createNode() {
        ClientResponse response = restRequest.post( "node", null );
        if ( restRequest.statusOtherThan( response, Status.CREATED ) ) {
            throw new RuntimeException( "" + response.getStatus() );
        }
        return new RestNode( response.getLocation().getHref(), this );
    }

    @Override
    public Iterable<Node> getAllNodes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node getNodeById( long id ) {
        ClientResponse response = restRequest.get( "node/" + id );
        if ( restRequest.statusIs( response, Status.NOT_FOUND ) ) {
            throw new NotFoundException( "" + id );
        }
        return new RestNode( restRequest.toMap( response ), this );
    }

    @Override
    public Node getReferenceNode() {
        Map<?, ?> map = restRequest.toMap( restRequest.get( "" ) );
        return new RestNode( (String) map.get( "reference_node" ), this );
    }

    @Override
    public Relationship getRelationshipById( long id ) {
        ClientResponse response = restRequest.get( "relationship/" + id );
        if ( restRequest.statusIs( response, Status.NOT_FOUND ) ) {
            throw new NotFoundException( "" + id );
        }
        return new RestRelationship( restRequest.toMap( response ), this );
    }

    @Override
    public Iterable<RelationshipType> getRelationshipTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
    }

    public RestRequest getRestRequest() {
        return restRequest;
    }

    public long getPropertyRefetchTimeInMillis() {
        return propertyRefetchTimeInMillis;
	}
    @Override
    public String getStoreDir() {
        return restRequest.getUri().toString();
    }

    @Override
    public Config getConfig() {
        return new RestConfig(this);
    }

    @Override
    public <T> T getManagementBean(Class<T> type) {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
