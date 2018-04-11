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


public class sistema extends ServerResource{
	public sistema(){
		setNegotiated(true);
	}
	@Override
	protected void doInit() throws ResourceException{
		System.out.println("Resource was initialized");		
		
	}
	@Override
	protected void doCatch(Throwable throwable){
		System.out.println("An exception was thrown in the root resource");		
		
	}
	@Override
	protected void doRelease() throws ResourceException{
		System.out.println("Resource was released");		
		
	}
//	@Override
//	protected Representation get() throws ResourceException {
//		System.out.println("Estas usando el metodo get");
//		return new StringRepresentation("Esto es el recurso sistema");
//	}
	
	@Get ("txt")
	public String represent(){
		String listas="Personas: /personas/ \nDiscografias: /discografias/ \nGrupos: /grupos/";	//Texto plano devuelto en el GET en Sistema
		return listas;
	}
	
	@Get("xml")
	public Representation toXml() throws IOException{ 	//Texto XML devuelto en el GET en Sistema
		
		DomRepresentation result = new DomRepresentation();
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
		linkPersonas.setAttribute("href", "/personas/");
		
		
		
		Node discografiasElt = doc.createElement("Discografias");	//Repetimos el proceso anterior para discografias
		Element linkDisco = doc.createElement("link");				
		linkDisco.setAttribute("title", "Discografias");
		linkDisco.setAttribute("type", "simple");
		linkDisco.setAttribute("href", "/discografias/");
		sistemaElt.appendChild(discografiasElt);
		discografiasElt.appendChild(linkDisco);

		Node gruposElt = doc.createElement("Grupos");				//Repetimos el proceso grupos
		Element linkGrupos = doc.createElement("link");			
		linkGrupos.setAttribute("title", "Grupos");					
		linkGrupos.setAttribute("type", "simple");
		linkGrupos.setAttribute("href", "/grupos/");
		sistemaElt.appendChild(gruposElt);
		gruposElt.appendChild(linkGrupos);
		
		Node sistemaRefElt = doc.createElement("sistemaRef");	//Mostramos la URL del sistema
		sistemaRefElt.setTextContent(new Reference(getReference(), "..")
				.getTargetRef().toString());
		sistemaElt.appendChild(sistemaRefElt);
		
		return result;
	}

}
