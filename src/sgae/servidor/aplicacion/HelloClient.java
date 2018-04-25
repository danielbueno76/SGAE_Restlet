package sgae.servidor.aplicacion;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Put;

public class HelloClient {

	public static void main(String[] args) throws Exception{
		
		//Cretae local proxy to resource
		ClientResource helloClientResource = new ClientResource ("http://localhost:8111/123456789A");
		
		//El cliente deberá:
		
		//Crear una persona
		Form f = new Form();
		f.add("NOMBRE", "David");
		f.add("APELLIDOS", "Bisbal");
		f.add("FECHANACIMIENTO", "11-11-1980");
		Representation rep = helloClientResource.put(f);
		
		//Listar toda las personas
//		helloClientResource.get()

	}

}
