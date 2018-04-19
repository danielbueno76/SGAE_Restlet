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
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.GrupoMusical;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.servidor.aplicacion.SGAEaplicacion;

public class GrupoServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	
	protected void doInit() throws ResourceException{		
		this.grupoID = getAttribute("CIFgrupo");
	}
	


	@Get("txt")
	public String represent(){
		
		String DescripcionGrupo=null;
		try {
			DescripcionGrupo= controladorGruposMusicales.verGrupoMusical(this.grupoID)+"\tURI: albumes/ \t o \t miembros/";
		}
		catch(ExcepcionGruposMusicales ex) {
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}
		
		return DescripcionGrupo;

	}
	
	
	
	@Get("xml")
	public JaxbRepresentation<sgae.util.generated.Grupo> toXml() {
		JaxbRepresentation <sgae.util.generated.Grupo> result = null;
		try {
			GrupoMusical g = controladorGruposMusicales.recuperarGrupoMusical(this.grupoID);			
			sgae.util.generated.Grupo grupoInfo = new sgae.util.generated.Grupo();	
			grupoInfo.setCIF(g.getCif());
			grupoInfo.setNombre(g.getNombre());
			grupoInfo.setFechacreacion(g.getFechaCreacion());
			result = new JaxbRepresentation<sgae.util.generated.Grupo> (grupoInfo);
			result.setFormattedOutput(true);
		}catch(ExcepcionGruposMusicales ex) {
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}
			return result;

	}
	
	
	@Put("form-data")
	public Representation annadirGrupo (Representation datos){

		Form form = new Form(datos);		
		String CIF= this.grupoID;
		String Nombre= form.getFirstValue("NOMBRE");
		String FechaCreacion= form.getFirstValue("FECHACREACION");

		System.out.println("CIF: " + CIF );
		System.out.println("Nombre: " + Nombre);
		System.out.println("Fecha de creacion: " + FechaCreacion);
		Representation result = null;
		 
		try {
			controladorGruposMusicales.crearGrupoMusical(CIF, Nombre, FechaCreacion);// Si se produce la expcion significa que la persona ya existe --> el usuario quiere hacer un put de modificacion
			 result =  new StringRepresentation("CIF: " + CIF +"\tNombre: " + Nombre+"\tFecha de creacion: " + FechaCreacion,   MediaType.TEXT_HTML);
		} catch (ExcepcionGruposMusicales ex){
			System.out.println("No se puede modficar un grupo ya creado");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			
		}catch (ParseException ax) {
			System.out.println("ParseException creargrupo");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}

		return result;
		
	}
	
	
	@Delete
	public void remove(){
		
		try {
		controladorGruposMusicales.borrarGrupoMusical(this.grupoID);
		}
		catch(ExcepcionGruposMusicales e)
		{
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	}


}