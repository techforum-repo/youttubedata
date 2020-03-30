package jaxrsservice.core.restservices;

import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

import org.osgi.service.component.annotations.Component; 

@Component(service=RESTProductAPI.class)
@Path("/products")
public class RESTProductAPI {

@GET
@Path("/{catagroy}/{title}/p/{code : \\d{5}}")
@Produces({MediaType.TEXT_PLAIN})
public String getProductDetails(@Context HttpServletRequest request, @Context HttpServletResponse response,@PathParam("catagroy") String catagroy,@PathParam("title") String title,@PathParam("code") String code) {

    return "code="+code+";catagroy="+catagroy+";title="+title; 

    }
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Product createProduct(Product product) {		
		product.setResult("Product Created");
		product.setId("1");
		return product;
		
	}
}

 
