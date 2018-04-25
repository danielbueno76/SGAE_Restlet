package sgae.nucleo.gruposMusicales;

import sgae.util.Utils;

/**
 * Clase que almacena informaci�n sobre las pistas de un �lbum.
 * @author Manuel Rodr�guez Cayetano. ETSIT UVa.
 * @version 1.0
 */
public class Pista {
	/** El identificador de la pista */
	private String idPista;
	/** El nombre de la pista */
	private String nombre;
	/** La duraci�n de la pista */
	private int duracion;

	/**
	 * Constructor con los campos b�sicos
	 * 
	 * @param idPista
	 *            el identificador de la pista
	 * @param nombre
	 *            el nombre de la pista
	 * @param duracion
	 *            la duraci�n de la pista
	 */
	public Pista(String idPista, String nombre, int duracion) {
		super();
		// Asigna campos b�sicos
		this.idPista = Utils.testStringNullOrEmptyOrWhitespaceAndSet(idPista, "Campo idPista vac�o");
		this.nombre = Utils.testStringNullOrEmptyOrWhitespaceAndSet(nombre, "Campo nombre vac�o");
		this.duracion = duracion;
	}

	/**
	 * M�todo que permite leer el identificador de la pista. NOTA: el
	 * identificador no se puede cambiar.
	 * 
	 * @return el valor del identificador de la pista
	 */
	public String getIdPista() {
		return idPista;
	}

	/**
	 * M�todo que permite leer el nombre.
	 * 
	 * @return el nombre de la pista
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * M�todo que permite cambiar el nombre.
	 * 
	 * @param nuevoNombre
	 *            el nuevo nombre de la pista
	 */
	public void setNombre(String nuevoNombre) {
		this.nombre = Utils.testStringNullOrEmptyOrWhitespaceAndSet(nuevoNombre, "Campo nombre vac�o");
	}

	/**
	 * M�todo que permite leer la duraci�n de la pista
	 * 
	 * @return la duraci�n de la pista en segundos
	 */
	public int getDuracion() {
		return duracion;
	}

	/**
	 * M�todo que permite cambiar la duraci�n.
	 * 
	 * @param nuevaDuracion
	 *            la nueva duraci�n de la pista
	 */
	public void setDuracion(int nuevaDuracion) {
		duracion = nuevaDuracion;
	}

	/**
	 * M�todo que permite recuperar una descripci�n breve de la pista.
	 * 
	 * @return descripci�n textual breve de la pista
	 */
	public String verDescripcionBreve() {
		return "nombre: " + nombre + "\n";
	}

	/**
	 * M�todo que devuelve en una �nica cadena la informaci�n completa de la
	 * pista.
	 * 
	 * @return la descripci�n textual completa de la pista
	 */
	public String verDescripcionCompleta() {
		return "Id: " + idPista + "\nNombre: " + nombre + "\nDuración: " + duracion + "segundos \n";
	}
}
