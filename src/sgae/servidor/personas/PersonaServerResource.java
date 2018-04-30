package sgae.servidor.personas;

import java.text.ParseException;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import sgae.nucleo.personas.ControladorPersonas;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.nucleo.personas.Persona;
import sgae.servidor.aplicacion.SGAEaplicacion;
/**
 * Clase que muestra, modifica y elimina una persona espec�fica.
 * @author Daniel Bueno Pacheco y Roberto Herreras Bab�n. ETSIT UVa.
 * @version 1.0
 */

public class PersonaServerResource extends ServerResource{
	/**Inicializamos las variables y controladores necesarios para Persona*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorPersonas controladorPersonas = ref.getControladorPersonas();
	/**Variable local personaID que contiene el id de una persona.*/
	private String personaID;
	
	/** 
	 * Metodo doInit que se ejecuta al acceder a esta clase
	 */
	protected void doInit() throws ResourceException{		
		this.personaID = getAttribute("DNI");
	}
	

	/** 
	 * Metodo get en formato texto plano sobre una  persona
	 * @throws ExcepcionPersonas No existe la persona solicitada
	 * @return result datos solicitados en texto plano
	 */
	@Get("txt")
	public String represent(){
		String DescripcionCompleta=null;
		try {
			//Almacenamos en la variable de tipo String la informaci�n completa de la persona. Luego este contenido se muestra.
			DescripcionCompleta= controladorPersonas.verPersona(this.personaID);
		}
		catch(ExcepcionPersonas ex) {
			System.out.println("ExcepcionPersonas No existe la persona");//No existe la persona solicitada
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
		return DescripcionCompleta;//Devolvemos la informaci�n solicitada
	}
	
	
	/** 
	 * Metodo get en formato xml sobre persona
	 * @throws ExcepcionPersonas No existe la persona solicitada
	 * @return result datos solicitados en xml
	 */
	@Get("xml")
	public JaxbRepresentation<sgae.util.generated.Persona> toXml() {
		JaxbRepresentation <sgae.util.generated.Persona> result = null;
		try {
			//Creamos un objeto Persona y le a�adimos la informaci�n devuelta .
			Persona p = controladorPersonas.recuperarPersona(this.personaID);			
			sgae.util.generated.Persona personaInfo = new sgae.util.generated.Persona();	
			personaInfo.setDNI(p.getDni());
			personaInfo.setNombre(p.getNombre());
			personaInfo.setApellidos(p.getApellidos());
			personaInfo.setFechanacimiento(p.getFechaNacimiento());
			//Se crea el objeto JaxbRepresentation result con el formato especificado en la teor�a
			result = new JaxbRepresentation<sgae.util.generated.Persona> (personaInfo);
			result.setFormattedOutput(true);
		}
		catch(ExcepcionPersonas ex) {
			System.out.println("ExcepcionPersonas No existe la persona");//No existe la persona solicitada
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
		
		return result;//Devuelve la variable de tipo JaxbRepresentation
		

	}
	
	/** 
	 * Metodo delete para borrar la persona que especifiquemos
	 * @throws ExcepcionPersonas No existe la persona solicitada
	 */
	@Delete
	public void remove(){
		try {
			//borramos la  persona
			controladorPersonas.borrarPersona(this.personaID);
			throw new ResourceException(Status.SUCCESS_OK, "Se ha borrado con �xito");//Se ha borrado con exito.
		}catch(ExcepcionPersonas ex) {
			System.out.println("ExcepcionPersonas No existe la persona");//No existe la persona solicitada
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
		
		
	}
	
	/** 
	 * Metodo put para crear una personas.
	 * @param datos Variable en la cual recibimos los datos enviados por el cliente para modificar el album.
	 * @throws ParseException Excepcion producida cuando el cliente env�a los datos para crear o  modificar de manera err�nea.
	 * @throws ExcepcionPersonas No existe la persona solicitada
	 * @return result datos solicitados en el formato solicitado
	 */
	@Put("form-data")
	public Representation annadircuenta (Representation datos){
		//Capturamos los datos de la cabecera
		Form form = new Form(datos);
		//Ejemplo: DNI=00000000B&NOMBRE=Dani&APELLIDOS=Bueno Pacheco&FECHANACIMIENTO=14-04-1995
		//Almacenamos los datos que nos pasan.
		String DNI= this.personaID;
		String Nombre= form.getFirstValue("NOMBRE");
		String Apellidos= form.getFirstValue("APELLIDOS");
		String fechanacimiento= form.getFirstValue("FECHANACIMIENTO");
//		//Mostramos los datos almacendos para ver si est�n bien.
//		System.out.println("DNI: " + DNI );
//		System.out.println("Nombre: " + Nombre);
//		System.out.println("Apellidos: " + Apellidos);
//		System.out.println("Fecha de nacimiento: " + fechanacimiento);
		Representation result = null;
		 
		try {
			//Creamos la persona con el DNI especificado
			controladorPersonas.crearPersona(DNI, Nombre, Apellidos, fechanacimiento);
			//Almacenamos la informaci�n en la variable result para mostrarla despu�s.
			result =  new StringRepresentation("DNI: " + DNI +" Nombre: " + Nombre+" Apellidos: " + Apellidos+" Fecha de nacimiento: " + fechanacimiento,   MediaType.APPLICATION_WWW_FORM);
		}catch(ExcepcionPersonas ex) {
			System.out.println("ExcepcionPersonas No existe la persona");//No existe la persona solicitada
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}catch (ParseException ax) {
			System.out.println("ParseException crearPersona");//El cliente ha enviado los datos de manera err�nea.
			 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Te falta un par�metro o has enviado un par�metro con una sintaxis err�nea");//Se devuelve un error --> El cliente ha enviado mal la informaci�n
		}
		
		getResponse().setStatus(Status.SUCCESS_CREATED, "Se ha creado con exito");
		return result;//Devuelve la variable de tipo StringRepresentation

	}


}