package com.example.hello.util;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * XML utility using javax.xml.bind (JAXB).
 * JAXB was removed from JDK in Java 11 (JEP 320).
 * Requires external dependency for Java 11+.
 */
public class XmlUtil {

    private static final Logger logger = Logger.getLogger(XmlUtil.class);

    /**
     * Simple user model for XML serialization.
     */
    @XmlRootElement(name = "user")
    public static class UserXml {
        private String name;
        private String email;
        private int age;

        public UserXml() {}

        public UserXml(String name, String email, int age) {
            this.name = name;
            this.email = email;
            this.age = age;
        }

        @XmlElement
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @XmlElement
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        @XmlElement
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    /**
     * Marshal object to XML string using JAXB (removed in Java 11).
     * Uses context classloader trick to access JDK-internal JAXB in embedded Tomcat.
     */
    public static String toXml(Object obj) throws JAXBException {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);

            String xml = writer.toString();
            logger.debug("Marshalled object to XML: " + xml.length() + " chars");
            return xml;
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    /**
     * Unmarshal XML string to object using JAXB (removed in Java 11).
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXml(String xml, Class<T> clazz) throws JAXBException {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader reader = new StringReader(xml);
            T obj = (T) unmarshaller.unmarshal(reader);
            logger.debug("Unmarshalled XML to " + clazz.getSimpleName());
            return obj;
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    /**
     * Convert hex string to bytes using JAXB DatatypeConverter (removed in Java 11).
     */
    public static byte[] hexToBytes(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }

    /**
     * Convert bytes to hex string using JAXB DatatypeConverter (removed in Java 11).
     */
    public static String bytesToHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
}
