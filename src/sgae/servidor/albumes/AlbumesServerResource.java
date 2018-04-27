package sgae.servidor.albumes;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.Component;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.Language;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
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
import org.restlet.util.ClientList;

import sgae.nucleo.gruposMusicales.Album;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.GrupoMusical;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.AlbumInfoBreve;
import sgae.util.generated.Albumes;
import sgae.util.generated.GrupoInfoBreve;
import sgae.util.generated.Grupos;
import sgae.util.generated.Link;

public class AlbumesServerResource extends ServerResource{
	
	//Inicializamos las variables y controladores necesarios para Albumes
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	
	@Override
	protected void doInit() throws ResourceException {		
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));	//Añadimos los formatos que se van a poder negociar 
		getVariants().add(new Variant(MediaType.TEXT_HTML));
		getVariants().add(new Variant(MediaType.APPLICATION_WWW_FORM));
		this.grupoID = getAttribute("CIFgrupo");				//Sacamos el identificador del grupo de la URI
		
	}
	
	@Override
	protected Representation get(Variant variant){		//Metodo   get sobre albumes con negociacion de contenido
		Representation result = null;
		StringBuilder result2 = new StringBuilder();
	if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {		//Peticion de formato plano	
		try {
			for (Album album: controladorGruposMusicales.recuperarAlbumes(grupoID)) {		//Mostramos la información breve de cada album y el URI relativo para el siguiente elemento
				result2.append((album == null) ? "" : "Título: " + album.getTitulo() + "\tUri: " + album.getId()+"/").append('\n');
			}
		} catch (ExcepcionGruposMusicales e) {		//Capturamos los posibles fallos que se puede dar
			System.out.println("ExcepcionGruposMusicales No existe el grupo");	//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);		//Se devuelve un error --> BAD REQUEST
		}
		result = new StringRepresentation(result2.toString());			//Devolvemos la representacion
	}
	
	else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) {		//Mismo proceso por en formato HTML
		Albumes albumesHTML = new Albumes();	//XML
		final List<AlbumInfoBreve> albumesInfoBreve= albumesHTML.getAlbumInfoBreve();
		Map<String, Object> albumDataModel = new HashMap<String, Object>();
		

			try {
				for(sgae.nucleo.gruposMusicales.Album a: controladorGruposMusicales.recuperarAlbumes(this.grupoID) ) {		//Recorremos todos los posibles albumes que se encuentran en el sistema
				
				AlbumInfoBreve albumInfo = new AlbumInfoBreve();	
				albumInfo.setTitulo(a.getTitulo());
				Link link = new Link();
				link.setHref(a.getId()+"/");
				link.setTitle("Albumes");
				link.setType("simple");
				albumInfo.setUri(link);
				albumesInfoBreve.add(albumInfo);
					}
				albumDataModel.put("albumes", albumesInfoBreve);
				Representation albumesVtl = new ClientResource(
						LocalReference.createClapReference(getClass().getPackage())+ "/Albumes.vtl").get();
						// Wrap bean with Velocity representation
						return new TemplateRepresentation(albumesVtl, albumDataModel, MediaType.TEXT_HTML);

			} catch (IOException e) {					//Capturamos posibles excepciones
				System.out.println("IOException GET HTML AlbumesServerResource");
				//throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			} catch (ExcepcionGruposMusicales e) {
				System.out.println("ExcepcionGruposMusicales No existe el grupo");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}
			
			

	}
		return result;
	}
	
	//@Post("form")				//Post para añadir nuevos albumes sin poder escoger el identificador	
	public Representation post (Representation datos, Variant variant) {
		Representation result = null;
		if (MediaType.APPLICATION_WWW_FORM.isCompatible(variant.getMediaType())) {
			Form form = new Form(datos);		//Cogemos los datos recibidos en la cabecera del mensaje
			String CIF= this.grupoID;
			String titulo= form.getFirstValue("TITULO");
			String fechaPublicacion= form.getFirstValue("FECHAPUBLICACION");
			int ejemplaresVendidos= Integer.parseInt(form.getFirstValue("EJEMPLARESVENDIDOS"));
			//TITULO=Ave Maria&FECHAPUBLICACION=02-04-1999&EJEMPLARESVENDIDOS=6
			 System.out.println("CIF: " + CIF);
			 System.out.println("Titulo: " + titulo);
			 System.out.println("Fecha de publicación: " + fechaPublicacion);
			 System.out.println("Número de ejemplares vendidos: " + ejemplaresVendidos);
			 
			 
			try {
				controladorGruposMusicales.crearAlbum(CIF, titulo, fechaPublicacion, ejemplaresVendidos);		//Creamos el album con la información necesaria
				 result =  new StringRepresentation("CIF: " + CIF +" Título: " + titulo+" Fecha de publicación: " + fechaPublicacion+" Número de ejemplares vendidos: " + ejemplaresVendidos + "Uri: " + getLocationRef().getIdentifier(),   MediaType.TEXT_PLAIN, Language.SPANISH, CharacterSet.ISO_8859_1);
			}catch (ParseException ax) {
				System.out.println("ParseException CrearAlbum");
				 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}catch (ExcepcionGruposMusicales ax) {
				System.out.println("ExcepcionGruposMusicales Ya existe el album");
				 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}
		}
		return result;

	}
	
	
	
}
