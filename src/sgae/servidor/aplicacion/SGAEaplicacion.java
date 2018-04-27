package sgae.servidor.aplicacion;
import java.text.ParseException;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Router;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionAlbumes;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionPistas;
import sgae.nucleo.personas.ControladorPersonas;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.nucleo.personas.InterfazControladorPersonas;
import sgae.servidor.albumes.AlbumServerResource;
import sgae.servidor.albumes.AlbumesServerResource;
import sgae.servidor.albumes.PistaServerResource;
import sgae.servidor.albumes.PistasServerResource;
import sgae.servidor.gruposMusicales.GrupoServerResource;
import sgae.servidor.gruposMusicales.GruposServerResource;
import sgae.servidor.gruposMusicales.MiembrosServerResource;
import sgae.servidor.personas.PersonaServerResource;
import sgae.servidor.personas.PersonasServerResource;

public class SGAEaplicacion extends Application{

	private ControladorPersonas controladorPersonas;
	private ControladorGruposMusicales controladorGruposMusicales;
	public SGAEaplicacion(){
		
		setName("SGAE Server Application");
		setDescription("Servidor de SGAE para almacenar información sobre grupos musicales");
		setOwner("ptpdx01");
		setAuthor("RHB y DBP");
		controladorPersonas = new ControladorPersonas();
		controladorGruposMusicales = new ControladorGruposMusicales(controladorPersonas);
		try {
		controladorPersonas.crearPersona("00000000A", "Bart", "Simpson","01-04-2003");
		controladorPersonas.crearPersona("11111111A", "Lisa", "Simpson","02-11-2006");
		} catch (ParseException e) {
			System.err.println("Alguna de las fechas proporcionadas no es válida.");
		} catch (ExcepcionPersonas e) {
			System.err.println("Ha fallado una operación para la persona con DNI " + 
					e.getDniPersona() + " por la siguiente razón: " + 
					e.getCausaFallo());
		}
		try {

			// Creación de grupos musicales
			controladorGruposMusicales.crearGrupoMusical("D0123456D", "Jamiroquai", "02-04-1992");
			controladorGruposMusicales.crearGrupoMusical("E0123456E", "Blur", "03-05-1988");
			controladorGruposMusicales.crearAlbum("D0123456D", "Piloto", "02-05-1994", 3);
			controladorGruposMusicales.crearAlbum("E0123456E", "Piloto", "09-07-1989", 5);
			

		} catch (ExcepcionGruposMusicales e) {
				System.err.println("Ha fallado una operación para la discográfica con CIF " + 
						e.getCif() + " por la siguiente razón: " + 
						e.getCausaFallo());
			} catch (ParseException e) {
				System.err.println("Alguna de las fechas proporcionadas no es válida.");
		}
		try {
			// Creación de grupos musicales
			controladorGruposMusicales.crearAlbum("D0123456D", "Ave Maria", "09-09-1999", 3);
			controladorGruposMusicales.crearAlbum("E0123456E", "Piloto2", "02-02-1982", 5);
			controladorGruposMusicales.anadirPista("D0123456D", "a0", "PistaHielo", 6);
			controladorGruposMusicales.anadirPista("D0123456D", "a1", "PistaFuego", 5);
		} catch (ExcepcionGruposMusicales e) {
				System.err.println("Ha fallado una operación para la discográfica con CIF " + 
						e.getCif() + " por la siguiente razón: " + 
						e.getCausaFallo());
			} catch (ParseException e) {
				System.err.println("Alguna de las fechas proporcionadas no es válida.");
		} catch (ExcepcionAlbumes e) {
			System.err.println("Error al crear pistas porque no existe el album.");
			}
		catch (ExcepcionPistas ax) {
			System.out.println("ExcepcionPistas Crearpista");

		}
		
		try {
			controladorGruposMusicales.anadirMiembro("D0123456D", "00000000A");
			controladorGruposMusicales.anadirMiembro("D0123456D", "11111111A");
			controladorGruposMusicales.anadirMiembro("E0123456E", "00000000A");
			controladorGruposMusicales.anadirMiembro("E0123456E", "11111111A");
			controladorGruposMusicales.eliminarMiembro("D0123456D", "00000000A");
		} catch (ExcepcionGruposMusicales e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExcepcionPersonas e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Restlet createInboundRoot(){
		Router router = new Router (getContext());
		router.attach("/",sistema.class);
		router.attach("/personas/",PersonasServerResource.class);
		router.attach("/personas/{DNI}",PersonaServerResource.class);
		router.attach("/grupos/",GruposServerResource.class);
		router.attach("/grupos/{CIFgrupo}/",GrupoServerResource.class);
		router.attach("/grupos/{CIFgrupo}/albumes/",AlbumesServerResource.class);
		router.attach("/grupos/{CIFgrupo}/albumes/{albumID}/",AlbumServerResource.class);
		router.attach("/grupos/{CIFgrupo}/albumes/{albumID}/pistas/",PistasServerResource.class);
		router.attach("/grupos/{CIFgrupo}/albumes/{albumID}/pistas/{nombre}",PistaServerResource.class);
		router.attach("/grupos/{CIFgrupo}/miembros",MiembrosServerResource.class);
		//router.attach("/grupos/{CIFgrupo}/miembros/../../../personas/{DNI}",MiembroServerResource.class);		
		return router;
	}
	
	public ControladorPersonas getControladorPersonas() {
		return controladorPersonas;
	}
	public ControladorGruposMusicales getControladorGruposMusicales() {
		return controladorGruposMusicales;
	}

}