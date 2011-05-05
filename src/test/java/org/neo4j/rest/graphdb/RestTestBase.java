package org.neo4j.rest.graphdb;


import org.jboss.resteasy.client.ClientResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import org.jboss.resteasy.client.ClientRequestFactory;

public class RestTestBase {

    protected GraphDatabaseService graphDb;
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 7474;
    private static final String SERVER_ROOT_URI = "http://" + HOSTNAME + ":" + PORT + "/db/data/";
    private static final String SERVER_CLEANDB_URI = "http://" + HOSTNAME + ":" + PORT + "/cleandb/secret-key";
    private static final String CONFIG = RestTestBase.class.getResource("/neo4j-server.properties").getFile();

    @BeforeClass
    public static void startDb() throws Exception {
        //BasicConfigurator.configure();
        //neoServer.start();
    }

    @Before
    public void setUp() throws URISyntaxException {
        cleanDb();
        graphDb = new RestGraphDatabase(new URI(SERVER_ROOT_URI));
    }

    private void cleanDb() {
        ClientResponse response = null;
        try {
            response = new ClientRequestFactory().createRequest(SERVER_CLEANDB_URI).delete();
        } catch (Exception e)
        {
            
        }

        if (response == null || response.getStatus() != 200) throw new RuntimeException("unable to clean database " + response);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @AfterClass
    public static void shutdownDb() {
        //neoServer.stop();

    }

    protected Relationship relationship() {
        Iterator<Relationship> it = node().getRelationships(Direction.OUTGOING).iterator();
        if (it.hasNext()) return it.next();
        return node().createRelationshipTo(graphDb.createNode(), Type.TEST);
    }

    protected Node node() {
        return graphDb.getReferenceNode();
    }
}
