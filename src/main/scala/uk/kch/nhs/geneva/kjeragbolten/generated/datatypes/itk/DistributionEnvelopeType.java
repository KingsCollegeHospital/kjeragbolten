
package uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for distributionEnvelopeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="distributionEnvelopeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{urn:nhs-itk:ns:201005}distributionHeaderType"/>
 *         &lt;element name="payloads" type="{urn:nhs-itk:ns:201005}payloadsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "distributionEnvelopeType", propOrder = {
    "header",
    "payloads"
})
public class DistributionEnvelopeType {

    @XmlElement(required = true)
    protected DistributionHeaderType header;
    @XmlElement(required = true)
    protected PayloadsType payloads;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link DistributionHeaderType }
     *     
     */
    public DistributionHeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link DistributionHeaderType }
     *     
     */
    public void setHeader(DistributionHeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the payloads property.
     * 
     * @return
     *     possible object is
     *     {@link PayloadsType }
     *     
     */
    public PayloadsType getPayloads() {
        return payloads;
    }

    /**
     * Sets the value of the payloads property.
     * 
     * @param value
     *     allowed object is
     *     {@link PayloadsType }
     *     
     */
    public void setPayloads(PayloadsType value) {
        this.payloads = value;
    }

}
