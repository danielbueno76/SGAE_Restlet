//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2018.04.25 a las 12:30:43 PM CEST 
//


package sgae.util.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="titulo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="personasRef" type="{http://www.ptpd.tel.uva.es/ns/sgaerest/util/link}link"/>
 *         &lt;element name="gruposMusicalesRef" type="{http://www.ptpd.tel.uva.es/ns/sgaerest/util/link}link"/>
 *         &lt;element name="albumesRef" type="{http://www.ptpd.tel.uva.es/ns/sgaerest/util/link}link"/>
 *         &lt;element name="pistasRef" type="{http://www.ptpd.tel.uva.es/ns/sgaerest/util/link}link"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "titulo",
    "personasRef",
    "gruposMusicalesRef",
    "albumesRef",
    "pistasRef"
})
@XmlRootElement(name = "Raiz")
public class Raiz {

    @XmlElement(required = true)
    protected String titulo;
    @XmlElement(required = true)
    protected Link personasRef;
    @XmlElement(required = true)
    protected Link gruposMusicalesRef;
    @XmlElement(required = true)
    protected Link albumesRef;
    @XmlElement(required = true)
    protected Link pistasRef;

    /**
     * Obtiene el valor de la propiedad titulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define el valor de la propiedad titulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
    }

    /**
     * Obtiene el valor de la propiedad personasRef.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getPersonasRef() {
        return personasRef;
    }

    /**
     * Define el valor de la propiedad personasRef.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setPersonasRef(Link value) {
        this.personasRef = value;
    }

    /**
     * Obtiene el valor de la propiedad gruposMusicalesRef.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getGruposMusicalesRef() {
        return gruposMusicalesRef;
    }

    /**
     * Define el valor de la propiedad gruposMusicalesRef.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setGruposMusicalesRef(Link value) {
        this.gruposMusicalesRef = value;
    }

    /**
     * Obtiene el valor de la propiedad albumesRef.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getAlbumesRef() {
        return albumesRef;
    }

    /**
     * Define el valor de la propiedad albumesRef.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setAlbumesRef(Link value) {
        this.albumesRef = value;
    }

    /**
     * Obtiene el valor de la propiedad pistasRef.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getPistasRef() {
        return pistasRef;
    }

    /**
     * Define el valor de la propiedad pistasRef.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setPistasRef(Link value) {
        this.pistasRef = value;
    }

}
