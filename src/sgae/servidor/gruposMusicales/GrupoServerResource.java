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
import sgae.nucleo.personas.Persona;
import sgae.servidor.aplicacion.SGAEaplicacion;
import sgae.util.generated.Link;

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
			Link link1 = new Link();
			link1.setHref("albumes/");
			link1.setTitle("Albumes");
			link1.setType("simple");
			grupoInfo.setUri1(link1);
			Link link2 = new Link();
			link2.setHref("miembros");
			link2.setTitle("Miembros");
			link2.setType("simple");
			grupoInfo.setUri2(link2);
			result = new JaxbRepresentation<sgae.util.generated.Grupo> (grupoInfo);
			result.setFormattedOutput(true);
		}catch(ExcepcionGruposMusicales ex) {
	        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}
			return result;

	}
	
	
	@Put("form-data")
	public Representation annadirGrupo (Representation datos){
		Representation result = null;
		Form form = new Form(datos);	
		String CIF= this.grupoID;
		String DNI= form.getValues("DNI");
		//Si DNI es nulo significa que queremos a�adir un grupo
		if (DNI==null) {
			
			String Nombre= form.getFirstValue("NOMBRE");
			String FechaCreacion= form.getFirstValue("FECHACREACION");
			System.out.println("CIF: " + CIF );
			System.out.println("Nombre: " + Nombre);
			System.out.println("Fecha de creacion: " + FechaCreacion);
			
			 
			try {
				controladorGruposMusicales.crearGrupoMusical(CIF, Nombre, FechaCreacion);// Si se produce la expcion significa que la persona ya existe --> el usuario quiere hacer un put de modificacion
				 result =  new StringRepresentation("CIF: " + CIF +"\tNombre: " + Nombre+"\tFecha de creacion: " + FechaCreacion,   MediaType.APPLICATION_WWW_FORM);
			} catch (ExcepcionGruposMusicales ex){
				System.out.println("No se puede modficar un grupo ya creado");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				
			}catch (ParseException ax) {
				System.out.println("ParseException creargrupo");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}
		}
		else {
			String[] arrayDNIs = DNI.split(",");
			System.out.print("DNIs: ");
			int i=0;
			for(i=0;i<arrayDNIs.length;i++) {
				System.out.print(arrayDNIs[i] + ",\t");//Mostrar DNIs
			}
			System.out.println("\nCIF grupo: " + CIF);

			try {
				StringBuilder result2 = new StringBuilder();
				if (controladorGruposMusicales.recuperarMiembros(grupoID).isEmpty()) {
					//Significa que no existen miembros actuales para ese grupo.
					for(i=0;i<arrayDNIs.length;i++) {
						//Significa que ese DNI no está incluido como miembro actual
						controladorGruposMusicales.anadirMiembro(CIF, arrayDNIs[i]);
						result2.append("Se añade un miembro con DNI: " + arrayDNIs[i] +" al grupo con CIF: " + CIF).append('\n');
					}
					result = new StringRepresentation(result2.toString());
				}
				else {
					if(controladorGruposMusicales.recuperarMiembros(grupoID).size()>arrayDNIs.length) {
						//significa que quieres borrar un miembro
						for (Persona persona: controladorGruposMusicales.recuperarMiembros(grupoID)) {
							for(i=0;i<arrayDNIs.length;i++) {
	
								if(persona.getDni().equals(arrayDNIs[i])==false) {
									//Significa que hay que borrar ese miembro
									controladorGruposMusicales.eliminarMiembro(CIF, persona.getDni());
									result2.append("Se elimina un miembro con DNI: " + persona.getDni() +" del grupo con CIF: " + CIF).append('\n');
									
								}
								else {
									result2.append("El miembro con DNI: " + arrayDNIs[i] +" del grupo con CIF: " + CIF + " se mantiene como miembro actual").append('\n');
								}
							}
						}
						
					}
					else {
						//Significa que queremos añadir un miembro.
						for (Persona persona: controladorGruposMusicales.recuperarMiembros(grupoID)) {
							for(i=0;i<arrayDNIs.length;i++) {
	
								if(persona.getDni().equals(arrayDNIs[i])==false) {
									//Significa que ese DNI no está incluido como miembro actual
									controladorGruposMusicales.anadirMiembro(CIF, arrayDNIs[i]);
									result2.append("Se añade un miembro con DNI: " + arrayDNIs[i] +" al grupo con CIF: " + CIF).append('\n');
									
								}
								else {
									result2.append("El miembro con DNI: " + arrayDNIs[i] +" del grupo con CIF: " + CIF + " ya pertenece a los miembros actuales").append('\n');
								}
							}
						}
					}

					result = new StringRepresentation(result2.toString());
				}

			}
			catch (ExcepcionPersonas ex){
				System.out.println("ExcepcionPersonas añadir Miembro");
				 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}catch (ExcepcionGruposMusicales e) {
				System.out.println("ExcepcionGruposMusicales añadir Miembro");
				 throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}

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