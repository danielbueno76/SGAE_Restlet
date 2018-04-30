package sgae.servidor.gruposMusicales;

import java.util.List;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.personas.Persona;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.Link;
import sgae.util.generated.PersonaInfoBreve;
import sgae.util.generated.Personas;
/**
 * Clase que muestra miembros de un grupo musical.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */
public class MiembrosServerResource extends ServerResource{
	/**Inicializamos la variable y controlador necesario para miembros*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	/**Variable local grupoID que contiene el Cif de un grupo.*/
	private String grupoID;
	
	/** 
	 * Metodo doInit que se ejecuta al acceder a esta clase
	 */
	protected void doInit() throws ResourceException{		
		this.grupoID = getAttribute("CIFgrupo");
	}
	
	/** 
	 * Metodo get en formato texto plano sobre miembros
	 * @return result datos solicitados en texto plano
	 */
	@Get("txt")
	public String represent() {
		StringBuilder result = new StringBuilder();
		try {
			//En este bucle nos devuelven los miembros actuales que existen previamente.
			for (Persona persona: controladorGruposMusicales.recuperarMiembros(grupoID)) {
				//Añadimos la información a mostrar de los miembros actuales
				result.append((persona.verDescripcionBreve() == null) ? "" : "Miembros actuales:\t"+persona.verDescripcionBreve()+"\tUri: /../../../personas/"+persona.getDni()).append('\n');
			}
		}  catch (ExcepcionGruposMusicales e) {
			System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());		//Se devuelve un error --> Recurso no encontrado.
		}
		try {
			//En este bucle nos devuelven los miembros anteriores que existen previamente.
			for (Persona persona: controladorGruposMusicales.recuperarMiembrosAnteriores(grupoID)) {
				//Añadimos la información a mostrar de los miembros actuales
				result.append((persona.verDescripcionBreve() == null) ? "" : "Miembros anteriores:\t"+persona.verDescripcionBreve()+"\tUri: /../../../personas/"+persona.getDni()).append('\n');
			}
		}  catch (ExcepcionGruposMusicales e) {
			System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,e.getCausaFallo());		//Se devuelve un error --> Recurso no encontrado.
		}
				
		return result.toString();//Devolvemos la información solicitada en String
	}
	
	
	/** 
	 * Metodo get en formato xml sobre miembros
	 * @return result datos solicitados en xml
	 */
	@Get("xml")
	public JaxbRepresentation<Personas> toXml() {
		
		Personas personasXML = new Personas();	//Objeto de tipo grupos
		final List<PersonaInfoBreve> personaInfoBreve= personasXML.getPersonaInfoBreve();//Creamos una lista que contiene objetos personasinfobreve
		
		try {
			for(sgae.nucleo.personas.Persona p: controladorGruposMusicales.recuperarMiembros(grupoID) ) {//En este bucle nos devuelven los miembros actuales, siendo objetos de tipo Persona.
				
				//Creamos un objeto PersonaInfoBreve y le añadimos la información devuelta en cada iteración del bucle.
				PersonaInfoBreve personaInfo = new PersonaInfoBreve();	
				personaInfo.setDNI(p.getDni());
				personaInfo.setApellidos(p.getApellidos());
				personaInfo.setNombre(p.getNombre());
				//Creamos un objeto de tipo Link y le añadimos los valores necesarios.
				Link link = new Link();
				link.setHref("/../../../personas/"+p.getDni());
				link.setTitle("Miembros actuales");
				link.setType("simple");
				personaInfo.setUri(link);
				//Ahora añadidmos a lista de PersonasInfoBreve el objeto PersonaInfoBreve con la información devuelta por el bucle.
				personaInfoBreve.add(personaInfo);
				
			}
			
			for(sgae.nucleo.personas.Persona p: controladorGruposMusicales.recuperarMiembrosAnteriores(grupoID) ) {//En este bucle nos devuelven los miembros anteriores, siendo objetos de tipo Persona.
				//Creamos un objeto PersonaInfoBreve y le añadimos la información devuelta en cada iteración del bucle.
				PersonaInfoBreve personaInfo = new PersonaInfoBreve();	
				personaInfo.setDNI(p.getDni());
				personaInfo.setApellidos(p.getApellidos());
				personaInfo.setNombre(p.getNombre());
				//Creamos un objeto de tipo Link y le añadimos los valores necesarios.
				Link link = new Link();
				link.setHref("/../../../personas/"+p.getDni());
				link.setTitle("Miembros anteriores");
				link.setType("simple");
				personaInfo.setUri(link);
				//Ahora añadidmos a lista de PersonasInfoBreve el objeto PersonaInfoBreve con la información devuelta por el bucle.
				personaInfoBreve.add(personaInfo);				
			}
		} catch (ExcepcionGruposMusicales e) {
			System.out.println("ExcepcionGruposMusicales a la hora de listar miembros");//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
		//Se crea el objeto de tipo JaxbRepresentation result con el formato especificado en la teoría
		JaxbRepresentation <Personas> result = new JaxbRepresentation<Personas> (personasXML);
		result.setFormattedOutput(true);
		
		return result;//Devuelve la variable de tipo JaxbRepresentation
	}

}