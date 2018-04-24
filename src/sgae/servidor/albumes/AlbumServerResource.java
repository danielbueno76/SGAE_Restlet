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
import org.restlet.resource.Delete;
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
	
	//Inicializamos las variables y controladores necesarios para Album
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	private String albumID;
	
	@Override
	protected void doInit() throws ResourceException {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));	//Añadimos los formatos que se van a poder negociar y cogemos los datos de la URI
		getVariants().add(new Variant(MediaType.TEXT_HTML));
		this.grupoID = getAttribute("CIFgrupo");
		this.albumID = getAttribute("albumID");
		
	}
	
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Representation result = null;

	if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {		//Representacion para texto plano
		
		try {
			result = new StringRepresentation(controladorGruposMusicales.verAlbum(grupoID, albumID)+"\tURI: pistas/");
		} catch (ExcepcionAlbumes e) {
			System.out.println("ExcepcionAlbumes No existe el album");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		} catch (ExcepcionGruposMusicales e) {
			System.out.println("ExcepcionGruposMusicales No existe el grupo");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	}
	
	else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) { 	//Representacion para HTML

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
					
				} catch (ExcepcionAlbumes e) {		//Excepciones de recuperarAlbum
					System.out.println("ExcepcionAlbumes No existe el album");
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				} catch (ExcepcionGruposMusicales e) {
					System.out.println("ExcepcionGruposMusicales No existe el grupo");
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				}			


	}
		return result;
	}
	
	@Put("form-data")			//Funcion put para modificar alguno de los datos previamente introducidos de album
	public Representation anadiralbum (Representation datos){

		Form form = new Form(datos);			//Capturamos los datos de la cabecera
		String CIF= this.grupoID;
		String titulo= form.getFirstValue("TITULO");
		String fechaPublicacion= form.getFirstValue("FECHAPUBLICACION");
		int ejemplaresVendidos= Integer.parseInt(form.getFirstValue("EJEMPLARESVENDIDOS"));
		//CIF=D0123456D&TITULO=Ave Maria&FECHAPUBLICACION=02-04-1999&EJEMPLARESVENDIDOS=6
		 System.out.println("CIF: " + CIF);
		 System.out.println("Titulo: " + titulo);
		 System.out.println("Fecha de publicacion: " + fechaPublicacion);
		 System.out.println("Numero de ejemplares vendidos: " + ejemplaresVendidos);
		 Representation result = null;
		 
		try {
			controladorGruposMusicales.modificarAlbum(CIF, this.albumID ,titulo, fechaPublicacion, ejemplaresVendidos);				//Modificamos los datos
			// Si se produce la expcion significa que la persona ya existe --> el usuario quiere hacer un put de modificacion
			 result =  new StringRepresentation("CIF: " + CIF +" Titulo: " + titulo+" Fecha de publicacion: " + fechaPublicacion+" Numero de ejemplares vendidos: " + ejemplaresVendidos,   MediaType.TEXT_HTML);
		}catch (ParseException ax) {
			System.out.println("ParseException Modificar Album");				//Controlamos las posibles excepciones que se pueden producir
			 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		catch (ExcepcionGruposMusicales ax) {
			System.out.println("ExcepcionGruposMusicales Modificar Album");
			 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		catch (ExcepcionAlbumes ax) {
			System.out.println("ExcepcionAlbumes Modificar Album");
			 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		
		return result;

	}
	
	@Delete
	public void remove(){			//Funcion delete para borrar un album del sistema
		
		try {
		controladorGruposMusicales.borrarAlbum(this.grupoID, this.albumID);
		}
		catch(ExcepcionAlbumes e)
		{
			System.out.println("ExcepcionAlbumes Borrar Album");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		catch(ExcepcionGruposMusicales e)
		{
			System.out.println("ExcepcionGruposMusicales Borrar Album");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	}
	
	
}
