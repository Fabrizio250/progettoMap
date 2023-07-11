/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room.REST.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.File;


/**
 *
 * @author gioel
 */
@Path("documentation")
public class DocService {
    
    @GET
    @Path("/download")
    //@Produces("text/plain")
    @Produces("application/pdf")
    public Response download() {

        File file = new File("src\\main\\java\\hasbullateam\\escape_room\\REST\\Service\\documentazione.pdf");

        // Verifica se il file esiste
        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Costruisci la risposta con il file PDF
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=documentazione.pdf");

        return response.build();
    }
    

    
}
