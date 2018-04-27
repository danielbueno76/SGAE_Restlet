package sgae.servidor.albumes;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.Language;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.ext.velocity.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionAlbumes;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionPistas;
import sgae.nucleo.gruposMusicales.Pista;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.AlbumInfoBreve;
import sgae.util.generated.Albumes;
import sgae.util.generated.GrupoInfoBreve;
import sgae.util.generated.Grupos;
import sgae.util.generated.Link;
import sgae.util.generated.PersonaInfoBreve;
import sgae.util.generated.Personas;
import sgae.util.generated.PistaInfoBreve;
import sgae.util.generated.Pistas;

public class PistasServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	private String idAlbum;
	
	@Override
	protected void doInit() throws ResourceException{	
		getVariants().add(new Variant (MediaType.TEXT_HTML));
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		getVariants().add(new Variant(MediaType.APPLICATION_WWW_FORM));
		this.grupoID = getAttribute("CIFgrupo");
		this.idAlbum = getAttribute("albumID");
	}
		
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Representation result = null;
		StringBuilder result2 = new StringBuilder();
		if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {		
			try {
				for (Pista p: controladorGruposMusicales.recuperarPistas(grupoID,idAlbum)) {
					result2.append((p == null) ? "" : "Título: " + p.getNombre() + "\tUri: " + p.getIdPista()).append('\n');
				}
			} catch (ExcepcionGruposMusicales e) {				
				System.out.println("ExcepcionGruposMusicales  listarpistas --> No existe el grupo");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);				
			} catch (ExcepcionAlbumes e) {
				System.out.println("ExcepcionGruposMusicales  listarpistas --> No existe el album");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);	
			}
			result = new StringRepresentation(result2.toString());
		}
		else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) {
			Pistas pistasHTML = new Pistas();	//XML
			final List<PistaInfoBreve> pistasInfoBreve= pistasHTML.getPistaInfoBreve();
			Map<String, Object> pistaDataModel = new HashMap<String, Object>();
			

				try {
					for(sgae.nucleo.gruposMusicales.Pista a: controladorGruposMusicales.recuperarPistas(grupoID ,idAlbum) ) {
					
					PistaInfoBreve pistaInfo = new PistaInfoBreve();	
					pistaInfo.setNombre(a.getNombre());
					Link link = new Link();
					link.setHref(a.getIdPista());
					link.setTitle("Pistas");
					link.setType("simple");
					pistaInfo.setUri(link);
					pistasInfoBreve.add(pistaInfo);
						}
					pistaDataModel.put("pistas", pistasInfoBreve);
					Representation pistasVtl = new ClientResource(
							LocalReference.createClapReference(getClass().getPackage())+ "/Pistas.vtl").get();
							// Wrap bean with Velocity representation
							return new TemplateRepresentation(pistasVtl, pistaDataModel, MediaType.TEXT_HTML);

				} catch (IOException e) {
					System.out.println("IOException GET HTML PistasServerResource");
					//throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				} catch (ExcepcionGruposMusicales e) {
					System.out.println("ExcepcionGruposMusicales No existe el grupo");
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				} catch (ExcepcionAlbumes e) {
					System.out.println("ExcepcionGruposMusicales No existe el album");
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				}
				
				

		}
		
		return result;
	}
	

	public Representation post (Representation datos, Variant variant) {
		Representation result = null;
		if (MediaType.APPLICATION_WWW_FORM.isCompatible(variant.getMediaType())) {
			Form form = new Form(datos);		
			String CIF= this.grupoID;
			String idAlbum = this.idAlbum;
			String Nombre= form.getFirstValue("NOMBRE");
			String Duracion= form.getFirstValue("DURACION");
			int duracion = Integer.parseInt(Duracion);
//	
//			System.out.println("CIF: " + CIF );
//			System.out.println("Album: " + idAlbum );
//			System.out.println("Nombre: " + Nombre);
//			System.out.println("Duracion: " + duracion);
			 
			try {
				controladorGruposMusicales.anadirPista(CIF, idAlbum, Nombre, duracion);
				String URI = getLocationRef().getIdentifier();
				result =  new StringRepresentation("CIF: " + CIF +" Album: " + idAlbum+" Nombre: " + Nombre +"Duracion: "+Duracion,   MediaType.TEXT_PLAIN, Language.SPANISH, CharacterSet.ISO_8859_1);
			} catch (ExcepcionAlbumes ex){
				System.out.println("ExcepcionAlbumes Crearpista");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				
			}catch (ExcepcionGruposMusicales ax) {
				System.out.println("ExcepcionGruposMusicales Crearpista");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}catch (ExcepcionPistas ax) {
				System.out.println("ExcepcionPistas Crearpista");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}
			
			
		}
		return result;
	}	


}