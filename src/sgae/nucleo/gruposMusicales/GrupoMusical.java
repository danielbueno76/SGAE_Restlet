package sgae.nucleo.gruposMusicales;

import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sgae.nucleo.personas.Persona;
import sgae.util.Utils;
import sgae.nucleo.personas.ExcepcionPersonas;

/**
 * Clase que almacena informaci�n sobre los grupos musicales.
 * @author Manuel Rodr�guez Cayetano. ETSIT UVa.
 * @version 1.0
 */
public class GrupoMusical {
	/** Attributes */
	private String cif;
	private String nombre;
	private Date fechaCreacion;
	private boolean contratado;
	/** Associations */
	/**
	 * Mapa indexado por identificador con la lista de personas que son miembros actuales
	 * del grupo  (hacemos que esta clase mantenga la persistencia de esta lista)
	 */
	private Map<String, Persona> listaMiembrosActuales;
	
	/**
	 * Mapa indexado por identificador con la lista de personas que han sido miembros 
	 * del grupo (hacemos que esta clase mantenga la persistencia de esta lista)
	 */
	private Map<String, Persona> listaMiembrosAnteriores;
	/**
	 * Mapa indexado por identificador con la lista de �lbumes (hacemos que esta
	 * clase mantenga la persistencia de esta lista)
	 */
	private Map<String, Album> listaAlbumes;
	/** Un contador para generar identificadores �nicos de �lbumes */
	private int ultimoAlbum;

	/**
	 * Constructor con los campos b�sicos.
	 * @param cif el identificador del grupo musical
	 * @param nombre el nombre del grupo
	 * @param fechaCreacion fecha de creaci�n del grupo
	 * @throws ParseException si el par�metro <i>fechaCreacion</i> no tiene 
	 * el formato dd-MM-yyyy
	 */
	public GrupoMusical(String cif, String nombre, String fechaCreacion)
		throws ParseException {
		super();
		this.cif = Utils.testStringNullOrEmptyOrWhitespaceAndSet(cif, "Campo CIF vac�o");
		this.nombre = Utils.testStringNullOrEmptyOrWhitespaceAndSet(nombre, "Campo nombre vac�o");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		if (Utils.isStringNullOrEmptyOrWhitespace(fechaCreacion)) {
			throw new ParseException("Campo fecha de creaci�n vac�o", 0);
		}
		this.fechaCreacion = dateFormat.parse(fechaCreacion);
		contratado = false;
		listaMiembrosActuales = new HashMap<String, Persona>();
		listaMiembrosAnteriores = new HashMap<String, Persona>();
		// Inicializa la lista de �lbumes
		listaAlbumes = new HashMap<String, Album>();
		ultimoAlbum = 0;
	}

	/**
	 * M�todo que permite leer el CIF.
	 * NOTA: el CIF no se puede cambiar.
	 * @return el valor del CIF del grupo musical
	 */
	public String getCif() {
		return cif;
	}
	
	/**
	 * M�todo que permite leer el nombre.
	 * @return el nombre del grupo musical
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * M�todo que permite cambiar el nombre.
	 * @param nombre el nuevo nombre del grupo musical
	 */
 	public void setNombre(String nombre) {
		this.nombre = Utils.testStringNullOrEmptyOrWhitespaceAndSet(nombre, "Campo nombre vac�o");
	}

	/**
	 * M�todo que permite leer la fecha de creaci�n.
	 * @return la fecha de creaci�n del grupo musical
	 */
	public String getFechaCreacion() {
		return new SimpleDateFormat("dd-MM-yyyy").format(fechaCreacion);
	}

	/**
	 * M�todo que permite cambiar la fecha de creaci�n.
	 * @param fechaCreacion la nueva fecha de creaci�n del grupo musical
	 * @throws ParseException si el par�metro <i>fechaCreacion</i> no tiene 
	 * el formato dd-MM-yyyy
	 */
 	public void setFechaCreacion(String fechaCreacion) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		if (Utils.isStringNullOrEmptyOrWhitespace(fechaCreacion)) {
			throw new ParseException("Campo fecha de creaci�n vac�o", 0);
		}
		this.fechaCreacion = dateFormat.parse(fechaCreacion);
	}

	/** 
	 * M�todo que indica si el grupo est� contratado por una compa��a discogr�fica.
	 * @return valor booleano <i>true</i> si el grupo est� contratado
	 */
	public boolean estaContratado() {
		return contratado;
	}

	/**
	 * M�todo que cambia el estado del grupo musical a contratado por una 
	 * compa��a discogr�fica.
	 */
	public void contrata() {
		contratado = true;
	}

	/**
	 * M�todo que cambia el estado del grupo musical a no contratado 
	 * por una compa��a discogr�fica.
	 */
	public void despide() {
		contratado = false;
	}
	
	/**
	 * M�todo que devuelve una descripci�n textual breve del grupo musical.
	 * @return la descripci�n textual breve del grupo musical
	 */
	public String verDescripcionBreve() {
		return "CIF: " + cif + "\tNombre: " + nombre + "\n";
	}

	/**
	 * M�todo que devuelve en una �nica cadena la informaci�n completa del grupo
	 * musical.
	 * 
	 * @return la descripci�n completa del grupo musical
	 */
	public String verDescripcionCompleta() {
		return "CIF: " + cif + "\tNombre: " + nombre +
			"\tFecha de creación: " + fechaCreacion + "\n";
	}

	/**
	 * M�todo que devuelve el n�mero total de �lbumes de este grupo musical.
	 * 
	 * @return el n�mero total de �lbumes
	 */
	public int verNumeroAlbumes() {
		return listaAlbumes.size();
	}

	/**
	 * M�todo que a�ade un nuevo miembro al grupo musical.
	 * @param persona objeto de la clase Persona que va a a�adirse como miembro
	 * @throws ExcepcionPersonas si la persona ya aparece como miembro del grupo
	 */
	public void anadirMiembro(Persona persona) throws ExcepcionPersonas {
		if (listaMiembrosActuales.containsKey(persona.getDni()) == false) {
			listaMiembrosActuales.put(persona.getDni(), persona);
		} else {
			throw new ExcepcionPersonas(persona.getDni(),
							   "La persona que se ha intentado a�adir ya es miembro del grupo");
		}
	}
	

	/**
	 * M�todo que permite obtener una colecci�n de objetos que representan a
	 * todas las personas que son miembros del grupo.
	 * 
	 * @return una lista donde cada elemento es un objeto de la clase Persona
	 */
	public List<Persona> recuperarMiembros() {
		// Devuelve un objeto lista con los valores que hab�a en el mapa
		return new ArrayList<Persona>(listaMiembrosActuales.values());
	}

	/**
	 * M�todo que permite obtener una colecci�n de objetos que representan a
	 * todas las personas que han sido miembros del grupo.
	 * 
	 * @return una lista donde cada elemento es un objeto de la clase Persona
	 */
	public List<Persona> recuperarMiembrosAnteriores() {
		// Devuelve un objeto lista con los valores que hab�a en el mapa
		return new ArrayList<Persona>(listaMiembrosAnteriores.values());
	}

	/**
	 * M�todo que elimina un miembro del grupo musical y lo a�ade a la lista de miembros
	 * anteriores.
	 * @param dniPersona DNI de la persona que se va a eliminar como miembro
	 * @throws ExcepcionPersonas si la persona que se quiere eliminar no 
	 * forma parte del grupo
	 */
	public void eliminarMiembro(String dniPersona) throws ExcepcionPersonas {
		Persona p = listaMiembrosActuales.remove(dniPersona);
		if (p == null) {
			// la persona no era miembro del grupo
			throw new ExcepcionPersonas(dniPersona,
						   "La persona no era miembro del grupo");
		}
		if (listaMiembrosAnteriores.containsKey(p.getDni()) == false) {
			listaMiembrosAnteriores.put(p.getDni(), p);
		}
	}

	/**
	 * M�todo que crea un nuevo �lbum y lo a�ade a la colecci�n.
	 * 
	 * @param titulo
	 *            t�tulo del �lbum
	 * @param fechaPublicacion
	 *            fecha de publicaci�n del �lbum en formato dd-MM-yyyy
	 * @param ejemplaresVendidos
	 *            n�mero de ejemplares vendidos del �lbum
	 * @return el identificador del �lbum creado
	 * @throws ParseException si el par�metro <i>fechaPublicacion</i> no tiene 
	 * el formato dd-MM-yyyy
	 */	
	public String crearAlbum(String titulo, String fechaPublicacion, int ejemplaresVendidos)
			throws ParseException {
		// Crea un identificador para el �lbum, formado por una 'a' y un
		// n�mero auto-incrementado
		String idAlbum = "a" + ultimoAlbum;
		// Crea el objeto
		Album a = new Album(idAlbum, titulo, fechaPublicacion, ejemplaresVendidos);
		// La colecciona, indexada por identificador
		listaAlbumes.put(idAlbum, a);
		// Incrementa el contador
		ultimoAlbum++;
		return idAlbum;
	}

	/**
	 * M�todo que comprueba si existe un �lbum identificado por un n�mero �nico
	 * @param id identificador del grupo musical
	 * @return objeto del tipo Album correspondiente al identificador dado
	 * @throws ExcepcionAlbumes si no existe un �lbum con un identificador
	 * igual al valor del par�metro <i>id</i>
	 */
	private Album comprobarAlbumExiste (String id) 
		throws ExcepcionAlbumes {
		Album album = listaAlbumes.get(id);
		if (album == null) {
			throw new ExcepcionAlbumes(id,
							   "El �lbum que ha especificado no existe");
		}
		return album;
	}
	
	/**
	 * M�todo que permite modificar un �lbum, recibiendo todos los campos (el
	 * identificador de �lbum no puede cambiar).
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @param titulo
	 *            t�tulo del �lbum
	 * @param fechaPublicacion
	 *            fecha de publicaci�n del �lbum en formato dd-MM-yyyy
	 * @param ejemplaresVendidos
	 *            n�mero de ejemplares vendidos del �lbum
	 * @throws ParseException si el par�metro <i>fechaPublicacion</i> no tiene 
	 * el formato dd-MM-yyyy
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un identificador igual al valor
	 *             del campo <i>idAlbum</i>
	 */
	public void modificarAlbum(String idAlbum, String titulo,
				   String fechaPublicacion, int ejemplaresVendidos)
			throws ParseException, ExcepcionAlbumes {
		// Recupera la instancia
		Album a = comprobarAlbumExiste(idAlbum);
		a.setTitulo(titulo);
		a.setFechaPublicacion(fechaPublicacion);
		a.setEjemplaresVendidos(ejemplaresVendidos);
	}

	/**
	 * M�todo que devuelve una lista de �lbumes existentes en este 
	 * grupo musical, con su descripci�n breve.
	 * 
	 * @return una lista donde cada elemento es una cadena de texto con la
	 *         descripci�n breve de un �lbum
	 */
	public List<String> listarAlbumes() {
		List<String> listado = new ArrayList<String>();
		// Recorre la lista de �lbumes
		for (Album a : listaAlbumes.values()) {
			// A cada una le pide su descripci�n breve
			listado.add(a.verDescripcionBreve());
		}
		return listado;
	}

	/**
	 * M�todo que permite obtener una colecci�n de objetos que representan a
	 * todos los �lbumes existentes en este grupo musical.
	 * 
	 * @return una lista donde cada elemento es un objeto de la clase Album
	 */
	public List<Album> recuperarAlbumes() {
		// Devuelve un objeto lista con los valores que hab�a en el mapa
		return new ArrayList<Album>(listaAlbumes.values());
	}

	/**
	 * M�todo que permite ver los detalles de un �lbum en una cadena de texto.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @return cadena de texto con la descripci�n completa del �lbum
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador igual al
	 *             par�metro <i>idAlbum</i>
	 */
	public String verAlbum(String idAlbum) throws ExcepcionAlbumes {
		// Recupera la instancia
		Album a = comprobarAlbumExiste(idAlbum);
		// Le pide una descripci�n
		return a.verDescripcionCompleta();
	}

	/**
	 * M�todo que permite recuperar el objeto que representa a un �lbum.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @return un objeto de tipo Album
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador igual al
	 *             par�metro <i>idAlbum</i>
	 */
	public Album recuperarAlbum(String idAlbum) throws ExcepcionAlbumes {
		// Recupera la instancia
		Album a = comprobarAlbumExiste(idAlbum);
		return a;
	}

	/**
	 * M�todo que permite borrar un �lbum.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador igual al
	 *             par�metro <i>idAlbum</i>
	 */
	public void borrarAlbum(String idAlbum) throws ExcepcionAlbumes {
		// Borra la instancia
		comprobarAlbumExiste(idAlbum);
		listaAlbumes.remove(idAlbum);
	}

	/**
	 * M�todo que permite a�adir un pista a un �lbum.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @param nombre
	 *            nombre de la pita
	 * @param duracion
	 *            duraci�n de la pista
	 * @return identificador �nico de la pista reci�n creada
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de idAlbum igual al
	 *             par�metro <i>idAlbum</i>
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador igual al
	 *             par�metro <i>idAlbum</i>
	 */
	public String anadirPista(String idAlbum, String nombre, int duracion)
		throws ExcepcionAlbumes {
		// Recupera la instancia de Album
		Album a = comprobarAlbumExiste(idAlbum);
		return a.anadirPista(nombre, duracion);
	}

	/**
	 * M�todo que permite ver las pistas de un �lbum.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @return lista de cadenas de texto que contienen la informaci�n de un
	 *         pista
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador del �lbum
	 *             igual al par�metro <i>idAlbum</i>
	 */
	public List<String> listarPistas(String idAlbum) throws ExcepcionAlbumes {
		// Recupera la instancia
		Album a = comprobarAlbumExiste(idAlbum);
		return a.verPistas();
	}

	/**
	 * M�todo que permite recuperar la lista de objetos que representan las
	 * pistas de un �lbum.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @return lista de objetos de tipo Pista
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador igual al
	 *             par�metro <i>idAlbum</i>
	 */
	public List<Pista> recuperarPistas(String idAlbum) throws ExcepcionAlbumes {
		// Recupera la instancia
		Album a = comprobarAlbumExiste(idAlbum);
		return a.recuperarPistas();
	}

	/**
	 * M�todo que permite ver una pista concreta de un �lbum dado.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @param idPista
	 *            identificador �nico de la pista buscado
	 * @return texto con informaci�n de la pista
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador igual al
	 *             par�metro <i>idAlbum</i>
	 * @throws ExcepcionPistas
	 *             si no existe una pista con un valor de identificador igual al
	 *             valor del par�metro <i>idPista</i>
	 */
	public String verPista(String idAlbum, String idPista)
		throws ExcepcionAlbumes, ExcepcionPistas {
		// Recupera la instancia
		Album a = comprobarAlbumExiste(idAlbum);
		return a.verPista(idPista);
	}

	/**
	 * M�todo que permite recuperar el objeto que representa un pista concreto
	 * de un �lbum dada.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @param idPista
	 *            identificador �nico de la pista buscada
	 * @return objeto de tipo Pista cuyo identificador es igual al par�metro
	 *         <i>idPista</i>
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador igual al
	 *             par�metro <i>idAlbum</i>
	 * @throws ExcepcionPistas
	 *             si no existe una pista con un valor de identificador igual al
	 *             valor del par�metro <i>idPista</i>
	 */
	public Pista recuperarPista(String idAlbum, String idPista)
		throws ExcepcionAlbumes, ExcepcionPistas {
		// Recupera la instancia
		Album a = comprobarAlbumExiste(idAlbum);
		return a.recuperarPista(idPista);
	}

	/**
	 * M�todo que busca los �lbumes que entre sus pistas poseen una pista
	 * determinada, y para cada una consigue su descripci�n breve.
	 * 
	 * @param nombre
	 *            nombre de la pista buscada
	 * @return lista de cadenas de texto, donde cada una contiene la descripci�n
	 *         breve de un �lbum
	 */
	public List<String> buscarAlbumesConPista(String nombre) {
		List<String> listado = new ArrayList<String>();
		// Recorre la lista de �lbumes
		for (Album a : listaAlbumes.values()) {
			// Primero preguntamos si tiene la pista
			if (a.tienePista(nombre) == true) {
				// Si la tiene, entonces pedimos
				// la descripci�n breve
				listado.add(a.verDescripcionBreve());
			}
		}
		return listado;
	}

	/**
	 * M�todo que busca los �lbumes que entre sus pistas poseen una pista
	 * determinada, y devuelve los objetos que cumplen la condici�n.
	 * 
	 * @param nombre
	 *            nombre de la pista buscada
	 * @return lista de objetos de tipo Album
	 */
	public List<Album> recuperarAlbumesConPista(String nombre) {
		List<Album> listado = new ArrayList<Album>();
		// Recorre la lista de �lbumes
		for (Album a : listaAlbumes.values()) {
			// Primero preguntamos si tiene la pista
			if (a.tienePista(nombre) == true) {
				// Si la tiene, entonces a�adimos el objeto de la clase Album
				listado.add(a);
			}
		}
		return listado;
	}

	/**
	 * M�todo que permite borrar un pista de un �lbum.
	 * 
	 * @param idAlbum
	 *            identificador del �lbum
	 * @param idPista
	 *            identificador �nico de la pista
	 * @throws ExcepcionAlbumes
	 *             si no existe un �lbum con un valor de identificador igual al
	 *             par�metro <i>idAlbum</i>
	 * @throws ExcepcionPistas
	 *             si no existe un pista con un identificador igual al valor del
	 *             par�metro <i>idPista</i>
	 */
	public void eliminarPista(String idAlbum, String idPista)
		throws ExcepcionAlbumes, ExcepcionPistas {
		// Recupera la instancia
		Album a = comprobarAlbumExiste(idAlbum);
		a.eliminarPista(idPista);
	}
}
