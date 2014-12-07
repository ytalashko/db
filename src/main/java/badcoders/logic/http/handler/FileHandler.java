package badcoders.logic.http.handler;

import badcoders.logic.util.Constants;
import co.cask.http.HttpResponder;
import org.jboss.netty.buffer.ByteBufferBackedChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handler for performing file uploading operations.
 */
public class FileHandler extends AbstractAuthHandler {

    /**
     * Gets index.html.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @GET
    @Path(Constants.API_BASE)
    public void getIndexHtml(HttpRequest request, HttpResponder responder) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(Constants.INDEX_HTML_PATH));

            ByteBuffer b = ByteBuffer.allocate(encoded.length);
            b.put(encoded);
            b.position(0);

            ChannelBuffer buffer = new ByteBufferBackedChannelBuffer(b);

            responder.sendContent(HttpResponseStatus.OK, buffer, "text/html", null);
        } catch (IOException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred");
        }
    }

    /**
     * Gets a js file.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @GET
    @Path("/js/{file-name}")
    public void getJsFile(HttpRequest request, HttpResponder responder, @PathParam("file-name") String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(Constants.JS_PARENT_PATH + fileName));

            ByteBuffer b = ByteBuffer.allocate(encoded.length);
            b.put(encoded);
            b.position(0);

            ChannelBuffer buffer = new ByteBufferBackedChannelBuffer(b);

            responder.sendContent(HttpResponseStatus.OK, buffer, "text/javascript", null);
        } catch (IOException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred");
        }
    }

    /**
     * Gets a css file.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @GET
    @Path("/css/{file-name}")
    public void getCssFile(HttpRequest request, HttpResponder responder, @PathParam("file-name") String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(Constants.CSS_PARENT_PATH + fileName));

            ByteBuffer b = ByteBuffer.allocate(encoded.length);
            b.put(encoded);
            b.position(0);

            ChannelBuffer buffer = new ByteBufferBackedChannelBuffer(b);

            responder.sendContent(HttpResponseStatus.OK, buffer, "text/css", null);
        } catch (IOException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred");
        }
    }
}
