package sgae.servidor.albumes;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import sgae.nucleo.gruposMusicales.Album;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionAlbumes;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.GrupoMusical;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.AlbumInfoBreve;
import sgae.util.generated.Albumes;
import sgae.util.generated.Link;

public class AlbumServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	private String albumID;
	
	@Override
	protected void doInit() throws ResourceException {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		getVariants().add(new Variant(MediaType.TEXT_HTML));
		this.grupoID = getAttribute("CIFgrupo");
		this.albumID = getAttribute("albumID");
		
		
	}
	
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Representation result = null;

	if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {
		
		try {
			result = new StringRepresentation(controladorGruposMusicales.verAlbum(grupoID, albumID));
		} catch (ExcepcionAlbumes e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExcepcionGruposMusicales e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) {
		System.out.println(LocalReference.createClapReference(getClass().getPackage())+ "/AlbumInfoBreve.vtl");
		Albumes albumesHTML = new Albumes();
		Map<String, Object> dataModelAlbum = new HashMap<String, Object>();
		dataModelAlbum.put("album", albumesHTML);
		// Load Velocity template
		
		Representation albumesVtl = new ClientResource(
		LocalReference.createClapReference(getClass().getPackage())+ "/AlbumInfoBreve.vtl").get();
		// Wrap bean with Velocity representation
		return new TemplateRepresentation(albumesVtl, dataModelAlbum, MediaType.TEXT_HTML);
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
	
	
	
}
