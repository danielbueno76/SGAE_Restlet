package sgae.servidor.albumes;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.ext.velocity.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import sgae.nucleo.gruposMusicales.Album;
import sgae.nucleo.gruposMusicales.ControladorGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionAlbumes;
import sgae.nucleo.gruposMusicales.ExcepcionGruposMusicales;
import sgae.nucleo.gruposMusicales.ExcepcionPistas;
import sgae.nucleo.gruposMusicales.GrupoMusical;
import sgae.nucleo.gruposMusicales.Pista;
import sgae.nucleo.personas.ExcepcionPersonas;
import sgae.servidor.aplicacion.SGAEaplicacion;

public class PistaServerResource extends ServerResource{
	
	SGAEaplicacion ref = (SGAEaplicacion)getApplication();
	ControladorGruposMusicales controladorGruposMusicales = ref.getControladorGruposMusicales();
	private String grupoID;
	private String idAlbum;
	private String idPista;
	
	protected void doInit() throws ResourceException{
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		getVariants().add(new Variant(MediaType.TEXT_HTML));
		this.grupoID = getAttribute("CIFgrupo");
		this.idAlbum = getAttribute("albumID");
		this.idPista = getAttribute("idPista");
		
	}
	
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Representation result = null;

		if (MediaType.TEXT_PLAIN.isCompatible(variant.getMediaType())) {
			
			try {
				result = new StringRepresentation(controladorGruposMusicales.verPista(grupoID, idAlbum, idPista));
			} catch (ExcepcionAlbumes e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("ExcepcionAlbumes GetpistaTXT");
			} catch (ExcepcionGruposMusicales e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("ExcepcionGruposMusicales GetpistaTXT");
			} catch(ExcepcionPistas e) {
				e.printStackTrace();
				System.out.println("ExcepcionPistas GetpistaTXT");
			}
		}
		else if (MediaType.TEXT_HTML.isCompatible(variant.getMediaType())) {

			Map<String, Object> pistaDataModel = new HashMap<String, Object>();
			
					try {
						Pista a = controladorGruposMusicales.recuperarPista(grupoID, idAlbum, idPista);				
						sgae.util.generated.Pista pistaInfo = new sgae.util.generated.Pista();	
						pistaInfo.setNombre(a.getNombre());
						pistaInfo.setIdPista(idPista);
						pistaInfo.setDuracion(String.valueOf(a.getDuracion()));
						pistaDataModel.put("album", pistaInfo);
						Representation pistaVtl = new ClientResource(
								LocalReference.createClapReference(getClass().getPackage())+ "/Pista.vtl").get();
								// Wrap bean with Velocity representation
								return new TemplateRepresentation(pistaVtl, pistaDataModel, MediaType.TEXT_HTML);
					} catch (IOException e) {
						
					} catch (ExcepcionAlbumes e) {
						System.out.println("ExcepcionAlbumes No existe el album");
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
					} catch (ExcepcionGruposMusicales e) {
						System.out.println("ExcepcionGruposMusicales No existe el grupo");
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
					} catch (ExcepcionPistas e ) {
						System.out.println("ExcepcionPistas No existe la pista");
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
					}


		}
		
		return result;
	}
	
	@Put("form")
	public Representation anadirPista (Representation datos) throws ExcepcionAlbumes, ExcepcionGruposMusicales{

		Form form = new Form(datos);		
		String CIF= this.grupoID;
		String idAlbum = this.idAlbum;
		String Nombre= form.getFirstValue("NOMBRE");
		String Duracion= form.getFirstValue("DURACION");
		int duracion = Integer.parseInt(Duracion);

		System.out.println("CIF: " + CIF );
		System.out.println("Album: " + idAlbum );
		System.out.println("Nombre: " + Nombre);
		System.out.println("Duracion: " + duracion);
		Representation result = null;
		 
		try {
			controladorGruposMusicales.anadirPista(CIF, idAlbum, Nombre, duracion);// Si se produce la expcion significa que la persona ya existe --> el usuario quiere hacer un put de modificacion
			 result =  new StringRepresentation("CIF: " + CIF +" Album: " + idAlbum+" Nombre: " + Nombre +"Duracion: "+Duracion,   MediaType.TEXT_HTML);
		} catch (ExcepcionAlbumes ex){
			System.out.println("ExcepcionAlbumes Crearpista");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			
		}catch (ExcepcionGruposMusicales ax) {
			System.out.println("ExcepcionGruposMusicales Crearpista");
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}

		return result;
		
	}
	
	
	@Delete
	public void remove () throws ExcepcionGruposMusicales, ExcepcionAlbumes, ExcepcionPistas{	
	
		
		try {
			controladorGruposMusicales.eliminarPista(this.grupoID, this.idAlbum, this.idPista);
		}
		catch(ExcepcionGruposMusicales ex){
			System.out.println("ExcepcionGruposMusicales Eliminarpista");
		}
		catch(ExcepcionAlbumes ex){
			System.out.println("ExcepcionAlbumes Eliminarpista");
		}
		catch(ExcepcionPistas ex){
			System.out.println("ExcepcionPistas Eliminarpista");
		}
		
	}


}