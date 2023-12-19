
package acmecollege.rest;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import acmecollege.rest.resource.HttpErrorResponse;

/**
 * Create a Jackson exception instead of the default Payara HTML response.<br>
 * This exception is mapped using "@{@link Provider}".<br>
 * This is not needed, optional design.
 * 
 * 
 * @see <a href="https://javaee.github.io/javaee-spec/javadocs/javax./ws/rs/ClientErrorException.html">JavaEE 8 ClientErrorException</a>
 * @see <a href="https://javaee.github.io/javaee-spec/javadocs/javax./ws/rs/ext/ExceptionMapper.html">JavaEE 8 ExceptionMapper</a>
 */
@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {
    
    @Override
    public Response toResponse(ClientErrorException exception) {
      Response response = exception.getResponse();
      Response.StatusType statusType = response.getStatusInfo();
      int statusCode = statusType.getStatusCode();
      String reasonPhrase = statusType.getReasonPhrase();
      HttpErrorResponse entity = new HttpErrorResponse(statusCode, reasonPhrase);
      return Response.status(statusCode).entity(entity).build();
    }
}