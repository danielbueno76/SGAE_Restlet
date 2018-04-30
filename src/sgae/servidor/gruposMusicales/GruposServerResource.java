package sgae.servidor.gruposMusicales;

import java.util.List;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.GrupoMusical;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.GrupoInfoBreve;
import sgae.util.generated.Grupos;
import sgae.util.generated.Link;
/**
 * Clase que muestra grupos musicales.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */
public class GruposServerResource extends ServerResource{
	
	/**Inicializamos la variable y controlador necesario para grupos*/
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	
	/** 
	 * Metodo get en formato texto plano sobre grupos
	 * @return result datos solicitados en texto plano
	 */
	@Get("txt")
	public String represent(){
		StringBuilder result = new StringBuilder();//Almacenamos el contenido a devolver en String.
		//En este bucle nos devuelven los objetos de tipo grupo que existen previamente.
		for (GrupoMusical grupo: controladorGruposMusicales.recuperarGruposMusicales()) {
			//Almacenamos en la variable de tipo StringBuilder la información breve de cada grupo y el URI relativo para el siguiente elemento. Luego este contenido se muestra.
			result.append((grupo == null) ? "" : "CIF: " + grupo.getCif() + "\tNombre: " + grupo.getNombre() + "\tURI: "+ grupo.getCif()+ "/\n").append('\n');
		}
		return result.toString();//Devolvemos el String en texto plano con toda la información.
	}
	
	/** 
	 * Metodo get en formato xml sobre grupos
	 * @return result datos solicitados en xml
	 */
	@Get("xml")
	public JaxbRepresentation<Grupos> toXml() {
		
		Grupos gruposXML = new Grupos();//Objeto de tipo grupos
		final List<GrupoInfoBreve> grupoInfoBreve= gruposXML.getGrupoInfoBreve();//Creamos una lista que contiene objetos grupos infobreve
		
		for(sgae.nucleo.gruposMusicales.GrupoMusical g: controladorGruposMusicales.recuperarGruposMusicales() ) {//Recorremos todos los posibles grupos que se encuentran en el sistema
			
			//Creamos un objeto GrupoInfoBreve y le añadimos la información devuelta en cada iteración del bucle.
			GrupoInfoBreve grupoInfo = new GrupoInfoBreve();	
			grupoInfo.setCIF(g.getCif());
			grupoInfo.setNombre(g.getNombre());
			//Creamos un objeto de tipo Link y le añadimos los valores necesarios.
			Link link = new Link();
			link.setHref(g.getCif()+"/");
			link.setTitle("Grupos");
			link.setType("simple");
			grupoInfo.setUri(link);
			//Ahora añadidmos a lista de GrupoInfoBreve el objeto GrupoInfoBreve con la información devuelta por el bucle.
			grupoInfoBreve.add(grupoInfo);
			
		}
		//Se crea el objeto de tipo JaxbRepresentation result con el formato especificado en la teoría
		JaxbRepresentation <Grupos> result = new JaxbRepresentation<Grupos> (gruposXML);
		result.setFormattedOutput(true);
		
		return result;//Devuelve la variable de tipo JaxbRepresentation
	}


}
