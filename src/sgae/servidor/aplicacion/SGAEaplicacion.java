package sgae.servidor.aplicacion;
import java.text.ParseException;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.personas.ControladorPersonas;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.nucleo.personas.InterfazControladorPersonas;
import sgae.servidor.albumes.AlbumesServerResource;
import sgae.servidor.gruposMusicales.GrupoServerResource;
import sgae.servidor.gruposMusicales.GruposServerResource;
import sgae.servidor.personas.PersonaServerResource;
import sgae.servidor.personas.PersonasServerResource;

public class SGAEaplicacion extends Application{

	private ControladorPersonas controladorPersonas;
	private ControladorGruposMusicales controladorGruposMusicales;
	public SGAEaplicacion(){
		
		setName("SGAE Server Application");
		setDescription("Servidor de SGAE para almacenar informacion sobre grupos musicales");
		setOwner("YO");
		setAuthor("RHB y DBP");
		controladorPersonas = new ControladorPersonas();
		InterfazControladorPersonas cp = null;
		controladorGruposMusicales = new ControladorGruposMusicales(cp);
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

		} catch (ExcepcionGruposMusicales e) {
				System.err.println("Ha fallado una operación para la discográfica con CIF " + 
						e.getCif() + " por la siguiente razón: " + 
						e.getCausaFallo());
			} catch (ParseException e) {
				System.err.println("Alguna de las fechas proporcionadas no es válida.");
		}
	}
	
	@Override
	public Restlet createInboundRoot(){
		Router router = new Router (getContext());
		router.attach("/",sistema.class);
		router.attach("/personas/",PersonasServerResource.class);
		router.attach("/personas/{DNI}",PersonaServerResource.class);
//		router.attach("/discografias/",listapersonas.class);
//		router.attach("/discografias/{CIF}/",listadiscografias.class);
//		router.attach("/discografias/{CIF}/directores/",listadirectores.class);
//		router.attach("/discografias/{CIF}/directores/{DNI}",directores.class);
//		router.attach("/discografias/{CIF}/contratos/",listacontratos.class);
//		router.attach("/discografias/{CIF}/contratos/{CIFgrupo}",contratos.class);
//		router.attach("/discografias/{CIF}/masalaboral",masalaboral.class);
		router.attach("/grupos/",GruposServerResource.class);
		router.attach("/grupos/{CIFgrupo}/",GrupoServerResource.class);
		router.attach("/grupos/{CIFgrupo}/albumes/",AlbumesServerResource.class);
//		router.attach("/grupos/{CIFgrupo}/albumes/{titulo}/",albumes.class);
//		router.attach("grupos/{CIFgrupo}/albumes/{titulo}/pistas/",listapistas.class);
//		router.attach("grupos/{CIFgrupo}/albumes/{titulo}/pistas/{nombre}",pistas.class);
//		router.attach("grupos/{CIFgrupo}/miembros/",listamiembros.class);
//		router.attach("grupos/{CIFgrupo}/miembros/{DNI}",miembros.class);		
		
		return router;
	}
	
	public ControladorPersonas getControladorPersonas() {
		return controladorPersonas;
	}
	public ControladorGruposMusicales getControladorGruposMusicales() {
		return controladorGruposMusicales;
	}

}
