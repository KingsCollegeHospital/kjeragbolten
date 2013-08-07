
package uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for payloadsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="payloadsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="payload" type="{urn:nhs-itk:ns:201005}payloadType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payloadsType", propOrder = {
    "payload"
})
public class PayloadsType {

    @XmlElement(required = true)
    protected List<PayloadType> payload;
    @XmlAttribute(name = "count", required = true)
    protected BigInteger count;

    /**
     * Gets the value of the payload property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the payload property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPayload().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PayloadType }
     * 
     * 
     */
    public List<PayloadType> getPayload() {
        if (payload == null) {
            payload = new ArrayList<PayloadType>();
        }
        return this.payload;
    }

    /**
     * Gets the value of the count property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCount(BigInteger value) {
        this.count = value;
    }

}
