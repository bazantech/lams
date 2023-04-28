/*
 * XML Type:  CT_MetadataStrings
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MetadataStrings(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTMetadataStringsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings {
    private static final long serialVersionUID = 1L;

    public CTMetadataStringsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "s"),
        new QName("", "count"),
    };


    /**
     * Gets a List of "s" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement> getSList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSArray,
                this::setSArray,
                this::insertNewS,
                this::removeS,
                this::sizeOfSArray
            );
        }
    }

    /**
     * Gets array of all "s" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement[] getSArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement[0]);
    }

    /**
     * Gets ith "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement getSArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "s" element
     */
    @Override
    public int sizeOfSArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "s" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement[] sArray) {
        check_orphaned();
        arraySetterHelper(sArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "s" element
     */
    @Override
    public void setSArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement s) {
        generatedSetterHelperImpl(s, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement insertNewS(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement addNewS() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "s" element
     */
    @Override
    public void removeS(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "count" attribute
     */
    @Override
    public long getCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "count" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "count" attribute
     */
    @Override
    public boolean isSetCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "count" attribute
     */
    @Override
    public void setCount(long count) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(count);
        }
    }

    /**
     * Sets (as xml) the "count" attribute
     */
    @Override
    public void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(count);
        }
    }

    /**
     * Unsets the "count" attribute
     */
    @Override
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
