package sgae.servidor.gruposMusicales;

import java.util.List;

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
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.nucleo.personas.Persona;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.Link;
import sgae.util.generated.PersonaInfoBreve;
import sgae.util.generated.Personas;

public class MiembrosServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	
	protected void doInit() throws ResourceException{		
		this.grupoID = getAttribute("CIFgrupo");
	}
	
	@Get("txt")
	public String represent() {
		StringBuilder result = new StringBuilder();
		try {
			for (Persona persona: controladorGruposMusicales.recuperarMiembros(grupoID)) {
				result.append((persona.verDescripcionBreve() == null) ? "" : "Miembros actuales:\t"+persona.verDescripcionBreve()+"\tUri: /../../../personas/"+persona.getDni()).append('\n');
			}
		} catch (ExcepcionGruposMusicales e) {
			System.out.println("ExcepcionGruposMusicales listar Miembros actuales");
			 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		try {
			for (Persona persona: controladorGruposMusicales.recuperarMiembrosAnteriores(grupoID)) {
				result.append((persona.verDescripcionBreve() == null) ? "" : "Miembros anteriores:\t"+persona.verDescripcionBreve()+"\tUri: /../../../personas/"+persona.getDni()).append('\n');
			}
		} catch (ExcepcionGruposMusicales e) {
			 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
				
		return result.toString();
	}
	
	
	
	@Get("xml")
	public JaxbRepresentation<Personas> toXml() {
		
		Personas personasXML = new Personas();	//XML
		final List<PersonaInfoBreve> personaInfoBreve= personasXML.getPersonaInfoBreve();
		
		try {
			for(sgae.nucleo.personas.Persona p: controladorGruposMusicales.recuperarMiembros(grupoID) ) {
				//NOMBRE=Dani&APELLIDOS=Bueno Pacheco&FECHANACIMIENTO=14-04-1995
				PersonaInfoBreve personaInfo = new PersonaInfoBreve();	
				personaInfo.setDNI(p.getDni());
				personaInfo.setApellidos(p.getApellidos());
				personaInfo.setNombre(p.getNombre());
				Link link = new Link();
				link.setHref("/../../../personas/"+p.getDni());
				link.setTitle("Miembros actuales");
				link.setType("simple");
				personaInfo.setUri(link);
				personaInfoBreve.add(personaInfo);
				
			}
			
			for(sgae.nucleo.personas.Persona p: controladorGruposMusicales.recuperarMiembrosAnteriores(grupoID) ) {
				//NOMBRE=Dani&APELLIDOS=Bueno Pacheco&FECHANACIMIENTO=14-04-1995
				PersonaInfoBreve personaInfo = new PersonaInfoBreve();	
				personaInfo.setDNI(p.getDni());
				personaInfo.setApellidos(p.getApellidos());
				personaInfo.setNombre(p.getNombre());
				Link link = new Link();
				link.setHref("/../../../personas/"+p.getDni());
				link.setTitle("Miembros anteriores");
				link.setType("simple");
				personaInfo.setUri(link);
				personaInfoBreve.add(personaInfo);
				
			}
		} catch (ExcepcionGruposMusicales e) {
			System.out.println("ExcepcionGruposMusicales a la hora de listar miembros");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		
		JaxbRepresentation <Personas> result = new JaxbRepresentation<Personas> (personasXML);
		result.setFormattedOutput(true);
		
		return result;
	}

}