package sgae.servidor.gruposMusicales;

import java.text.ParseException;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import sgae.nucleo.gruposMusicales.Album;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.GrupoMusical;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.nucleo.personas.Persona;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.Link;
/**
 * Clase que muestra, crea, modifica y elimina un grupo musical específico. También añade y elimina miembros de un grupo.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */
public class GrupoServerResource extends ServerResource{
	
	/**Inicializamos las variables y controladores necesarios para grupo*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	/**Variable local grupoID que contiene el Cif de un grupo.*/
	private String grupoID;
	
	/** 
	 * Metodo doInit que se ejecuta al acceder a esta clase
	 */
	protected void doInit() throws ResourceException{		
		this.grupoID = getAttribute("CIFgrupo");
	}
	

	/** 
	 * Metodo get en formato texto plano sobre grupo
	 * @return result datos solicitados en texto plano
	 */
	@Get("txt")
	public String represent(){
		
		String DescripcionGrupo=null;
		int numerodejemplaresvendidos=0;
		try {
			//Almacenamos en la variable de tipo String la información completa del grupo y el URI relativo para el siguiente elemento. Luego este contenido se muestra.
			DescripcionGrupo= controladorGruposMusicales.verGrupoMusical(this.grupoID);
			//Obtenemos el umero de ejemplares vendidos
			for (Album album: controladorGruposMusicales.recuperarAlbumes(grupoID)) {		
				numerodejemplaresvendidos+=album.getEjemplaresVendidos();
			}
			DescripcionGrupo=DescripcionGrupo+"\tNúmero de ejemplares vendidos: "+numerodejemplaresvendidos+"\tURI: albumes/ \t o \t miembros/";
		}
		catch(ExcepcionGruposMusicales ex) {
			System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
		
		return DescripcionGrupo;//Devolvemos la variable de tipo String con todos los datos.
	}
	
	
	/** 
	 * Metodo get en formato xml sobre grupo
	 * @return result datos solicitados en xml
	 */
	@Get("xml")
	public JaxbRepresentation<sgae.util.generated.Grupo> toXml() {
		JaxbRepresentation <sgae.util.generated.Grupo> result = null;
		int numerodejemplaresvendidos=0;
		try {
			//Recuperamos el grupo musical especifico
			GrupoMusical g = controladorGruposMusicales.recuperarGrupoMusical(this.grupoID);
			//Obtenemos el umero de ejemplares vendidos
			for (Album album: controladorGruposMusicales.recuperarAlbumes(grupoID)) {		
				numerodejemplaresvendidos+=album.getEjemplaresVendidos();
			}
			//Añadimos al objeto grupo la información obtenida.
			sgae.util.generated.Grupo grupoInfo = new sgae.util.generated.Grupo();	
			grupoInfo.setCIF(g.getCif());
			grupoInfo.setNombre(g.getNombre());
			grupoInfo.setFechacreacion(g.getFechaCreacion());
			grupoInfo.setEjemplaresvendidos(String.valueOf(numerodejemplaresvendidos));
			//Creamos dos objetos de tipo link que son las dos colecciones que cuelgan.
			Link link1 = new Link();
			link1.setHref("albumes/");
			link1.setTitle("Albumes");
			link1.setType("simple");
			grupoInfo.setUri1(link1);
			Link link2 = new Link();
			link2.setHref("miembros");
			link2.setTitle("Miembros");
			link2.setType("simple");
			grupoInfo.setUri2(link2);
			//Se crea el objeto JaxbRepresentation result con el formato especificado en la teoría
			result = new JaxbRepresentation<sgae.util.generated.Grupo> (grupoInfo);
			result.setFormattedOutput(true);
		}catch(ExcepcionGruposMusicales ex) {
			System.out.println("ExcepcionGruposMusicales No existe el grupo");//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
			return result;//Devolvemos la variable de tipo JaxbRepresentation con todos los datos.

	}
	
	/** 
	 * Metodo put para crear un grupo o añadir y eliminar miembros.
	 * @param datos Variable en la cual recibimos los datos enviados por el cliente para modificar el album.
	 * @throws ParseException Excepcion producida cuando el cliente envía los datos para crear o  modificar de manera errónea.
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 * @return result datos solicitados en el formato solicitado
	 */
	@Put("form-data")
	public Representation annadirGrupo (Representation datos){
		Representation result = null;//En esta variable se almacenará el contenido que devolveremos en modo Representation.
		//Capturamos los datos de la cabecera
		Form form = new Form(datos);	
		//Almacenamos en variables locales los datos necesarios.
		String CIF= this.grupoID;
		String DNI= form.getValues("DNI");
		//Almacenamos en variables locales los datos necesarios para crear o modificar un grupo.
		String Nombre= form.getFirstValue("NOMBRE");
		String FechaCreacion= form.getFirstValue("FECHACREACION");
//		//Mostramos los datos almacenados por si acaso.
//		System.out.println("CIF: " + CIF );
//		System.out.println("Nombre: " + Nombre);
//		System.out.println("Fecha de creacion: " + FechaCreacion);
		//Queremos crear un grupo y no añadir miembros
		if (DNI==null &&Nombre!=null &&FechaCreacion!=null) {	
			
			try {
				//Creamos un grupo
				controladorGruposMusicales.crearGrupoMusical(CIF, Nombre, FechaCreacion);
				//Almacenamos la información que vamos a mostrar al crear el grupo.
				result =  new StringRepresentation("CIF: " + CIF +"\tNombre: " + Nombre+"\tFecha de creacion: " + FechaCreacion,   MediaType.APPLICATION_WWW_FORM);
				getResponse().setStatus(Status.SUCCESS_CREATED, "Se ha creado con exito");
			} 
			//Controlamos las posibles excepciones que se pueden producir
			catch (ExcepcionGruposMusicales ex){
				System.out.println("ExcepcionGruposMusicales crearGrupo");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
				
			}catch (ParseException ax) {
				System.out.println("ParseException creargrupo");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Te falta un parámetro o has enviado un parámetro con una sintaxis errónea");//Se devuelve un error --> Los datos enviados por el usuario han sido enviado de manera errónea.
			}
		}
		//Queremos crear un grupo y añadir miembros
		else if (DNI!=null &&Nombre!=null &&FechaCreacion!=null) {
			String[] arrayDNIs = DNI.split(",");//Dividimos la ristra de DNIS, en varios String.
			int i=0;
			String listamiembros = "";
			try {
				//Creamos un grupo
				controladorGruposMusicales.crearGrupoMusical(CIF, Nombre, FechaCreacion);
				//añadimos los miembros
				for(i=0;i<arrayDNIs.length;i++) {
					//Vamos añadiendo en cada iteración del bucle los nuevos miembros
					controladorGruposMusicales.anadirMiembro(CIF, arrayDNIs[i]);
					listamiembros=listamiembros+arrayDNIs[i]+"\t";
				}
				//Almacenamos la información que vamos a mostrar al crear el grupo.
				result =  new StringRepresentation("CIF: " + CIF +"\tNombre: " + Nombre+"\tFecha de creacion: " + FechaCreacion +"\tMiembros añadidos: "+listamiembros,   MediaType.APPLICATION_WWW_FORM);
				getResponse().setStatus(Status.SUCCESS_CREATED, "Se ha creado con exito");
			} 
			//Controlamos las posibles excepciones que se pueden producir
			catch (ExcepcionGruposMusicales ex){
				System.out.println("ExcepcionGruposMusicales crearGrupo");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
				
			}catch (ParseException ax) {
				System.out.println("ParseException creargrupo");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Te falta un parámetro o has enviado un parámetro con una sintaxis errónea");//Se devuelve un error --> Los datos enviados por el usuario han sido enviado de manera errónea.
			} catch (ExcepcionPersonas e) {
				System.out.println("ExcepcionPersonas añadir Miembro");//No existe la persona solicitado
				 throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
			
			}
			
		}
		//añadir o eliminar un miembro.
		else {
			//Ejemplo: DNI=0000000A, 1111111A, 0000000B
			String[] arrayDNIs = DNI.split(",");//Dividimos la ristra de DNIS, en varios String.
			String[] arraymiembros= null;//Dividimos la lista de miembros en un array de strings.
			//Mostramos los DNIs para ver que estan bien
			System.out.print("DNIs: "); 
			int i=0;
			int j=0;
			int controlmiembrosannadir=0;//Esta variable auxiliar nos ayudará a saber si un dni de los que le pasamos es un miembro actual
			int controlmiembrosborrar=0;//Esta variable auxiliar nos ayudará a saber si borramos un miembro
			for(i=0;i<arrayDNIs.length;i++) {
				System.out.print(arrayDNIs[i] + ",\t");//Mostrar DNIs
			}

			
			try {
				StringBuilder result2 = new StringBuilder();//Creamos esta variable que almacenará la información de tipo StringBuilder
				//Significa que no existen miembros actuales para ese grupo.
				if (controladorGruposMusicales.recuperarMiembros(grupoID).isEmpty()) {
					
					for(i=0;i<arrayDNIs.length;i++) {
						//Vamos añadiendo en cada iteración del bucle los nuevos miembros
						controladorGruposMusicales.anadirMiembro(CIF, arrayDNIs[i]);
						result2.append("Se añade un miembro con DNI: " + arrayDNIs[i] +" al grupo con CIF: " + CIF).append('\n');
					}
					result = new StringRepresentation(result2.toString());//Devolvemos el StringRepresentation con toda la información.
					getResponse().setStatus(Status.SUCCESS_CREATED, "Se ha añadido con exito");
				}

					//En el caso de que haya un miembro solo y se quiera eliminarlo, se entra aquí. Se le pasa DNI= . Es decir campo vacío.
				else if(controladorGruposMusicales.recuperarMiembros(grupoID).size()==1 && arrayDNIs[0].trim().length()==0) {
						//significa que quieres borrar el miembro que hay
						for (Persona persona: controladorGruposMusicales.recuperarMiembros(grupoID)) {
							//Significa que hay que borrar este miembro
							controladorGruposMusicales.eliminarMiembro(CIF, persona.getDni());
							//Información que mostramos tras eliminar el miembro.
							result2.append("Se elimina un miembro con DNI: " + persona.getDni() +" del grupo con CIF: " + CIF).append('\n');
						}
						getResponse().setStatus(Status.SUCCESS_OK, "Se ha borrado con exito");
					}
					//Significa que queremos añadir o borrar un miembro.
				else {
						int tamannolistamiembros=controladorGruposMusicales.recuperarMiembros(grupoID).size();
						arraymiembros= new String[tamannolistamiembros];
						//Vamos comparando cada miembro con cada DNI de la lista que pasamos.
						for (Persona persona: controladorGruposMusicales.recuperarMiembros(grupoID)) {
							arraymiembros[j]=persona.getDni();
							j=j+1;
							for(i=0;i<arrayDNIs.length;i++) {
								//Significa que hay que borrar ese miembro, ya que no se encuentra en la lista proporcionada
								if(persona.getDni().equals(arrayDNIs[i])==true) {
									controlmiembrosborrar=1;//No queremos borrarlo
								
								}

							}
							if ( controlmiembrosborrar==0) {
								controladorGruposMusicales.eliminarMiembro(CIF, persona.getDni());
								//Añadimos la información del miembro borrado que mostraremos.
								result2.append("Se elimina un miembro con DNI: " + persona.getDni() +" del grupo con CIF: " + CIF).append('\n');
							}
							controlmiembrosborrar=0;
						}
						
						//Vamos comparando cada miembro con la lista de DNIs que pasamos.
							for(i=0;i<arrayDNIs.length;i++) {
								for(j=0;j<arraymiembros.length;j++) {
								//Significa que ese DNI no está incluido como miembro actual
								if(arraymiembros[j].equals(arrayDNIs[i])==true) {	
									controlmiembrosannadir=1;//Significa que el DNI pertenece a los miembros actuales	
								}

							}
								if(controlmiembrosannadir==0) {
									//añadimos el miembro
									controladorGruposMusicales.anadirMiembro(CIF, arrayDNIs[i]);
									//Añadimos la información que mostraremos.
									result2.append("Se añade un miembro con DNI: " + arrayDNIs[i] +" al grupo con CIF: " + CIF).append('\n');
								}
								//Si uno de los DNi de la lista del cliente, ya está incluido en la lista de miembros, se mantiene.
								else {
									result2.append("El miembro con DNI: " + arrayDNIs[i] +" del grupo con CIF: " + CIF + " ya pertenece a los miembros actuales").append('\n');
								}
								controlmiembrosannadir=0;
								
					}

				result = new StringRepresentation(result2.toString());	//Devolvemos la variable de tipo Representation con todos los datos.			
				getResponse().setStatus(Status.SUCCESS_OK, "Se han modificado los miembros con exito");
				}
			}
			catch (ExcepcionPersonas ex){
				System.out.println("ExcepcionPersonas añadir Miembro");//No existe la persona solicitado
				 throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ex.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
			}catch(ExcepcionGruposMusicales e)
			{
				System.out.println("ExcepcionGruposMusicales Borrar Album");//No existe el grupo solicitado
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
			}

		}

		
		return result;//Devolvemos la variable de tipo Representation con todos los datos.
		
	}
	
	
	/** 
	 * Metodo delete para borrar el grupo que especifiquemos
	 * @throws ExcepcionGruposMusicales No existe el grupo solicitado.
	 */
	@Delete
	public void remove(){
		
		try {
			//Borramos el Grupo
			controladorGruposMusicales.borrarGrupoMusical(this.grupoID);
			getResponse().setStatus(Status.SUCCESS_OK, "Se ha borrado con éxito");//Se ha borrado con exito.
			
		}
		catch(ExcepcionGruposMusicales e)
		{
			System.out.println("ExcepcionGruposMusicales Borrar Grupo");//No existe el grupo solicitado
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getCausaFallo());//Se devuelve un error --> Recurso no encontrado.
		}
	}


}