package sgae.servidor.albumes;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
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
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionAlbumes;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionPistas;
import sgae.nucleo.gruposMusicales.Pista;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.Link;
import sgae.util.generated.PistaInfoBreve;
import sgae.util.generated.Pistas;
/**
 * Clase que muestra y crea pistas de un álbum.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */
public class PistasServerResource extends ServerResource{
	
	/**Inicializamos las variables y controladores necesarios para pistas*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	/**Variable local grupoID que contiene el Cif de un grupo.*/
	private String grupoID;
	/**Variable local albumID que contiene el id de un album.*/
	private String idAlbum;
	
	/** 
	 * Metodo doInit que se ejecuta al acceder a esta clase
	 */
	protected void doInit() throws ResourceException{	
		//Añadimos los formatos que se van a poder negociar 
		getVariants().add(new Variant (MediaType.TEXT_HTML));//Get en texto plano
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));//Get en html
		getVariants().add(new Variant(MediaType.APPLICATION_WWW_FORM));//Post en form-data
		this.grupoID = getAttribute("CIFgrupo");//Almacenamos el identificador del grupo de la URI
		this.idAlbum = getAttribute("albumID");//Almacenamos el identificador del album de la URI		
	}
	
	/** 
	 * Metodo get sobre pistas con negociacion de contenido
	 * @param variant Formato de la variable enviada
	 * @throws IOException Excepcion producida cuando hay un problema al devolver TemplateRepresentation	
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @throws ExcepcionAlbumes No existe el album solicitado.
	 * @return result datos solicitados en el formato solicitado
	 */
	protected Representation get(Variant variant) throws ResourceException {
		Representation result = null;//En esta variable se almacenará el contenido que devolveremos en modo Representation.
		StringBuilder result2 = new StringBuilder();//Almacenamos el contenido a devolver en String.
		if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {		//Peticion de formato texto plano	
			try {
				//En este bucle nos devuelven los objetos de tipo Pista que existen previamente.
				for (Pista p: controladorGruposMusicales.recuperarPistas(grupoID,idAlbum)) {
					//Almacenamos en la variable de tipo StringBuilder la información breve de cada pista y el URI relativo para el siguiente elemento. Luego este contenido se muestra.
					result2.append((p == null) ? "" : "Título: " + p.getNombre() + "\tUri: " + p.getIdPista()).append('\n');
				}
			} 
			//Capturamos las posibles excepciones que se pueden dar
			catch (ExcepcionGruposMusicales e) {				
				System.out.println("ExcepcionGruposMusicales  listarpistas --> No existe el grupo");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
			} catch (ExcepcionAlbumes e) {
				System.out.println("ExcepcionGruposMusicales  listarpistas --> No existe el album");//No existe el album solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());	//Se devuelve un error --> Recurso no encontrado.
			}
			result = new StringRepresentation(result2.toString());//Devolvemos la representacion
		}
		else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) {//Mismo proceso pero en formato HTML
			Pistas pistasHTML = new Pistas();	//Objeto de tipo pistas
			final List<PistaInfoBreve> pistasInfoBreve= pistasHTML.getPistaInfoBreve();//Creamos una lista que contiene objetos pistasinfobreve
			Map<String, Object> pistaDataModel = new HashMap<String, Object>();//Creamos un hashmap que contendrá la lista de objetos pistas con su informacion breve y con un identificador de tipo string.
			

				try {
					for(sgae.nucleo.gruposMusicales.Pista a: controladorGruposMusicales.recuperarPistas(grupoID ,idAlbum) ) {//Recorremos todos las posibles pistas que se encuentran en el sistema
						//Creamos un objeto PistaInfoBreve y le añadimos la información devuelta en cada iteración del bucle.
					PistaInfoBreve pistaInfo = new PistaInfoBreve();	
					pistaInfo.setNombre(a.getNombre());
					//Creamos un objeto de tipo Link y le añadimos los valores necesarios.
					Link link = new Link();
					link.setHref(a.getIdPista());
					link.setTitle("Pistas");
					link.setType("simple");					
					pistaInfo.setUri(link);
					//Ahora añadidmos a lista de PistasInfoBreve el objeto PistaInfoBreve con la información devuelta por el bucle.
					pistasInfoBreve.add(pistaInfo);
				}
					
					pistaDataModel.put("pistas", pistasInfoBreve);//Añadimos la lista de pistasInfoBreve con identificador pistas al HashMap.
					//Se crea el objeto Representation pistasVtl con el formato especificado en la teoría
					Representation pistasVtl = new ClientResource(
					LocalReference.createClapReference(getClass().getPackage())+ "/Pistas.vtl").get();
					// Devolvemos la información que mostraremos
					return new TemplateRepresentation(pistasVtl, pistaDataModel, MediaType.TEXT_HTML);

				}
				//Capturamos posibles excepciones
				catch (IOException e) {//Excepcion producida cuando hay un problema al devolver TemplateRepresentation			
					System.out.println("IOException GET HTML PistasServerResource");
					//Devolvemos el error 500 ServerError
				} catch (ExcepcionGruposMusicales e) {
					System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
					throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
				} catch (ExcepcionAlbumes a) {
					System.out.println("ExcepcionGruposMusicales No existe el album");//No existe el album solicitado
					throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, a.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
				}
				
				

		}
		
		return result;//Devuelve la variable Representation
	}
	
	/**
	 * Post para añadir nuevas pistas sin poder escoger el identificador pero con con negociacion de contenido
	 * @param datos Variable en la cual recibimos los datos enviados por el cliente para añadir la pista.
	 * @param variant Formato de la variable enviada
	 * @throws ParseException Excepcion producida cuando a la hora de crear una pista el cliente envia datos de manera errónea.
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @return result datos añadidos
	 */
	protected Representation post (Representation datos, Variant variant) {
		Representation result = null;//En esta variable se almacenará el contenido que devolveremos en modo Representation.
		if (MediaType.APPLICATION_WWW_FORM.isCompatible(variant.getMediaType())) {//Si los parámetros que nos pasan estan en formato form-data seguimos adelante.
			Form form = new Form(datos);	//Cogemos los datos recibidos en la cabecera del mensaje.
			//Almacenamos los datos que usaremos a la hora de crear pistas.
			String CIF= this.grupoID;
			String idAlbum = this.idAlbum;
			String idPista= null;
			String Nombre= form.getFirstValue("NOMBRE");
			String Duracion= form.getFirstValue("DURACION");
			
//			//Mostramos las variables para ver que están bien.
//	  	    System.out.println("CIF: " + CIF);
//			System.out.println("Album: " + idAlbum );
//			System.out.println("Nombre: " + Nombre);
			 
			try {
				int duracion = Integer.parseInt(Duracion);//convertimos a int la variable duración ya que se crea en ese formato.
				//Creamos una pista llamando al metodo anadirPista con la información necesaria.
				idPista=controladorGruposMusicales.anadirPista(CIF, idAlbum, Nombre, duracion);//Nos devuelve el id de la pista recién creada.
				//Almacenamos la información que vamos a mostrar al crear la pista.
				result =  new StringRepresentation("CIF: " + CIF +"\t Album: " + idAlbum+"\t Nombre: " + Nombre +"\tDuracion: "+Duracion+"\tUri: " +idPista,   MediaType.TEXT_PLAIN, Language.SPANISH, CharacterSet.ISO_8859_1);
				getResponse().setStatus(Status.SUCCESS_CREATED, "Se ha creado con exito");
			} catch (ExcepcionAlbumes ex){
				System.out.println("ExcepcionAlbumes Crearpista");//No existe el album solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ex.getCausaFallo());		//Se devuelve un error --> Recurso no encontrado.		
			}catch (ExcepcionGruposMusicales ax) {
				System.out.println("ExcepcionGruposMusicales Crearpista");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ax.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
			}catch (ExcepcionPistas ax) {
				System.out.println("ExcepcionPistas Crearpista");//Error a la hora de crear la pista debido al envío erróneo de la información necesaria.
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Te falta un parámetro o has enviado un parámetro con una sintaxis errónea");
			}catch(NumberFormatException ax) {
				System.out.println("NumberFormatException Duracion mal introducida");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Duracion mal introducida");		//Se devuelve un error --> Recurso no encontrado.
			}
			
			
		}
		else {
			result = null;
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Cabecera mal introducida");
		}
		
		return result;//Devolvemos la variable de tipo Representation
	}	


}