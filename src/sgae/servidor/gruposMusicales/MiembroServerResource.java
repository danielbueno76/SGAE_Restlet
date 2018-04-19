package sgae.servidor.gruposMusicales;

import java.text.ParseException;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.html.FormData;
import org.restlet.ext.html.FormDataSet;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import sgae.nucleo.personas.ControladorPersonas;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.nucleo.personas.Persona;
import sgae.servidor.aplicacion.SGAEaplicacion;


public class MiembroServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorPersonas controladorPersonas = ref.getControladorPersonas();
	private String personaID;
	
	protected void doInit() throws ResourceException{		
		this.personaID = getAttribute("DNI");
	}
	


	@Get("txt")
	public String represent() throws ExcepcionPersonas,ResourceException{
		String DescripcionCompleta=null;
		try {
			DescripcionCompleta= controladorPersonas.verPersona(this.personaID);
		}
		catch(ExcepcionPersonas ex) {
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}
		return DescripcionCompleta;
	}
	
	
	
	@Get("xml")
	public JaxbRepresentation<sgae.util.generated.Persona> toXml() throws ExcepcionPersonas, ParseException, ResourceException {
		JaxbRepresentation <sgae.util.generated.Persona> result = null;
		try {
			Persona p = controladorPersonas.recuperarPersona(this.personaID);			
			sgae.util.generated.Persona personaInfo = new sgae.util.generated.Persona();	
			personaInfo.setDNI(p.getDni());
			personaInfo.setNombre(p.getNombre());
			personaInfo.setApellidos(p.getApellidos());
			personaInfo.setFechanacimiento(p.getFechaNacimiento());
			result = new JaxbRepresentation<sgae.util.generated.Persona> (personaInfo);
			result.setFormattedOutput(true);
		}
	catch(ExcepcionPersonas ex) {
        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
	}
		return result;

	}
	
	@Delete
	public void remove() throws ExcepcionPersonas{
		controladorPersonas.borrarPersona(this.personaID);
		
	}
	
	@Put("form-data")
	public Representation annadircuenta (Representation datos) throws ParseException, ExcepcionPersonas, ResourceException{

		Form form = new Form(datos);		
		String DNI= this.personaID;
		String Nombre= form.getFirstValue("NOMBRE");
		String Apellidos= form.getFirstValue("APELLIDOS");
		String fechanacimiento= form.getFirstValue("FECHANACIMIENTO");
//			if (fechanacimiento.equals(null)) {
//				System.out.println("ParseException crear");
//				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
//			}
		 System.out.println("DNI: " + DNI );
		 System.out.println("Nombre: " + Nombre);
		 System.out.println("Apellidos: " + Apellidos);
		 System.out.println("Fecha de nacimiento: " + fechanacimiento);
		 Representation result = null;
		 
		try {
			controladorPersonas.crearPersona(DNI, Nombre, Apellidos, fechanacimiento); //Hay que poer las variables
			// Si se produce la expcion significa que la persona ya existe --> el usuario quiere hacer un put de modificacion
			 result =  new StringRepresentation("DNI: " + DNI +" Nombre: " + Nombre+" Apellidos: " + Apellidos+" Fecha de nacimiento:: " + fechanacimiento,   MediaType.TEXT_HTML);
		} catch (ExcepcionPersonas ex){
			try {
			controladorPersonas.modificarPersona(DNI, Nombre, Apellidos, fechanacimiento);
			 result =  new StringRepresentation("Nombre: " + Nombre+" Apellidos: " + Apellidos+" Fecha de nacimiento:: " + fechanacimiento,   MediaType.TEXT_HTML);
			}
			catch(ExcepcionPersonas ex2) {
				ex2.printStackTrace();
			}
			catch (ParseException ax2) {
				ax2.printStackTrace();
			}
		}catch (ParseException ax) {
			System.out.println("ParseException crear");
			 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
//		catch(ExcepcionPersonas ex) {
//	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
//		}
		return result;

	}


}
