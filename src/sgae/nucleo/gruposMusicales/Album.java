package sgae.nucleo.gruposMusicales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sgae.util.Utils;

/**
 * Clase que almacena informacin sobre los lbumes de un grupo musical.
 * @author Manuel Rodrguez Cayetano. ETSIT UVa.
 * @version 1.0
 */
public class Album {
	/** Attributes */
	private String idAlbum;
	private String titulo;
	private Date fechaPublicacion;
	private int ejemplaresVendidos;
	
	/** Associations */
	
	/** La lista de pistas, indexada por un identificador nico */
	private Map<String,Pista> listaPistas;
	/** Un contador para generar identificadores nicos de pista */
	private int ultimaPista;
	
	/** 
	 * Constructor con los atributos que se pueden inicializar de partida. 
	 * Ojo, la fecha de publicacin se pasa como una cadena con el formato 
	 * dd-MM-yyyy .
	 *
	 * @param idAlbum el identificador del lbum
	 * @param titulo el ttulo del lbum
	 * @param fechaPublicacion la fecha de publicacin del lbum, se 
	 * pasa como una cadena dd-MM-yyyy
	 * @param ejemplaresVendidos nmero de ejempares vendidos del lbum
	 * @throws ParseException si el parmetro <i>fechaPublicacion</i> no tiene 
	 * el formato dd-MM-yyyy
	 */
	public Album(String idAlbum, String titulo, String fechaPublicacion, int ejemplaresVendidos) 
		throws ParseException {
		super();
		// Inicializa con valores pasados como parmetros
		this.idAlbum = Utils.testStringNullOrEmptyOrWhitespaceAndSet(idAlbum, "Campo idAlbum vaco");
		this.titulo = Utils.testStringNullOrEmptyOrWhitespaceAndSet(titulo, "Campo ttulo vaco");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		if (Utils.isStringNullOrEmptyOrWhitespace(fechaPublicacion)) {
			throw new ParseException("Campo fecha de publicacin vaco", 0);
		}
		this.fechaPublicacion = dateFormat.parse(fechaPublicacion);
		this.ejemplaresVendidos = ejemplaresVendidos;
		// Inicializa una lista de pistas vaca y el contador de pistas
		listaPistas = new HashMap<String,Pista>();
		ultimaPista = 0;
	}
	
	/**
	 * Mtodo que lee el identificador del lbum.
	 * NOTA: El identificador slo se puede leer, no escribir.
	 * @return el valor del identificador
	 */
	public String getId() {
		return idAlbum;
	}
	
	/**
	 * Mtodo que devuelve el ttulo del lbum.
	 * @return el ttulo del lbum
	 */
	public String getTitulo() {
		return titulo;
	}
	
	/**
	 * Mtodo que modifica el ttulo.
	 * @param nuevoTitulo el nuevo ttulo del nombre
	 */
	public void setTitulo(String nuevoTitulo) {
		titulo = Utils.testStringNullOrEmptyOrWhitespaceAndSet(nuevoTitulo, "Campo ttulo vaco");
	}
	
	/** 
	 * Mtodo que devuelve la fecha de publicacin como una cadena.
	 * @return la fecha de publicacin en formato dd-MM-yyyy
	 */
	public String getFechaPublicacion() {
		return new SimpleDateFormat("dd-MM-yyyy").format(fechaPublicacion);
	}
	
	/**
	 * Mtodo que cambia la fecha de publicacin a partir de una cadena.
	 * @param nuevaFechaPublicacion la nueva fecha de publicacin del lbum
	 * @throws ParseException si el parmetro <i>fechaPublicacion</i> no tiene 
	 * el formato dd-MM-yyyy
	 */
	public void setFechaPublicacion(String nuevaFechaPublicacion) 
		throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		if (Utils.isStringNullOrEmptyOrWhitespace(nuevaFechaPublicacion)) {
			throw new ParseException("Campo fecha de publicacin vaco", 0);
		}
		this.fechaPublicacion = dateFormat.parse(nuevaFechaPublicacion);
	}
	
	/**
	 * Mtodo que devuelve el nmero de ejemplares vendidos del lbum.
	 * @return el nmero de ejemplares vendidos
	 */
	public int getEjemplaresVendidos() {
		return ejemplaresVendidos;
	}
	
	/**
	 * Mtodo que cambia el nmero de ejemplares vendidos del lbum.
	 * @param nuevosEjemplaresVendidos el nuevo valor de los ejemplares vendidos
	 */
	public void setEjemplaresVendidos(int nuevosEjemplaresVendidos) {
		ejemplaresVendidos = nuevosEjemplaresVendidos;
	}
	
	
	/**
	 * Mtodo que devuelve una descripcin textual breve del lbum.
	 * @return la descripcin textual breve del lbum
	 */
	String verDescripcionBreve() {
		return "Ttulo: " + titulo + "\n";
	}
	
	/**
	 * Mtodo que devuelve en una nica cadena la informacin completa del lbum.
	 * 
	 * @return la descripcin completa del lbum
	 */
	String verDescripcionCompleta() {
		return "Id: " + idAlbum + 
			"\tTítulo: " + titulo +
			"\tFecha de publicación: " + fechaPublicacion +
			"\tEjemplares vendidos: " + ejemplaresVendidos + "\n";
	}
	
	/**
	 * Mtodo que aade un pista, y devuelve su identificador nico.
	 * @param nombre nombre de la pista a aadir
	 * @param duracion duracin de la pista a aadir
	 * @return el identificador nico de la pista aadira al lbum
	 */
	public String anadirPista(String nombre, int duracion) {
		// Crea un identificador para la pista, formado por una 'p' y un 
		// nmero auto-incrementado
		String idPista = "p" + ultimaPista;
		// Crea el objeto
		Pista p = new Pista(idPista, nombre, duracion);
		// Colecciona el objeto indexado por el identificador
		listaPistas.put(idPista, p);
		// Incrementa el contador
		ultimaPista++;
		// Retorna el identificador del objeto recién creado
		return idPista;
	}

	/**
	 * Mtodo que comprueba si existe una pista identificada por un nmero nico
	 * @param id identificador de la pista
	 * @return objeto del tipo Pista correspondiente al identificador dado
	 * @throws ExcepcionPistas si no existe una pista con un identificador
	 * igual al valor del parmetro <i>id</i>
	 */
	private Pista comprobarPistaExiste (String id) 
		throws ExcepcionPistas {
		Pista pista = listaPistas.get(id);
		if (pista == null) {
			throw new ExcepcionPistas(id,
							   "La pista que ha especificado no existe");
		}
		return pista;
	}
	
	/**
	 * Mtodo que devuelve en una lista de cadenas la informacin de las 
	 * pistas.
	 * @return la lista formada por cadenas de texto, donde cada una 
	 * contiene la descripcin de una pista
	 */
	public List<String> verPistas() {
		List<String> listado = new ArrayList<String>();
		
		// Recorre la lista de pistas
		for(Pista p : listaPistas.values()) {
			// Y a cada una le pide los detalles
			listado.add(p.verDescripcionCompleta());
		}
		return listado;
	}
	
	/**
	 * Mtodo que devuelve las pistas en una lista de objetos 
	 * de tipo Pista.
	 * @return la lista cuyos elementos son objetos del tipo Pista
	 */
	public List<Pista> recuperarPistas() {
		return new ArrayList<Pista>(listaPistas.values());
	}
	
	/**
	 * Mtodo que permite ver la descripcin textual de una pista de este lbum.
	 * @param idPista el identificador nico de la pista a mostrar
	 * @return una cadena con la descripcin de la pista
	 * @throws ExcepcionPistas si no existe la pista que se busca
	 */
	public String verPista(String idPista) throws ExcepcionPistas {
		// Intenta obtener el objeto
		return comprobarPistaExiste(idPista).verDescripcionCompleta();
	}
	
	/**
	 * Mtodo que permite obtener el objeto que representa a una pista dada
	 * en este lbum
	 * @param idPista el identificador nico de la pista a recuperar
	 * @return un objeto de tipo Pista
	 * @throws ExcepcionPistas si no existe la pista que se busca
	 */
	public Pista recuperarPista(String idPista) throws ExcepcionPistas {
		return comprobarPistaExiste(idPista);
	}

	/**
	 * Comprueba si el lbum tiene una pista con un nombre determinado.
	 * @param nombrePista nombre de la pista a buscar
	 * @return valor booleano <i>true</i> si existe la pista buscada
	 */
	public boolean tienePista (String nombrePista) {
		for (Pista p: listaPistas.values()) {
			if (p.getNombre().equals(nombrePista)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Mtodo que elimina una pista.
	 * @param idPista el identificador nico de la pista a eliminar
	 * @throws ExcepcionPistas si no existe una pista con el idientificador que se 
	 * ha pasado como parmetro
	 */
	public void eliminarPista(String idPista) throws ExcepcionPistas {
		// Intenta borrar el objeto
		comprobarPistaExiste(idPista);
		listaPistas.remove(idPista);
	}    
}
