package sgae.servidor.personas;

import java.util.List;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import sgae.nucleo.personas.ControladorPersonas;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.Link;
import sgae.util.generated.PersonaInfoBreve;
import sgae.util.generated.Personas;

public class PersonasServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorPersonas controladorPersonas = ref.getControladorPersonas();
	
	@Get("txt")
	public String represent(){
		StringBuilder result = new StringBuilder();
		for (String persona: controladorPersonas.listarPersonas()) {
			result.append((persona == null) ? "" : persona).append('\n');
		}
		
		return result.toString();
	}
	
	
	
	@Get("xml")
	public JaxbRepresentation<Personas> toXml() {
		
		Personas personasXML = new Personas();	//XML
		final List<PersonaInfoBreve> personaInfoBreve= personasXML.getPersonaInfoBreve();
		
		for(sgae.nucleo.personas.Persona p: controladorPersonas.recuperarPersonas() ) {
			//DNI=00000000B&NOMBRE=Dani&APELLIDOS=Bueno Pacheco&FECHANACIMIENTO=14-04-1995
			PersonaInfoBreve personaInfo = new PersonaInfoBreve();	
			personaInfo.setDNI(p.getDni());
			personaInfo.setApellidos(p.getApellidos());
			personaInfo.setNombre(p.getNombre());
			Link link = new Link();
			link.setHref("/personas/"+p.getDni());
			link.setTitle("Personas");
			link.setType("simple");
			personaInfo.setUri(link);
			personaInfoBreve.add(personaInfo);
			
		}
		
		JaxbRepresentation <Personas> result = new JaxbRepresentation<Personas> (personasXML);
		result.setFormattedOutput(true);
		
		return result;
	}

}
