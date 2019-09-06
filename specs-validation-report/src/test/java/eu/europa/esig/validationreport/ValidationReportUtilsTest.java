package eu.europa.esig.validationreport;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class ValidationReportUtilsTest {

	@Test
	public void getJAXBContext() throws JAXBException {
		assertNotNull(ValidationReportUtils.getJAXBContext());
		// cached
		assertNotNull(ValidationReportUtils.getJAXBContext());
	}

	@Test
	public void getSchema() throws SAXException {
		assertNotNull(ValidationReportUtils.getSchema());
		// cached
		assertNotNull(ValidationReportUtils.getSchema());
	}

}
