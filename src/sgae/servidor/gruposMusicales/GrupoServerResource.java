package sgae.servidor.gruposMusicales;

import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.GrupoMusical;
import sgae.servidor.aplicacion.SGAEaplicacion;

public class GrupoServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	
	protected void doInit() throws ResourceException{		
		this.grupoID = getAttribute("CIFgrupo");
	}
	


	@Get("txt")
	public String represent() throws ExcepcionGruposMusicales{
		return controladorGruposMusicales.verGrupoMusical(this.grupoID);

	}
	
	
	
	@Get("xml")
	public JaxbRepresentation<sgae.util.generated.GrupoMusical> toXml() throws ExcepcionGruposMusicales {
			GrupoMusical g = controladorGruposMusicales.recuperarGrupoMusical(this.grupoID);			
			sgae.util.generated.GrupoMusical grupoInfo = new sgae.util.generated.GrupoMusical();	
			grupoInfo.setCif(g.getCif());
			grupoInfo.setNombre(g.getNombre());
			grupoInfo.setFechaCreacion(g.getFechaCreacion());
		

		JaxbRepresentation <sgae.util.generated.GrupoMusical> result = new JaxbRepresentation<sgae.util.generated.GrupoMusical> (grupoInfo);
		result.setFormattedOutput(true);
		
		return result;

	}


}
