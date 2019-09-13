/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author junnguyen
 */
public class JSONUtils {

    public static <T> String marshal(Object source, Class<T> configurationClass) throws JAXBException {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            //jsonInString = mapper.writeValueAsString(source);
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(source);
           
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JSONUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonInString;
    }
    
       public static <T> T unmarshal(String ObjXml, Class<T> configurationClass) throws JAXBException {

        ObjectMapper mapper = new ObjectMapper();

        T obj = null;
        try {
            obj = (T) mapper.readValue(ObjXml, configurationClass);
        } catch (IOException ex) {
            Logger.getLogger(JSONUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return obj;
    }


}
