package router;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


@Path("sample")
public class SampleRooter {
    @Path("Area")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String doLogic (
        @QueryParam("x") String ax, 
        @QueryParam("y") String ay          
    )throws Exception{
        return "test";
    }
}