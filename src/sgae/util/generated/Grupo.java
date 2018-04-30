//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2018.04.30 a las 02:38:14 PM CEST 
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
 *         &lt;element name="CIF" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechacreacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ejemplaresvendidos" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uri1" type="{http://www.ptpd.tel.uva.es/ns/sgaerest/util/link}link"/>
 *         &lt;element name="uri2" type="{http://www.ptpd.tel.uva.es/ns/sgaerest/util/link}link"/>
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
    "cif",
    "nombre",
    "fechacreacion",
    "ejemplaresvendidos",
    "uri1",
    "uri2"
})
@XmlRootElement(name = "Grupo")
public class Grupo {

    @XmlElement(name = "CIF", required = true)
    protected String cif;
    @XmlElement(required = true)
    protected String nombre;
    @XmlElement(required = true)
    protected String fechacreacion;
    @XmlElement(required = true)
    protected String ejemplaresvendidos;
    @XmlElement(required = true)
    protected Link uri1;
    @XmlElement(required = true)
    protected Link uri2;

    /**
     * Obtiene el valor de la propiedad cif.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCIF() {
        return cif;
    }

    /**
     * Define el valor de la propiedad cif.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCIF(String value) {
        this.cif = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad fechacreacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechacreacion() {
        return fechacreacion;
    }

    /**
     * Define el valor de la propiedad fechacreacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechacreacion(String value) {
        this.fechacreacion = value;
    }

    /**
     * Obtiene el valor de la propiedad ejemplaresvendidos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEjemplaresvendidos() {
        return ejemplaresvendidos;
    }

    /**
     * Define el valor de la propiedad ejemplaresvendidos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEjemplaresvendidos(String value) {
        this.ejemplaresvendidos = value;
    }

    /**
     * Obtiene el valor de la propiedad uri1.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getUri1() {
        return uri1;
    }

    /**
     * Define el valor de la propiedad uri1.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setUri1(Link value) {
        this.uri1 = value;
    }

    /**
     * Obtiene el valor de la propiedad uri2.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getUri2() {
        return uri2;
    }

    /**
     * Define el valor de la propiedad uri2.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setUri2(Link value) {
        this.uri2 = value;
    }

}
