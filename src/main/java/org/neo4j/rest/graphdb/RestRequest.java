package org.neo4j.rest.graphdb;

import org.jboss.resteasy.client.ClientResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;
import org.jboss.resteasy.client.ClientRequestFactory;

public class RestRequest {
    private final URI baseUri;
    private final ClientRequestFactory client;

    public RestRequest( URI baseUri ) {
        this( baseUri, null, null );
    }

    public RestRequest( URI baseUri, String username, String password ) {
        this.baseUri = uriWithoutSlash( baseUri );
        client = new ClientRequestFactory(this.baseUri);
        //if ( username != null ) client.addFilter( new HTTPBasicAuthFilter( username, password ) );

    }

    private RestRequest( URI uri, ClientRequestFactory client ) {
        this.baseUri = uriWithoutSlash( uri );
        this.client = client;
    }

    private URI uriWithoutSlash( URI uri ) {
        String uriString = uri.toString();
        return uriString.endsWith( "/" ) ? uri( uriString.substring( 0, uriString.length() - 1 ) ) : uri;
    }

    public static String encode( Object value ) {
        if ( value == null ) return "";
        try {
            return URLEncoder.encode( value.toString(), "utf-8" ).replaceAll( "\\+", "%20" );
        } catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException( e );
        }
    }


    /*private Builder builder( String path ) {
        WebResource resource = client.resource( uri( pathOrAbsolute( path ) ) );
        return resource.accept( MediaType.APPLICATION_JSON_TYPE );
    }*/

    private String pathOrAbsolute( String path ) {
        if ( path.startsWith( "http://" ) ) return path;
        return baseUri + "/" + path;
    }

    public ClientResponse get( String path ) {
        ClientResponse response = null;
        try {
            response = client.createRequest(pathOrAbsolute(path)).get();
        } catch (Exception e)
        {
            
        }
        
        return response;
        //return builder( path ).get( ClientResponse.class );
    }

    public ClientResponse delete( String path ) {
        ClientResponse response = null;
        try {
            response = client.createRequest(pathOrAbsolute(path)).delete();
        } catch (Exception e)
        {
            
        }
        
        return response;
        //return builder( path ).delete( ClientResponse.class );
    }

    public ClientResponse post( String path, String data ) {
        ClientResponse response = null;
        try {
            if(data!=null)
                response = client.createRequest(pathOrAbsolute(path)).body(MediaType.APPLICATION_JSON_TYPE, data).post();
            else response = client.createRequest(pathOrAbsolute(path)).post();
        } catch (Exception e)
        {
            
        }
        
        return response;
/*        Builder builder = builder( path );
        if ( data != null ) {
            builder = builder.entity( data, MediaType.APPLICATION_JSON_TYPE );
        }
        return builder.post( ClientResponse.class );*/
    }

    public ClientResponse put( String path, String data ) {
        ClientResponse response = null;
        try {
            if(data!=null)
                response = client.createRequest(pathOrAbsolute(path)).body(MediaType.APPLICATION_JSON_TYPE, data).put();
            else response = client.createRequest(pathOrAbsolute(path)).put();
        } catch (Exception e)
        {
            
        }
        
        return response;
        /*Builder builder = builder( path );
        if ( data != null ) {
            builder = builder.entity( data, MediaType.APPLICATION_JSON_TYPE );
        }
        return builder.put( ClientResponse.class );*/
    }


    public Object toEntity( ClientResponse response ) {
        return JsonHelper.jsonToSingleValue( entityString( response ) );
    }

    public Map<?, ?> toMap( ClientResponse response ) {
        return JsonHelper.jsonToMap( entityString( response ) );
    }

    private String entityString( ClientResponse response ) {
        Object entity = response.getEntity(String.class);
        return entity.toString();
        //return response.getEntity( String.class );
    }

    public boolean statusIs( ClientResponse response, Response.StatusType status ) {
        return response.getStatus() == status.getStatusCode();
    }

    public boolean statusOtherThan( ClientResponse response, Response.StatusType status ) {
        return !statusIs( response, status );
    }

    public RestRequest with( String uri ) {
        return new RestRequest( uri( uri ));
    }

    private URI uri( String uri ) {
        try {
            return new URI( uri );
        } catch ( URISyntaxException e ) {
            throw new RuntimeException( e );
        }
    }

    public URI getUri() {
        return baseUri;
    }
}
