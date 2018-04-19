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

public class GruposServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	
	@Get("txt")
	public String represent(){
		StringBuilder result = new StringBuilder();
		for (GrupoMusical grupo: controladorGruposMusicales.recuperarGruposMusicales()) {
			result.append((grupo == null) ? "" : "CIF: " + grupo.getCif() + "\tNombre: " + grupo.getNombre() + "\tURI: "+ grupo.getCif()+ "/\n").append('\n');
		}
		return result.toString();
	}
	
	
	
	@Get("xml")
	public JaxbRepresentation<Grupos> toXml() {
		
		Grupos gruposXML = new Grupos();	//XML
		final List<GrupoInfoBreve> grupoInfoBreve= gruposXML.getGrupoInfoBreve();
		
		for(sgae.nucleo.gruposMusicales.GrupoMusical g: controladorGruposMusicales.recuperarGruposMusicales() ) {
			
			GrupoInfoBreve grupoInfo = new GrupoInfoBreve();	
			grupoInfo.setCIF(g.getCif());
			grupoInfo.setNombre(g.getNombre());
			Link link = new Link();
			link.setHref(g.getCif()+"/");
			link.setTitle("Grupos");
			link.setType("simple");
			grupoInfo.setUri(link);
			grupoInfoBreve.add(grupoInfo);
			
		}
		
		JaxbRepresentation <Grupos> result = new JaxbRepresentation<Grupos> (gruposXML);
		result.setFormattedOutput(true);
		
		return result;
	}


}
