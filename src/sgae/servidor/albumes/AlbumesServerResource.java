package sgae.servidor.albumes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import sgae.nucleo.gruposMusicales.Album;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.GrupoMusical;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.GrupoMusicalesInfoBreve;
import sgae.util.generated.GruposMusicales;
import sgae.util.generated.Link;

public class AlbumesServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	
	@Override
	protected void doInit() throws ResourceException {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		getVariants().add(new Variant(MediaType.TEXT_HTML));
		this.grupoID = getAttribute("CIFgrupo");
		
	}
	
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Representation result = null;
		StringBuilder result2 = new StringBuilder();
	if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {		
		try {
			for (String album: controladorGruposMusicales.listarAlbumes(grupoID)) {
				result2.append((album == null) ? "" : album).append('\n');
			}
		} catch (ExcepcionGruposMusicales e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = new StringRepresentation(result2.toString());
	}
	
	else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) {
//		Personas personasHTML = new Personas();	//XML
////		final List<PersonaInfoBreve> personaInfoBreve= personasHTML.getPersonaInfoBreve();
//		Map<String, Object> albumModel = new HashMap<String, Object>();
//
//		for( Album a : controladorGruposMusicales.recuperarAlbumes(grupoID) ) {
//			
//			PersonaInfoBreve personaInfo = new PersonaInfoBreve();	
//			personaInfo.setDni(p.getDni());
//			personaInfo.setApellidos(p.getApellidos());
//			personaInfo.setNombre(p.getNombre());
//			Link link = new Link();
//			link.setHref("/personas/"+p.getDni());
//			link.setTitle("Personas");
//			link.setType("simple");
//			personaInfo.setUri(link);
//			albumModel.put("Personas",personaInfo);
//			
//		}
////		
//		Representation personasVTL = new ClientResource(LocalReference.createClapReference(getClass().getPackage())	+ "/Personas.vtl").get();
//		result= new TemplateRepresentation(personasVTL, personaInfoBreve, MediaType.TEXT_HTML);
	}
		return result;
	}
	
	
	
	@Get("xml")
	public JaxbRepresentation<sgae.util.generated.GrupoMusical> toXml() throws ExcepcionGruposMusicales {
			GrupoMusical g = controladorGruposMusicales.recuperarGrupoMusical(this.grupoID);			
			sgae.util.generated.GrupoMusical grupoInfo = new sgae.util.generated.GrupoMusical();	
			grupoInfo.setCif(g.getCif());
			grupoInfo.setNombre(g.getNombre());
			grupoInfo.setFechaCreacion(g.getFechaCreacion());
		

		JaxbRepresentation <sgae.util.generated.GrupoMusical> result = new JaxbRepresentation<sgae.util.generated.GrupoMusical> (grupoInfo);
		result.setFormattedOutput(true);
		
		return result;

	}

}
