package sgae.servidor.aplicacion;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.routing.VirtualHost;


public class SGAEcomponent extends Component{
	
	public static void main(String[] args) throws Exception {
		
	new SGAEcomponent().start();
		
		
	}
	
	public SGAEcomponent(){
		
		setName("SGAE Server Component");
		setDescription("Servidor de SGAE para almacenar informacion sobre grupos musicales");
		setOwner("YO");
		setAuthor("RHB y DBP");
		
		Server server = new Server(new Context(), Protocol.HTTP, 8111);
		
		server.getContext().getParameters().set("tracing", "true");
		getServers().add(server);
		
		VirtualHost host = getDefaultHost();
		host.attachDefault(new SGAEaplicacion());	
		
		
	}
}
