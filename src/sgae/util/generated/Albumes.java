//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2018.04.30 a las 02:38:14 PM CEST 
//


package sgae.util.generated;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://www.ptpd.tel.uva.es/ns/sgaerest/util/esquemaRepresentacionesRecursos}AlbumInfoBreve" maxOccurs="unbounded" minOccurs="0"/>
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
    "albumInfoBreve"
})
@XmlRootElement(name = "Albumes")
public class Albumes {

    @XmlElement(name = "AlbumInfoBreve", namespace = "http://www.ptpd.tel.uva.es/ns/sgaerest/util/esquemaRepresentacionesRecursos")
    protected List<AlbumInfoBreve> albumInfoBreve;

    /**
     * Gets the value of the albumInfoBreve property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the albumInfoBreve property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlbumInfoBreve().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AlbumInfoBreve }
     * 
     * 
     */
    public List<AlbumInfoBreve> getAlbumInfoBreve() {
        if (albumInfoBreve == null) {
            albumInfoBreve = new ArrayList<AlbumInfoBreve>();
        }
        return this.albumInfoBreve;
    }

}
