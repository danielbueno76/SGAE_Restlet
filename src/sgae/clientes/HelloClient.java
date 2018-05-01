package sgae.clientes;
import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class HelloClient {

	public static void main(String[] args) throws Exception{
		
		//El cliente deberá:
		
		//Crear una persona
		System.out.println("Crear una persona:\n");
		ClientResource crearPersonaClientResource = new ClientResource ("http://localhost:8111/personas/" + "123456789A");
		try{
			Form f = new Form();
			f.add("NOMBRE", "David");
			f.add("APELLIDOS", "Bisbal");
			f.add("FECHANACIMIENTO", "11-11-1980");
			crearPersonaClientResource.put(f, MediaType.APPLICATION_WWW_FORM);
			
			if(crearPersonaClientResource.getStatus().equals(Status.SUCCESS_CREATED))
				System.out.println("La persona con DNI 123456789A se ha creado correctamente.");
			else
				System.out.println("No se ha creado la persona con DNI 123456789A. Error: "+crearPersonaClientResource.getStatus().getCode()+" "+crearPersonaClientResource.getStatus().getDescription());
		} catch (ResourceException e) {
			System.err.println("Error en la creación de la persona. Respuesta: "+e.getStatus().getDescription());
		}
		
		//Listar todas las personas
		System.out.println("\nListar todas las personas:\n");
		ClientResource listarPersonasClientResource = new ClientResource ("http://localhost:8111/personas/");
		try{
			Representation respuesta = listarPersonasClientResource.get(MediaType.TEXT_PLAIN);
			if(listarPersonasClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ninguna persona registrada en el servicio");
			}
			else{
				System.out.println("Formato: Texto plano");
				System.out.println(respuesta.getText());
				System.out.println();
			}
			respuesta = listarPersonasClientResource.get(MediaType.APPLICATION_XML);
			if(listarPersonasClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ninguna persona aún");
			}
			else{
				System.out.println("Formato: XML");
				System.out.println(respuesta.getText());
			}
		}catch(ResourceException e){
			System.err.println("Error al listar las personas. Respuesta: "+e.getStatus().getDescription());
			System.err.println(e.getResponse().getEntityAsText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error en la respuesta del servidor");
		}
		System.out.println();
		
		//Crear un grupo musical
		System.out.println("\nCrear un grupo:\n");
		ClientResource crearGrupoClientResource = new ClientResource ("http://localhost:8111/grupos/" + "Z0123456Z" +"/");
		try{
			Form f = new Form();
			f.add("NOMBRE", "Operacion Triunfo");
			f.add("FECHACREACION", "21-05-2001");
			crearGrupoClientResource.put(f, MediaType.APPLICATION_WWW_FORM);
			if(crearGrupoClientResource.getStatus().equals(Status.SUCCESS_CREATED))
				System.out.println("El grupo con CIF Z0123456Z se ha creado correctamente.");
			else
				System.out.println("No se ha creado el grupo con CIF Z0123456Z. Error: "+crearGrupoClientResource.getStatus().getCode()+" "+crearGrupoClientResource.getStatus().getDescription());
		
		} catch (ResourceException e) {
			System.err.println("Error en la creación del grupo. Respuesta recibida: "+e.getStatus().getDescription());
		}
		
		//Listar todos los grupos musicales
		System.out.println("\nListar todos los grupos musicales:\n");
		ClientResource listarGruposClientResource = new ClientResource ("http://localhost:8111/grupos/");
		try{
			Representation respuesta = listarGruposClientResource.get(MediaType.TEXT_PLAIN);
			if(listarGruposClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ningun grupo registrado en el servicio");
			}
			else{
				System.out.println("Formato: Texto plano");
				System.out.println(respuesta.getText());
				System.out.println();
			}
			respuesta = listarGruposClientResource.get(MediaType.APPLICATION_XML);
			if(listarGruposClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ningun grupo aún");
			}
			else{
				System.out.println("Formato: XML");
				System.out.println(respuesta.getText());
			}
		}catch(ResourceException e){
			System.err.println("Error al listar los grupos. Respuesta: "+e.getStatus().getDescription());
			System.err.println(e.getResponse().getEntityAsText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error en la respuesta del servidor");
		}
		System.out.println();
		
		
		//Añadir un miembro
		System.out.println("\nAñadir un miembro:\n");
		ClientResource annadirMiembroClientResource = new ClientResource ("http://localhost:8111/grupos/" + "Z0123456Z" +"/");
		try{
			Form f = new Form();
			f.add("DNI", "123456789A");
			annadirMiembroClientResource.put(f, MediaType.APPLICATION_WWW_FORM);
			if(annadirMiembroClientResource.getStatus().equals(Status.SUCCESS_CREATED))
				System.out.println("Se ha añadido el miembro 123456789A al grupo Z0123456Z correctamente.");
			else
				System.out.println("No se ha añadido el miembro 123456789A al grupo Z0123456Z . Error: "+annadirMiembroClientResource.getStatus().getCode()+" "+annadirMiembroClientResource.getStatus().getDescription());
		
		} catch (ResourceException e) {
			System.err.println("Error en la creación del grupo. Respuesta: "+e.getStatus().getDescription());
		}
		
		//Listar los miembros
		System.out.println("\nListar los miembros:\n");
		ClientResource listarMiembrosClientResource = new ClientResource ("http://localhost:8111/grupos/"+"Z0123456Z" + "/miembros");
		try{
			Representation respuesta = listarMiembrosClientResource.get(MediaType.TEXT_PLAIN);
			if(listarMiembrosClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ningun miembros aún en este grupo");
			}
			else{
				System.out.println("Formato: Texto plano");
				System.out.println(respuesta.getText());
				System.out.println();
			}
			respuesta = listarMiembrosClientResource.get(MediaType.APPLICATION_XML);
			if(listarMiembrosClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ningun miembros aún en este grupo");
			}
			else{
				System.out.println("Formato: XML");
				System.out.println(respuesta.getText());
			}
		}catch(ResourceException e){
			System.err.println("Error al listar los miembros. Respuesta: "+e.getStatus().getDescription());
			System.err.println(e.getResponse().getEntityAsText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error en la respuesta del servidor");
		}
		System.out.println();

		//DUDAS
		//Eliminar un miembro
		System.out.println("\nEliminar un miembro:\n");
		ClientResource eliminarMiembroClientResource = new ClientResource ("http://localhost:8111/grupos/" + "Z0123456Z" +"/");
		try{
			Form f = new Form();
			f.add("DNI", "123456789A");
			eliminarMiembroClientResource.put(f, MediaType.APPLICATION_WWW_FORM);
			if(eliminarMiembroClientResource.getStatus().equals(Status.SUCCESS_OK))
				System.out.println("Se ha eliminado el miembro 123456789A al grupo Z0123456Z correctamente.");
			else
				System.out.println("No se ha eliminado el miembro 123456789A al grupo Z0123456Z . Error: "+eliminarMiembroClientResource.getStatus().getCode()+" "+eliminarMiembroClientResource.getStatus().getDescription());
		
		} catch (ResourceException e) {
			System.err.println("Error en la creación del grupo. Respuesta: "+e.getStatus().getDescription());
		}
		
		//Fallos
		//Crear un album
		System.out.println("\nCrear un album:\n");
		ClientResource crearAlbumClientResource = new ClientResource ("http://localhost:8111/grupos/" + "Z0123456Z" +"/albumes/");
		try{
			Form f = new Form();
			f.add("TITULO", "Viva yo");
			f.add("FECHAPUBLICACION", "01-06-2001");
			f.add("EJEMPLARESVENDIDOS","10000");			
			crearAlbumClientResource.post(f, MediaType.APPLICATION_WWW_FORM);
			if(crearAlbumClientResource.getStatus().equals(Status.SUCCESS_CREATED))
				System.out.println("El album se ha creado correctamente.");
			else
				System.out.println("No se ha creado el album. Error: "+crearAlbumClientResource.getStatus().getCode()+" "+crearAlbumClientResource.getStatus().getDescription());
		
		} catch (ResourceException e) {
			System.err.println("Error en la creación del grupo. Respuesta: "+e.getStatus().getDescription());
		}
		
		
		//Correcto
		//Listar todos los albumes
		System.out.println("\nListar todos los albumes:\n");
		ClientResource listarAlbumClientResource = new ClientResource ("http://localhost:8111/grupos/" +"Z0123456Z" +"/albumes/");
		try{
			Representation respuesta = listarAlbumClientResource.get(MediaType.TEXT_PLAIN);
			if(listarAlbumClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ningun album registrado en el servicio");
			}
			else{
				System.out.println("Formato: Texto plano");
				System.out.println(respuesta.getText());
				System.out.println();
			}
			respuesta = listarAlbumClientResource.get(MediaType.TEXT_HTML);
			if(listarAlbumClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ningun album aún");
			}
			else{
				System.out.println("Formato: HTML");
				System.out.println(respuesta.getText());
			}
		}catch(ResourceException e){
			System.err.println("Error al listar los albumes. Respuesta: "+e.getStatus().getDescription());
			System.err.println(e.getResponse().getEntityAsText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error en la respuesta del servidor");
		}
		System.out.println();
		
		//Fallos
		//Crear una pista
		System.out.println("\nCrear una pista:\n");
		ClientResource crearPistaClientResource = new ClientResource ("http://localhost:8111/grupos/" + "Z0123456Z" +"/albumes/a0/pistas/");
		try{
			Form f = new Form();
			f.add("NOMBRE", "Viva yo");
			f.add("DURACION", "10000");
			crearPistaClientResource.post(f, MediaType.APPLICATION_WWW_FORM);
			if(crearPistaClientResource.getStatus().equals(Status.SUCCESS_CREATED))
				System.out.println("La pista se ha creado correctamente.");
			else
				System.out.println("No se ha creado la pista. Error: "+crearPistaClientResource.getStatus().getCode()+" "+crearPistaClientResource.getStatus().getDescription());
		
		} catch (ResourceException e) {
			System.err.println("Error en la creación del grupo. Respuesta: "+e.getStatus().getDescription());
		}
		
		//Correcto
		//Listar todas las pistas
		System.out.println("\nListar todas las pistas:\n");
		ClientResource listarPistasClientResource = new ClientResource ("http://localhost:8111/grupos/" +"Z0123456Z" +"/albumes/a0/pistas/");
		try{
			Representation respuesta = listarPistasClientResource.get(MediaType.TEXT_PLAIN);
			if(listarPistasClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ninguna pista registrada en el servicio");
			}
			else{
				System.out.println("Formato: Texto plano");
				System.out.println(respuesta.getText());
				System.out.println();
			}
			respuesta = listarPistasClientResource.get(MediaType.TEXT_HTML);
			if(listarPistasClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT)){
				System.out.println("No hay ninguna pista aún");
			}
			else{
				System.out.println("Formato: HTML");
				System.out.println(respuesta.getText());
			}
		}catch(ResourceException e){
			System.err.println("Error al listar las pistas. Respuesta: "+e.getStatus().getDescription());
			System.err.println(e.getResponse().getEntityAsText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error en la respuesta del servidor");
		}
		System.out.println();
		
		//Borrar una pista
		System.out.println("\nBorrar una pista:\n");
		ClientResource borrarPistaClientResource = new ClientResource ("http://localhost:8111/grupos/" + "Z0123456Z" +"/albumes/a0/pistas/p0");
		try{
			
			borrarPistaClientResource.delete();
			if(borrarPistaClientResource.getStatus().equals(Status.SUCCESS_OK)||borrarPistaClientResource.getStatus().equals(Status.SUCCESS_NO_CONTENT))
				System.out.println("La pista ha sido eliminada.");
			else
				System.out.println("No se ha eliminada la pista. Error: "+borrarPistaClientResource.getStatus().getCode()+" "+borrarPistaClientResource.getStatus().getDescription());
		
		} catch (ResourceException e) {
			System.err.println("Error en la eliminacion de la pista. Respuesta: "+e.getStatus().getDescription());
		} 
	}

}
