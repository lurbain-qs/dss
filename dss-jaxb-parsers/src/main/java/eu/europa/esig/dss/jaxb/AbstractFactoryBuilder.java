package eu.europa.esig.dss.jaxb;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import eu.europa.esig.dss.alert.Alert;
import eu.europa.esig.dss.alert.DSSLogAlert;
import eu.europa.esig.dss.jaxb.exception.XmlSecurityException;

public abstract class AbstractFactoryBuilder<F extends Object> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractFactoryBuilder.class);
	
	private Map<String, Boolean> features = new HashMap<>();
	private Map<String, Object> attributes = new HashMap<>();
	
	private Alert<Exception> securityExceptionAlert = new DSSLogAlert(Level.WARN, LOG.isDebugEnabled());

	/**
	 * This method allows to configure a custom alert on security exception in the builder
	 * 
	 * @param securityExceptionAlert {@link Alert} to define
	 */
	public void setSecurityExceptionAlert(Alert<Exception> securityExceptionAlert) {
		this.securityExceptionAlert = securityExceptionAlert;
	}
	
	/**
	 * Enables a custom feature
	 * 
	 * @param feature {@link String} the feature constraint
	 * @return this builder
	 */
	public AbstractFactoryBuilder<F> enableFeature(String feature) {
		setFeature(feature, true);
		return this;
	}

	/**
	 * Disables a custom feature
	 * 
	 * @param feature {@link String} the feature constraint
	 * @return this builder
	 */
	public AbstractFactoryBuilder<F> disableFeature(String feature) {
		setFeature(feature, false);
		return this;
	}
	
	private void setFeature(String feature, boolean value) {
		Objects.requireNonNull(feature, "The feature constraint cannot be null!");
		if (features.containsKey(feature) && features.get(feature) != value) {
			LOG.warn("SECURITY : feature with the name [{}] changed from [{}] to [{}]", feature, features.get(feature), value);
		} else if (LOG.isDebugEnabled()) {
			LOG.debug("The feature {} = {} has been added to the configuration", feature, value);
		}
		features.put(feature, value);
	}
	
	/**
	 * Sets a custom attribute.
	 * 
	 * @param attribute {@link String} attribute constraint to set
	 * @param value {@link Object} a value to define for the attribute
	 * @return this builder
	 */
	public AbstractFactoryBuilder<F> setAttribute(String attribute, Object value) {
		Objects.requireNonNull(attribute, "The attribute constraint cannot be null!");
		if (attributes.containsKey(attribute) && attributes.get(attribute).equals(value)) {
			LOG.warn("SECURITY : attribute with the name [{}] changed from [{}] to [{}]", attribute, attributes.get(attribute), value);
		} else if (LOG.isDebugEnabled()) {
			LOG.debug("The attribute {} = {} has been added to the configuration", attribute, value);
		}
		attributes.put(attribute, value);
		return this;
	}
	
	/**
	 * Removes the attribute from a list of attributes to set
	 * 
	 * @param attribute {@link String} attribute to disable
	 * @return this builder
	 */
	public AbstractFactoryBuilder<F> removeAttribute(String attribute) {
		Objects.requireNonNull(attribute, "The attribute constraint cannot be null!");
		if (attributes.containsKey(attribute)) {
			attributes.remove(attribute);
			LOG.warn("SECURITY : the attribute with name [{}] has been disabled", attribute);
		}
		return this;
	}
	
	/**
	 * Sets all features to the factory
	 */
	protected void setSecurityFeatures(F factory) {
		for (Map.Entry<String, Boolean> entry : features.entrySet()) {
			try {
				setSecurityFeature(factory, entry.getKey(), entry.getValue());
			} catch (Exception e) {
				String message = String.format("SECURITY : unable to set feature '%s' = '%s'. Cause : %s", 
						entry.getKey(), entry.getValue(), e.getMessage());
				securityExceptionAlert.alert(new XmlSecurityException(message, e));
			}
		}
	}
	
	/**
	 * Sets the feature to the factory
	 * 
	 * @param factory to set the feature to
	 * @param feature {@link String} feature constraint to set
	 * @param value {@link Boolean} value of the feature to add
	 * @throws Exception in case if any exception occurs
	 */
	protected abstract void setSecurityFeature(F factory, String feature, Boolean value) throws Exception;

	/**
	 * Sets all attributes to the factory
	 */
	protected void setSecurityAttributes(F factory) {
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			try {
				setSecurityAttribute(factory, entry.getKey(), entry.getValue());
			} catch (Exception e) {
				String message = String.format("SECURITY : unable to set attribute '%s' = '%s'. Cause : %s", 
						entry.getKey(), entry.getValue(), e.getMessage());
				securityExceptionAlert.alert(new XmlSecurityException(message, e));
			}
		}
	}
	
	/**
	 * Sets the attribute to the factory
	 * 
	 * @param factory {@code Factory} to set the attribute to
	 * @param attribute {@link String} attribute constraint to set
	 * @param value {@link Object} value of the attribute to add
	 * @throws Exception in case if any exception occurs
	 */
	protected abstract void setSecurityAttribute(F factory, String attribute, Object value) throws Exception;

}
