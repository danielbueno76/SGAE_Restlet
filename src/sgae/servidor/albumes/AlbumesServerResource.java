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
import sgae.nucleo.gruposMusicales.Album;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.AlbumInfoBreve;
import sgae.util.generated.Albumes;
import sgae.util.generated.Link;
/**
 * Clase que muestra y crea álbumes de un grupo musical.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */

public class AlbumesServerResource extends ServerResource{
	
	/**Inicializamos las variables y controladores necesarios para Albumes*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	/**Variable local grupoID que contiene el Cif de un grupo.*/
	private String grupoID;
	

	/** 
	 * Metodo doInit que se ejecuta al acceder a esta clase
	 */
	protected void doInit() throws ResourceException {	
		//Añadimos los formatos que se van a poder negociar 
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));	//Get en texto plano
		getVariants().add(new Variant(MediaType.TEXT_HTML));	//Get en html
		getVariants().add(new Variant(MediaType.APPLICATION_WWW_FORM)); //Post en form-data
		this.grupoID = getAttribute("CIFgrupo");				//Almacenamos el identificador del grupo de la URI
		
	}
	
	/** 
	 * Metodo get sobre albumes con negociacion de contenido
	 * @param variant Formato de la variable enviada
	 * @throws IOException Excepcion producida cuando hay un problema al devolver TemplateRepresentation	
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @return result datos solicitados en el formato solicitado
	 */
	
	protected Representation get(Variant variant){	
		Representation result = null; //En esta variable se almacenará el contenido que devolveremos en modo Representation.
		StringBuilder result2 = new StringBuilder(); //Almacenamos el contenido a devolver en String.
	if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {		//Peticion de formato texto plano	
		try {
			//En este bucle nos devuelven los objetos de tipo Album que existen previamente.
			for (Album album: controladorGruposMusicales.recuperarAlbumes(grupoID)) {		
				//Almacenamos en la variable de tipo StringBuilder la información breve de cada album y el URI relativo para el siguiente elemento. Luego este contenido se muestra.
				result2.append((album == null) ? "" : "Título: " + album.getTitulo() + "\tUri: " + album.getId()+"/").append('\n');
			}
			//Capturamos las posibles excepciones que se pueden dar
		} catch (ExcepcionGruposMusicales e) {		
			System.out.println("ExcepcionGruposMusicales No existe el grupo");	//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());		//Se devuelve un error --> Recurso no encontrado.
		}
		result = new StringRepresentation(result2.toString());			//Devolvemos la representacion
	}
	
	else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) {		//Mismo proceso pero en formato HTML
		Albumes albumesHTML = new Albumes();	//Objeto de tipo albumes
		final List<AlbumInfoBreve> albumesInfoBreve= albumesHTML.getAlbumInfoBreve(); //Creamos una lista que contiene objetos albumes infobreve
		Map<String, Object> albumDataModel = new HashMap<String, Object>(); //Creamos un hashmap que contendrá la lista de objetos albumes con su informacion breve y con un identificador de tipo string.
		

			try {
				for(sgae.nucleo.gruposMusicales.Album a: controladorGruposMusicales.recuperarAlbumes(this.grupoID) ) {		//Recorremos todos los posibles albumes que se encuentran en el sistema
				
					//Creamos un objeto AlbumInfoBreve y le añadimos la información devuelta en cada iteración del bucle.
					AlbumInfoBreve albumInfo = new AlbumInfoBreve();	
					albumInfo.setTitulo(a.getTitulo());
					//Creamos un objeto de tipo Link y le añadimos los valores necesarios.
					Link link = new Link();
					link.setHref(a.getId()+"/");
					link.setTitle("Albumes");
					link.setType("simple");
					albumInfo.setUri(link);
					//Ahora añadidmos a lista de AlbumesInfoBreve el objeto AlbumInfoBreve con la información devuelta por el bucle.
					albumesInfoBreve.add(albumInfo);
					}
				albumDataModel.put("albumes", albumesInfoBreve);//Añadimos la lista de albumesInfoBreve con identificador albumes al HashMap.
				//Se crea el objeto Representation albumesVtl con el formato especificado en la teoría
				Representation albumesVtl = new ClientResource(
						LocalReference.createClapReference(getClass().getPackage())+ "/Albumes.vtl").get();
				// Devolvemos la información que mostraremos
				return new TemplateRepresentation(albumesVtl, albumDataModel, MediaType.TEXT_HTML);

			} 
			//Capturamos posibles excepciones
			  catch (IOException e) {	//Excepcion producida cuando hay un problema al devolver TemplateRepresentation			
				System.out.println("IOException GET HTML AlbumesServerResource");
				//Devolvemos el error 500 ServerError
			} catch (ExcepcionGruposMusicales e) {
				System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());		//Se devuelve un error --> Recurso no encontrado.
			}		

	}
	else {
		getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Cabecera mal introducida");
	}
		return result; //Devuelve la variable Representation
	}
	
	/**
	 * Post para añadir nuevos albumes sin poder escoger el identificador pero con con negociacion de contenido
	 * @param datos Variable en la cual recibimos los datos enviados por el cliente para añadir el album.
	 * @param variant Formato de la variable enviada
	 * @throws ParseException Excepcion producida cuando a la hora de crear un album el cliente envia datos de manera erronea.
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @return result datos añadidos
	 */
	protected Representation post (Representation datos, Variant variant) {
		Representation result = null; //En esta variable se almacenará el contenido que devolveremos en modo Representation.
		if (MediaType.APPLICATION_WWW_FORM.isCompatible(variant.getMediaType())) { //Si los parámetros que nos pasan estan en formato form-data seguimos adelante.
			Form form = new Form(datos);		//Cogemos los datos recibidos en la cabecera del mensaje.
			//Almacenamos los datos que usaremos a la hora de crear albumes.
			String CIF= this.grupoID;
			String titulo= form.getFirstValue("TITULO");
			String fechaPublicacion= form.getFirstValue("FECHAPUBLICACION");
			
			
			//Ejemplo: TITULO=Ave Maria&FECHAPUBLICACION=02-04-1999&EJEMPLARESVENDIDOS=6
//			//Mostramos las variables para ver que están bien.
//			 System.out.println("CIF: " + CIF);
//			 System.out.println("Titulo: " + titulo);
//			 System.out.println("Fecha de publicación: " + fechaPublicacion);
//			 System.out.println("Número de ejemplares vendidos: " + ejemplaresVendidos);
			 
			 
			try {
				int ejemplaresVendidos= Integer.parseInt(form.getFirstValue("EJEMPLARESVENDIDOS"));
				//Creamos un album llamando al metodo crearAlbum con la información necesaria
				String idAlbum=controladorGruposMusicales.crearAlbum(CIF, titulo, fechaPublicacion, ejemplaresVendidos);	
				//Almacenamos la información que vamos a mostrar al crear el Album.
				 result =  new StringRepresentation("CIF: " + CIF +"\t Título: " + titulo+" \tFecha de publicación: " + fechaPublicacion+" \tNúmero de ejemplares vendidos: " + ejemplaresVendidos + " \tUri: " + idAlbum+ "/",   MediaType.TEXT_PLAIN, Language.SPANISH, CharacterSet.ISO_8859_1);
				 getResponse().setStatus(Status.SUCCESS_CREATED, "Se ha creado con exito");
				
			}catch (ParseException ax) { //Salta esta excepción si a la hora de crear un album el cliente envia datos de manera erronea.
				 System.out.println("ParseException CrearAlbum");
				 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Te falta un parámetro o has enviado un parámetro con una sintaxis errónea");
			}catch (ExcepcionGruposMusicales ax) {
				System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ax.getCausaFallo());		//Se devuelve un error --> Recurso no encontrado.
			}catch(NumberFormatException ax) {
				System.out.println("NumberFormatException Numero de ejemplares mal introducido");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Numero de ejemplares mal introducido");		//Se devuelve un error --> Recurso no encontrado.
			}
		}
		else {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Cabecera mal introducida");
		}
		return result;  //Devolvemos la variable de tipo Representation
		

	}
	
	
	
}
