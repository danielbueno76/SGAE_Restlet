package sgae.servidor.aplicacion;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.routing.VirtualHost;

//Componente Restful que contiene la aplicación SGAE.
public class SGAEcomponent extends Component{
	
	public static void main(String[] args) throws Exception {
		//Cargamos el componente SgaeComponent
		new SGAEcomponent().start();		
	}
	
	public SGAEcomponent(){
		// establecemos las propiedades
		setName("SGAE Server Component");
		setDescription("Servidor de SGAE para almacenar informacion sobre grupos musicales");
		setOwner("ptpdx01");
		setAuthor("RHB y DBP");
		
		// Creamos el componente servidor HTTP
		Server server = new Server(new Context(), Protocol.HTTP, 8111);
		//Creamos el componente cliente CLAP.
		Client client = new Client(new Context(), Protocol.CLAP);
		//Permitimos el rastreo
		server.getContext().getParameters().set("tracing", "true");
		getServers().add(server);//Añadimos el conector
		getClients().add(client);//Añadimos el cliente
		//Adherimos la aplicación al host por defecto
		VirtualHost host = getDefaultHost();
		host.attachDefault(new SGAEaplicacion());	
		
	}
}