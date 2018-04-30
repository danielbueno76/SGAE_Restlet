package sgae.servidor.albumes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.restlet.data.CharacterSet;
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
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionAlbumes;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionPistas;
import sgae.nucleo.gruposMusicales.Pista;
import sgae.servidor.aplicacion.SGAEaplicacion;
/**
 * Clase que muestra, modifica y elimina una pista específica de un grupo musical.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */
public class PistaServerResource extends ServerResource{
	/**Inicializamos las variables y controladores necesarios para pista*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	/**Variable local grupoID que contiene el Cif de un grupo.*/
	private String grupoID;
	/**Variable local idAlbum que contiene el id de un album.*/
	private String idAlbum;
	/**Variable local idPista que contiene el id de una pista.*/
	private String idPista;
	
	/** 
	 * Metodo doInit que se ejecuta al acceder a esta clase
	 */
	protected void doInit() throws ResourceException{
		//Añadimos los formatos que se van a poder negociar 
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));//Get en texto plano
		getVariants().add(new Variant(MediaType.TEXT_HTML));//Get en html
		getVariants().add(new Variant(MediaType.APPLICATION_WWW_FORM)); //Put en form-data
		this.grupoID = getAttribute("CIFgrupo");//Almacenamos el identificador del grupo de la URI
		this.idAlbum = getAttribute("albumID");//Almacenamos el identificador del album de la URI
		this.idPista = getAttribute("pistasID");//Almacenamos el identificador de la pista de la URI
		
	}
	
	/** 
	 * Metodo get sobre pista con negociacion de contenido
	 * @param variant Formato de la variable enviada
	 * @throws IOException Excepcion producida cuando hay un problema al devolver TemplateRepresentation	
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @throws ExcepcionAlbumes No existe el album solicitado.
	 * @throws ExcepcionPistas No existe la pista solicitada.
	 * @return result datos solicitados en el formato solicitado
	 */
	protected Representation get(Variant variant){
		Representation result = null;//En esta variable se almacenará el contenido que devolveremos en modo Representation.

		if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {//Representacion para texto plano
			
			try {
				//Almacenamos en la variable de tipo StringRepresentation la información completa de la pista. Luego este contenido se muestra.
				result = new StringRepresentation(controladorGruposMusicales.verPista(grupoID, idAlbum, idPista),MediaType.TEXT_PLAIN, Language.SPANISH, CharacterSet.ISO_8859_1);
			} catch (ExcepcionAlbumes e) {
				System.out.println("ExcepcionAlbumes GetpistaTXT");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
			} catch (ExcepcionGruposMusicales e) {
				System.out.println("ExcepcionGruposMusicales GetpistaTXT");//No existe el album solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
			} catch(ExcepcionPistas e) {
				System.out.println("ExcepcionPistas GetpistaTXT");//No existe la pista solicitada
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
			}
		}
		else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) {//Representacion para HTML

			Map<String, Object> pistaDataModel = new HashMap<String, Object>();//Creamos un hashmap que contendrá el objeto pista con su informacion completa y con un identificador de tipo string.
			
					try {
						//Creamos un objeto Pista y le añadimos la información devuelta .
						Pista p = controladorGruposMusicales.recuperarPista(grupoID, idAlbum, idPista);				
						sgae.util.generated.Pista pistaInfo = new sgae.util.generated.Pista();	
						pistaInfo.setNombre(p .getNombre());
						pistaInfo.setIdPista(idPista);
						pistaInfo.setDuracion(String.valueOf(p .getDuracion()));
						//Añadimos el album con identificador pista al HashMap.
						pistaDataModel.put("pista", pistaInfo);
						//Se crea el objeto Representation pistaVtl con el formato especificado en la teoría
						Representation pistaVtl = new ClientResource(
						LocalReference.createClapReference(getClass().getPackage())+ "/Pista.vtl").get();
						// Devolvemos la información que mostraremos
						return new TemplateRepresentation(pistaVtl, pistaDataModel, MediaType.TEXT_HTML);
					} 
					//Capturamos posibles excepciones
					catch (IOException e) {//Excepcion producida cuando hay un problema al devolver TemplateRepresentation			
						System.out.println("IOException a la hora de mostrar pista en html");
					} catch (ExcepcionAlbumes e) {
						System.out.println("ExcepcionAlbumes No existe el album");//No existe el album solicitado
						throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
					} catch (ExcepcionGruposMusicales e) {
						System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
						throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
					} catch (ExcepcionPistas e ) {
						System.out.println("ExcepcionPistas No existe la pista");//No existe la pista solicitada
						throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
					}


		}
		
		return result;//Devolvemos la variable de tipo Representation con todos los datos.
		
	}
	
	
	/** 
	 * Metodo delete para borrar la pista que especifiquemos
	 * @param variant Formato de la variable enviada
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @throws ExcepcionAlbumes No existe el album solicitado.
	 * @throws ExcepcionPistas No existe la pista solicitada.
	 */
	public Representation delete(Variant variant){	
	
		
		try {
			//borra la pista llamando al metodo eliminarPista
			controladorGruposMusicales.eliminarPista(this.grupoID, this.idAlbum, this.idPista);
			getResponse().setStatus(Status.SUCCESS_OK, "Se ha borrado con éxito");//Se ha borrado con exito.
		}
		//Capturamos excepciones.
		catch(ExcepcionAlbumes e)
		{
			System.out.println("ExcepcionAlbumes Borrar Album");//No existe el album solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
		}
		catch(ExcepcionGruposMusicales e)
		{
			System.out.println("ExcepcionGruposMusicales Borrar Album");//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
		}
		catch(ExcepcionPistas ex){
			System.out.println("ExcepcionPistas Eliminarpista");//No existe la pista solicitada
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);//Se devuelve un error --> Recurso no encontrado.
		}
		return null;
		
	}


}