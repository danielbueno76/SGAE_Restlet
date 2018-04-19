package sgae.servidor.albumes;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.ext.velocity.TemplateRepresentation;
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
import sgae.util.generated.GrupoInfoBreve;
import sgae.util.generated.Grupos;
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

		Map<String, Object> albumDataModel = new HashMap<String, Object>();
		
				try {
					Album a = controladorGruposMusicales.recuperarAlbum(grupoID, albumID);				
					sgae.util.generated.Album albumInfo = new sgae.util.generated.Album();	
					albumInfo.setTitulo(a.getTitulo());
					albumInfo.setIdAlbum(albumID);
					albumInfo.setEjemplaresVendidos(String.valueOf(a.getEjemplaresVendidos()));
					albumInfo.setFechaPublicacion(a.getFechaPublicacion());
					albumDataModel.put("album", albumInfo);
					Representation albumesVtl = new ClientResource(
							LocalReference.createClapReference(getClass().getPackage())+ "/Album.vtl").get();
							// Wrap bean with Velocity representation
							return new TemplateRepresentation(albumesVtl, albumDataModel, MediaType.TEXT_HTML);
				} catch (IOException e) {
				} catch (ExcepcionAlbumes e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExcepcionGruposMusicales e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			


	}
		return result;
	}
	
	@Put("form-data")
	public Representation anadiralbum (Representation datos) throws ParseException, ExcepcionPersonas, ResourceException, ExcepcionGruposMusicales{

		Form form = new Form(datos);		
		String CIF= form.getFirstValue("CIF");
		String titulo= form.getFirstValue("TITULO");
		String fechaPublicacion= form.getFirstValue("FECHAPUBLICACION");
		int ejemplaresVendidos= Integer.parseInt(form.getFirstValue("EJEMPLARESVENDIDOS"));
		//CIF=D0123456D&TITULO=Ave Maria&FECHAPUBLICACION=02-04-1999&EJEMPLARESVENDIDOS=6
		 System.out.println("CIF: " + titulo);
		 System.out.println("Titulo: " + titulo);
		 System.out.println("Fecha de publicacion: " + fechaPublicacion);
		 System.out.println("Numero de ejemplares vendidos: " + ejemplaresVendidos);
		 Representation result = null;
		 
		try {
			controladorGruposMusicales.crearAlbum(CIF, titulo, fechaPublicacion, ejemplaresVendidos);
			// Si se produce la expcion significa que la persona ya existe --> el usuario quiere hacer un put de modificacion
			 result =  new StringRepresentation("CIF: " + CIF +" Titulo: " + titulo+" Fecha de publicacion: " + fechaPublicacion+" Numero de ejemplares vendidos: " + ejemplaresVendidos,   MediaType.TEXT_HTML);
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
