package sgae.servidor.aplicacion;
import java.io.IOException;
import org.restlet.data.Reference;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Clase que muestra la lista de elementos de la raíz principal de la aplicación.
 * @author Daniel Bueno Pacheco y Roberto Herreras Babón. ETSIT UVa.
 * @version 1.0
 */

public class sistema extends ServerResource{
	public sistema(){
		setNegotiated(true);
	}
	
	/** 
	 * Metodo doInit que se ejecuta al acceder a esta clase
	 */
	protected void doInit() throws ResourceException{
		System.out.println("Resource was initialized");		
	/** 
	 * Metodo doCatch para añadir tareas a la gestión estándar de excepciones
	 */
	}
	protected void doCatch(Throwable throwable){
		System.out.println("An exception was thrown in the root resource");		
		
	/** 
	 * Metodo doRelease(): para añadir tareas a la liberación estándar
	 */
	}
	protected void doRelease() throws ResourceException{
		System.out.println("Resource was released");		
	}
	
	/** 
	 * Metodo get (txt) sobre el sistema
	 * @return listas datos solicitados en texto plano
	 */
	@Get ("txt")
	public String represent(){
		String listas="Personas: personas/ \nGrupos: grupos/";	//Texto plano devuelto en el GET en Sistema
		return listas;
	}
	
	/** 
	 * Metodo get (xml) sobre el sistema
	 * @return listas datos solicitados en xml
	 */
	@Get("xml")
	public Representation toXml() throws IOException{ 	//Texto XML devuelto en el GET en Sistema
		
		DomRepresentation result = new DomRepresentation();//En esta variable se almacenará el contenido que devolveremos en modo Representation.
		result.setIndenting(true);
		
		Document doc = result.getDocument();
		
		Node sistemaElt = doc.createElement("sistema");		//Nodo sistema
		doc.appendChild(sistemaElt);
		
		Node personasElt = doc.createElement("Personas");	//Nodo personas que contiene un elemento link
		Element linkPersonas = doc.createElement("link");	//Elemento link --> 3 atributos (name, type and href)
		sistemaElt.appendChild(personasElt);				//Añadimos a sistema su hijo personas	
		personasElt.appendChild(linkPersonas);				//Añadimos a personas su hijo link
		linkPersonas.setAttribute("title", "Personas");		//Ponemos atributos
		linkPersonas.setAttribute("type", "simple");
		linkPersonas.setAttribute("href", "personas/");		
		
//Repetimos el proceso anterior para discografias. Pero lo comentamos porque no lo implementamos.
//		Node discografiasElt = doc.createElement("Discografias");	
//		Element linkDisco = doc.createElement("link");				
//		linkDisco.setAttribute("title", "Discografias");
//		linkDisco.setAttribute("type", "simple");
//		linkDisco.setAttribute("href", "/discografias/");
//		sistemaElt.appendChild(discografiasElt);
//		discografiasElt.appendChild(linkDisco);

		Node gruposElt = doc.createElement("Grupos");				//Repetimos el proceso grupos
		Element linkGrupos = doc.createElement("link");			
		linkGrupos.setAttribute("title", "Grupos");					
		linkGrupos.setAttribute("type", "simple");
		linkGrupos.setAttribute("href", "grupos/");
		sistemaElt.appendChild(gruposElt);
		gruposElt.appendChild(linkGrupos);
		
		Node sistemaRefElt = doc.createElement("sistemaRef");	//Mostramos la URL del sistema

		sistemaRefElt.setTextContent(new Reference(getReference(), "..")
				.getTargetRef().toString());
		sistemaElt.appendChild(sistemaRefElt);
		
		return result;//Devolvemos la variable de tipo Representation
	}

}
