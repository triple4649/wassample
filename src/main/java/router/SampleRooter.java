package router;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import jpn.JapaneseAnalyser;


@Path("sample")
public class SampleRooter {
    @Path("Area")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,List<String>> doLogic (
        @QueryParam("path") String filePath        
    )throws Exception{
        return JapaneseAnalyser.parseXML(filePath);
    }
}