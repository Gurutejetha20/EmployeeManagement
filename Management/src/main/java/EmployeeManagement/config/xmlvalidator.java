package EmployeeManagement.config;

import EmployeeManagement.dto.Employeedto;
import EmployeeManagement.exception.AppException;
import org.springframework.stereotype.Component;
import jakarta.xml.bind.*;
import javax.xml.validation.*;
import javax.xml.XMLConstants;
import java.io.StringReader;
import org.springframework.core.io.ClassPathResource;
import javax.xml.transform.stream.StreamSource;

@Component
public class xmlvalidator {

    public void validate(String xml) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(
                    new StreamSource(new ClassPathResource("employee.xsd").getInputStream()));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
        } catch (Exception e) {
            throw AppException.invalidXml(e.getMessage());
        }
//        catch( NullPointerException e) {
//        	
//        }
    }

    public Employeedto convert(String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(Employeedto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Employeedto) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            throw AppException.xmlParseError(e.getMessage());
        }
    }
}