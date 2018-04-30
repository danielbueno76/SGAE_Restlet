package sgae.servidor.personas;

import java.util.List;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import sgae.nucleo.personas.ControladorPersonas;
import sgae.nucleo.personas.Persona;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.Link;
import sgae.util.generated.PersonaInfoBreve;
import sgae.util.generated.Personas;
/**
 * Clase que muestra las personas almacenadas.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */
public class PersonasServerResource extends ServerResource{
	/**Inicializamos la variable y controlador necesario para miembros*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorPersonas controladorPersonas = ref.getControladorPersonas();
	
	/** 
	 * Metodo get en formato texto plano sobre personas
	 * @return result datos solicitados en texto plano
	 */
	@Get("txt")
	public String represent(){
		StringBuilder result = new StringBuilder();
		//En este bucle nos devuelven las personas que existen previamente.
		for (Persona persona: controladorPersonas.recuperarPersonas()) {
			//Añadimos la información a mostrar de las personas
			result.append((persona == null) ? "" : "DNI: " + persona.getDni() + "\tNombre: " + persona.getNombre() + "\tApellidos: " + persona.getApellidos() +"\tURI: " + persona.getDni()+ "\n").append('\n');
		}
		
		return result.toString();//Devolvemos los datos solicitados en un String.
	}
	
	
	/** 
	 * Metodo get en formato xml sobre personas
	 * @return result datos solicitados en xml
	 */
	@Get("xml")
	public JaxbRepresentation<Personas> toXml() {
		
		Personas personasXML = new Personas();	//Objeto de tipo personas
		final List<PersonaInfoBreve> personaInfoBreve= personasXML.getPersonaInfoBreve();//Creamos una lista que contiene una lista de personainfobreve
		
		for(sgae.nucleo.personas.Persona p: controladorPersonas.recuperarPersonas() ) {//Recorremos todos las posibles personas que se encuentran en el sistema
			
			//Creamos un objeto PersonaInfoBreve y le añadimos la información devuelta en cada iteración del bucle.
			PersonaInfoBreve personaInfo = new PersonaInfoBreve();	
			personaInfo.setDNI(p.getDni());
			personaInfo.setApellidos(p.getApellidos());
			personaInfo.setNombre(p.getNombre());
			//Creamos un objeto de tipo Link y le añadimos los valores necesarios.
			Link link = new Link();
			link.setHref(p.getDni());
			link.setTitle("Personas");
			link.setType("simple");
			personaInfo.setUri(link);
			//Ahora añadidmos a lista de PersonaInfoBreve el objeto PersonaInfoBreve con la información devuelta en el bucle.
			personaInfoBreve.add(personaInfo);
			
		}
		//Se crea el objeto de tipo JaxbRepresentation result con el formato especificado en la teoría
		JaxbRepresentation <Personas> result = new JaxbRepresentation<Personas> (personasXML);
		result.setFormattedOutput(true);
		
		return result;//Devuelve la variable de tipo JaxbRepresentation
	}

}
