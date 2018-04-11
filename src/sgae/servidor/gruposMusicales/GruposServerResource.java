package sgae.servidor.gruposMusicales;
import java.util.List;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.GrupoMusicalesInfoBreve;
import sgae.util.generated.GruposMusicales;
import sgae.util.generated.Link;

public class GruposServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	
	@Get("txt")
	public String represent(){
		StringBuilder result = new StringBuilder();
		for (String grupo: controladorGruposMusicales.listarGruposMusicales()) {
			result.append((grupo == null) ? "" : grupo).append('\n');
		}
		return result.toString();
	}
	
	
	
	@Get("xml")
	public JaxbRepresentation<GruposMusicales> toXml() {
		
		GruposMusicales gruposXML = new GruposMusicales();	//XML
		final List<GrupoMusicalesInfoBreve> grupoInfoBreve= gruposXML.getGrupoMusicalesInfoBreve();
		
		for(sgae.nucleo.gruposMusicales.GrupoMusical g: controladorGruposMusicales.recuperarGruposMusicales() ) {
			
			GrupoMusicalesInfoBreve grupoInfo = new GrupoMusicalesInfoBreve();	
			grupoInfo.setCif(g.getCif());
			grupoInfo.setNombre(g.getNombre());
			Link link = new Link();
			link.setHref("/grupos/"+g.getCif());
			link.setTitle("Grupos");
			link.setType("simple");
			grupoInfo.setUri(link);
			grupoInfoBreve.add(grupoInfo);
			
		}
		
		JaxbRepresentation <GruposMusicales> result = new JaxbRepresentation<GruposMusicales> (gruposXML);
		result.setFormattedOutput(true);
		
		return result;
	}


}
