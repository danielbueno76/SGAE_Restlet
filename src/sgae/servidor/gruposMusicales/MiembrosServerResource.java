package sgae.servidor.gruposMusicales;

import java.util.List;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.personas.ControladorPersonas;
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
	public String represent() throws ExcepcionGruposMusicales{
		StringBuilder result = new StringBuilder();
		for (Persona persona: controladorGruposMusicales.recuperarMiembros(grupoID)) {
			System.out.println("Entra");
			result.append((persona.verDescripcionBreve() == null) ? "" : persona.verDescripcionBreve()).append('\n');
		}
		
		return result.toString();
	}
	
	
	
//	@Get("xml")
//	public JaxbRepresentation<Personas> toXml() {
//		
//		Personas personasXML = new Personas();	//XML
//		final List<PersonaInfoBreve> personaInfoBreve= personasXML.getPersonaInfoBreve();
//		
//		for(sgae.nucleo.personas.Persona p: controladorGruposMusicales.recuperarPersonas() ) {
//			//DNI=00000000B&NOMBRE=Dani&APELLIDOS=Bueno Pacheco&FECHANACIMIENTO=14-04-1995
//			PersonaInfoBreve personaInfo = new PersonaInfoBreve();	
//			personaInfo.setDni(p.getDni());
//			personaInfo.setApellidos(p.getApellidos());
//			personaInfo.setNombre(p.getNombre());
//			Link link = new Link();
//			link.setHref("/personas/"+p.getDni());
//			link.setTitle("Personas");
//			link.setType("simple");
//			personaInfo.setUri(link);
//			personaInfoBreve.add(personaInfo);
//			
//		}
//		
//		JaxbRepresentation <Personas> result = new JaxbRepresentation<Personas> (personasXML);
//		result.setFormattedOutput(true);
//		
//		return result;
//	}

}
