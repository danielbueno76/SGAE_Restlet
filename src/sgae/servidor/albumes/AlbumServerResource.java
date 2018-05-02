package sgae.servidor.albumes;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.Language;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.velocity.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import sgae.nucleo.gruposMusicales.Album;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionAlbumes;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.servidor.aplicacion.SGAEaplicacion;
/**
 * Clase que muestra, modifica y elimina un álbum específico de un grupo musical.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */

public class AlbumServerResource extends ServerResource{
	
	/**Inicializamos las variables y controladores necesarios para Album*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	/**Variable local grupoID que contiene el Cif de un grupo.*/
	private String grupoID;
	/**Variable local albumID que contiene el id de un album.*/
	private String albumID;
	
	/** 
	 * Metodo doInit que se ejecuta al acceder a esta clase
	 */
	protected void doInit() throws ResourceException {
		//Añadimos los formatos que se van a poder negociar 
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));//Get en texto plano
		getVariants().add(new Variant(MediaType.TEXT_HTML));//Get en html
		getVariants().add(new Variant(MediaType.APPLICATION_WWW_FORM)); //Put en form-data
		this.grupoID = getAttribute("CIFgrupo");//Almacenamos el identificador del grupo de la URI
		this.albumID = getAttribute("albumID");//Almacenamos el identificador del album de la URI		
		
	}
	
	/** 
	 * Metodo get sobre album con negociacion de contenido
	 * @param variant Formato de la variable enviada
	 * @throws IOException Excepcion producida cuando hay un problema al devolver TemplateRepresentation	
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @throws ExcepcionAlbumes No existe el album solicitado.
	 * @return result datos solicitados en el formato solicitado
	 */
	protected Representation get(Variant variant) {
		Representation result = null;//En esta variable se almacenará el contenido que devolveremos en modo Representation.

	if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {		//Representacion para texto plano
		
		try {
			//Almacenamos en la variable de tipo StringRepresentation la información completa del album y el URI relativo para el siguiente elemento. Luego este contenido se muestra.
			result = new StringRepresentation(controladorGruposMusicales.verAlbum(grupoID, albumID)+"\tURI: pistas/");
		} 
		//Capturamos excepciones
		catch (ExcepcionAlbumes e) {
			System.out.println("ExcepcionAlbumes No existe el album");//No existe el album solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		} catch (ExcepcionGruposMusicales e) {
			System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,e.getCausaFallo());		//Se devuelve un error --> Recurso no encontrado.
		}
	}
	
	else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) { 	//Representacion para HTML

		Map<String, Object> albumDataModel = new HashMap<String, Object>();//Creamos un hashmap que contendrá el objeto album con su informacion completa y con un identificador de tipo string.
		
				try {
					//Creamos un objeto Album y le añadimos la información devuelta para este grupo y album.
					Album a = controladorGruposMusicales.recuperarAlbum(grupoID, albumID);				
					sgae.util.generated.Album albumInfo = new sgae.util.generated.Album();	
					albumInfo.setTitulo(a.getTitulo());
					albumInfo.setIdAlbum(albumID);
					albumInfo.setEjemplaresVendidos(String.valueOf(a.getEjemplaresVendidos()));
					albumInfo.setFechaPublicacion(a.getFechaPublicacion());	
					//Añadimos el album con identificador album al HashMap.
					albumDataModel.put("album", albumInfo);
					//Se crea el objeto Representation albumesVtl con el formato especificado en la teoría
					Representation albumesVtl = new ClientResource(
					LocalReference.createClapReference(getClass().getPackage())+ "/Album.vtl").get();
					// Devolvemos la información que mostraremos
					return new TemplateRepresentation(albumesVtl, albumDataModel, MediaType.TEXT_HTML);
							
				} 
				//Capturamos posibles excepciones
				catch (IOException e) {//Excepcion producida cuando hay un problema al devolver TemplateRepresentation			
					System.out.println("IOException GET HTML AlbumServerResource");
					//Devolvemos el error 500 ServerError
				} catch (ExcepcionAlbumes e) {
					System.out.println("ExcepcionAlbumes No existe el album");//No existe el album solicitado
					throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
				} catch (ExcepcionGruposMusicales e) {
					System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
					throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
				}			


	}else {
		getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Cabecera mal introducida");
	}
	return result;//Devolvemos la variable de tipo Representation con todos los datos.
	}
	
	/** 
	 * Metodo put para modificar alguno de los datos previamente introducidos de album
	 * @param datos Variable en la cual recibimos los datos enviados por el cliente para modificar el album.
	 * @param variant Formato de la variable enviada
	 * @throws IOException Excepcion producida cuando hay un problema al devolver TemplateRepresentation	
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @throws ExcepcionAlbumes No existe el album solicitado.
	 * @return result datos solicitados en el formato solicitado
	 */
	public Representation put (Representation datos, Variant variant){
		
		Representation result=null;
		if (MediaType.APPLICATION_WWW_FORM.isCompatible(variant.getMediaType())) {
			Form form = new Form(datos);			//Capturamos los datos de la cabecera
			//Almacenamos en variables locales los datos necesarios para modificar un album.
			String CIF= this.grupoID;
			String titulo= form.getFirstValue("TITULO");
			String fechaPublicacion= form.getFirstValue("FECHAPUBLICACION");

			//Ejemplo: TITULO=Ave Maria&FECHAPUBLICACION=02-04-1999&EJEMPLARESVENDIDOS=6
//			 System.out.println("CIF: " + CIF);
//			 System.out.println("Titulo: " + titulo);
//			 System.out.println("Fecha de publicacion: " + fechaPublicacion);
//			 System.out.println("Numero de ejemplares vendidos: " + ejemplaresVendidos);
			 
			try {
				int ejemplaresVendidos= Integer.parseInt(form.getFirstValue("EJEMPLARESVENDIDOS"));
				controladorGruposMusicales.modificarAlbum(CIF, this.albumID ,titulo, fechaPublicacion, ejemplaresVendidos);				//Modificamos los datos
				//Almacenamos la información que vamos a mostrar al crear el Album.
				 result =  new StringRepresentation("CIF: " + CIF +" Titulo: " + titulo+" Fecha de publicacion: " + fechaPublicacion+" Numero de ejemplares vendidos: " + ejemplaresVendidos,   MediaType.TEXT_PLAIN, Language.SPANISH, CharacterSet.ISO_8859_1);
			}
			//Controlamos las posibles excepciones que se pueden producir
			catch (ParseException ax) {
				System.out.println("ParseException Modificar Album");				
				 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);//Se devuelve un error --> Los datos enviados por el usuario han sido enviado de manera errónea.
			}
			catch (ExcepcionGruposMusicales ax) {
				System.out.println("ExcepcionGruposMusicales Modificar Album");//No existe el grupo solicitado
				 throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,ax.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
			}
			catch (ExcepcionAlbumes ax) {
				System.out.println("ExcepcionAlbumes Modificar Album");//No existe el album solicitado
				 throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,ax.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
			}
			catch(NumberFormatException ax) {
				System.out.println("NumberFormatException Numero de ejemplares mal introducido");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Numero de ejemplares mal introducido");		//Se devuelve un error --> Recurso no encontrado.
			}
		}else {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Cabecera mal introducida");
		}
		return result;//Devolvemos la variable de tipo Representation con todos los datos.

	}
	
	/** 
	 * Metodo delete para borrar el album que especifiquemos
	 * @param variant Formato de la variable enviada
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @throws ExcepcionAlbumes No existe el album solicitado.
	 */
	public Representation delete(Variant variant){	
		
		try {
			//borra el album llamando al metodo borrarAlbum
			controladorGruposMusicales.borrarAlbum(this.grupoID, this.albumID);
			getResponse().setStatus(Status.SUCCESS_OK, "Se ha borrado con éxito");//Se ha borrado con exito.
		}
		//Capturamos excepciones.
		catch(ExcepcionAlbumes e)
		{
			System.out.println("ExcepcionAlbumes Borrar Album");//No existe el album solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
		catch(ExcepcionGruposMusicales e)
		{
			System.out.println("ExcepcionGruposMusicales Borrar Album");//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
		return null;
	}
	
	
}
