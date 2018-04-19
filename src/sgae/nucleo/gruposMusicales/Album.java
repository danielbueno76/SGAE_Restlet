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
 * Clase que almacena informaci�n sobre los �lbumes de un grupo musical.
 * @author Manuel Rodr�guez Cayetano. ETSIT UVa.
 * @version 1.0
 */
public class Album {
	/** Attributes */
	private String idAlbum;
	private String titulo;
	private Date fechaPublicacion;
	private int ejemplaresVendidos;
	
	/** Associations */
	
	/** La lista de pistas, indexada por un identificador �nico */
	private Map<String,Pista> listaPistas;
	/** Un contador para generar identificadores �nicos de pista */
	private int ultimaPista;
	
	/** 
	 * Constructor con los atributos que se pueden inicializar de partida. 
	 * Ojo, la fecha de publicaci�n se pasa como una cadena con el formato 
	 * dd-MM-yyyy .
	 *
	 * @param idAlbum el identificador del �lbum
	 * @param titulo el t�tulo del �lbum
	 * @param fechaPublicacion la fecha de publicaci�n del �lbum, se 
	 * pasa como una cadena dd-MM-yyyy
	 * @param ejemplaresVendidos n�mero de ejempares vendidos del �lbum
	 * @throws ParseException si el par�metro <i>fechaPublicacion</i> no tiene 
	 * el formato dd-MM-yyyy
	 */
	public Album(String idAlbum, String titulo, String fechaPublicacion, int ejemplaresVendidos) 
		throws ParseException {
		super();
		// Inicializa con valores pasados como par�metros
		this.idAlbum = Utils.testStringNullOrEmptyOrWhitespaceAndSet(idAlbum, "Campo idAlbum vac�o");
		this.titulo = Utils.testStringNullOrEmptyOrWhitespaceAndSet(titulo, "Campo t�tulo vac�o");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		if (Utils.isStringNullOrEmptyOrWhitespace(fechaPublicacion)) {
			throw new ParseException("Campo fecha de publicaci�n vac�o", 0);
		}
		this.fechaPublicacion = dateFormat.parse(fechaPublicacion);
		this.ejemplaresVendidos = ejemplaresVendidos;
		// Inicializa una lista de pistas vac�a y el contador de pistas
		listaPistas = new HashMap<String,Pista>();
		ultimaPista = 0;
	}
	
	/**
	 * M�todo que lee el identificador del �lbum.
	 * NOTA: El identificador s�lo se puede leer, no escribir.
	 * @return el valor del identificador
	 */
	public String getId() {
		return idAlbum;
	}
	
	/**
	 * M�todo que devuelve el t�tulo del �lbum.
	 * @return el t�tulo del �lbum
	 */
	public String getTitulo() {
		return titulo;
	}
	
	/**
	 * M�todo que modifica el t�tulo.
	 * @param nuevoTitulo el nuevo t�tulo del nombre
	 */
	public void setTitulo(String nuevoTitulo) {
		titulo = Utils.testStringNullOrEmptyOrWhitespaceAndSet(nuevoTitulo, "Campo t�tulo vac�o");
	}
	
	/** 
	 * M�todo que devuelve la fecha de publicaci�n como una cadena.
	 * @return la fecha de publicaci�n en formato dd-MM-yyyy
	 */
	public String getFechaPublicacion() {
		return new SimpleDateFormat("dd-MM-yyyy").format(fechaPublicacion);
	}
	
	/**
	 * M�todo que cambia la fecha de publicaci�n a partir de una cadena.
	 * @param nuevaFechaPublicacion la nueva fecha de publicaci�n del �lbum
	 * @throws ParseException si el par�metro <i>fechaPublicacion</i> no tiene 
	 * el formato dd-MM-yyyy
	 */
	public void setFechaPublicacion(String nuevaFechaPublicacion) 
		throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		if (Utils.isStringNullOrEmptyOrWhitespace(nuevaFechaPublicacion)) {
			throw new ParseException("Campo fecha de publicaci�n vac�o", 0);
		}
		this.fechaPublicacion = dateFormat.parse(nuevaFechaPublicacion);
	}
	
	/**
	 * M�todo que devuelve el n�mero de ejemplares vendidos del �lbum.
	 * @return el n�mero de ejemplares vendidos
	 */
	public int getEjemplaresVendidos() {
		return ejemplaresVendidos;
	}
	
	/**
	 * M�todo que cambia el n�mero de ejemplares vendidos del �lbum.
	 * @param nuevosEjemplaresVendidos el nuevo valor de los ejemplares vendidos
	 */
	public void setEjemplaresVendidos(int nuevosEjemplaresVendidos) {
		ejemplaresVendidos = nuevosEjemplaresVendidos;
	}
	
	
	/**
	 * M�todo que devuelve una descripci�n textual breve del �lbum.
	 * @return la descripci�n textual breve del �lbum
	 */
	String verDescripcionBreve() {
		return "T�tulo: " + titulo + "\n";
	}
	
	/**
	 * M�todo que devuelve en una �nica cadena la informaci�n completa del �lbum.
	 * 
	 * @return la descripci�n completa del �lbum
	 */
	String verDescripcionCompleta() {
		return "Id: " + idAlbum + 
			"\tTítulo: " + titulo +
			"\tFecha de publicación: " + fechaPublicacion +
			"\tEjemplares vendidos: " + ejemplaresVendidos + "\n";
	}
	
	/**
	 * M�todo que a�ade un pista, y devuelve su identificador �nico.
	 * @param nombre nombre de la pista a a�adir
	 * @param duracion duraci�n de la pista a a�adir
	 * @return el identificador �nico de la pista a�adira al �lbum
	 */
	public String anadirPista(String nombre, int duracion) {
		// Crea un identificador para la pista, formado por una 'p' y un 
		// n�mero auto-incrementado
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
	 * M�todo que comprueba si existe una pista identificada por un n�mero �nico
	 * @param id identificador de la pista
	 * @return objeto del tipo Pista correspondiente al identificador dado
	 * @throws ExcepcionPistas si no existe una pista con un identificador
	 * igual al valor del par�metro <i>id</i>
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
	 * M�todo que devuelve en una lista de cadenas la informaci�n de las 
	 * pistas.
	 * @return la lista formada por cadenas de texto, donde cada una 
	 * contiene la descripci�n de una pista
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
	 * M�todo que devuelve las pistas en una lista de objetos 
	 * de tipo Pista.
	 * @return la lista cuyos elementos son objetos del tipo Pista
	 */
	public List<Pista> recuperarPistas() {
		return new ArrayList<Pista>(listaPistas.values());
	}
	
	/**
	 * M�todo que permite ver la descripci�n textual de una pista de este �lbum.
	 * @param idPista el identificador �nico de la pista a mostrar
	 * @return una cadena con la descripci�n de la pista
	 * @throws ExcepcionPistas si no existe la pista que se busca
	 */
	public String verPista(String idPista) throws ExcepcionPistas {
		// Intenta obtener el objeto
		return comprobarPistaExiste(idPista).verDescripcionCompleta();
	}
	
	/**
	 * M�todo que permite obtener el objeto que representa a una pista dada
	 * en este �lbum
	 * @param idPista el identificador �nico de la pista a recuperar
	 * @return un objeto de tipo Pista
	 * @throws ExcepcionPistas si no existe la pista que se busca
	 */
	public Pista recuperarPista(String idPista) throws ExcepcionPistas {
		return comprobarPistaExiste(idPista);
	}

	/**
	 * Comprueba si el �lbum tiene una pista con un nombre determinado.
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
	 * M�todo que elimina una pista.
	 * @param idPista el identificador �nico de la pista a eliminar
	 * @throws ExcepcionPistas si no existe una pista con el idientificador que se 
	 * ha pasado como par�metro
	 */
	public void eliminarPista(String idPista) throws ExcepcionPistas {
		// Intenta borrar el objeto
		comprobarPistaExiste(idPista);
		listaPistas.remove(idPista);
	}    
}
