package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.impl.ExtensionsBuilder;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.samlext.saml2mdattr.EntityAttributes;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.schema.XSString;
import org.springframework.security.saml.metadata.MetadataGenerator;

public class PnetMetadataGenerator extends MetadataGenerator {

	@Override
	public EntityDescriptor generateMetadata() {
		EntityDescriptor descriptor = super.generateMetadata();

		appendExtensions(descriptor);

		return descriptor;
	}

	private void appendExtensions(EntityDescriptor descriptor) {
		Extensions extensions = new ExtensionsBuilder().buildObject();

		EntityAttributes entityAttributes = createSamlObject(EntityAttributes.DEFAULT_ELEMENT_NAME);
		entityAttributes.getAttributes()
				.add(entityAttribute("urn:oasis:names:tc:SAML:profiles:subject-id:req", "subject-id"));

		extensions.getUnknownXMLObjects().add(entityAttributes);

		descriptor.setExtensions(extensions);
	}

	private Attribute entityAttribute(String attributeName, String attributeValue) {
		Attribute attribute = createSamlObject(Attribute.DEFAULT_ELEMENT_NAME);
		attribute.setName(attributeName);
		attribute.setNameFormat(Attribute.URI_REFERENCE);

		XSString value = createXMLObject(XSString.TYPE_NAME, AttributeValue.DEFAULT_ELEMENT_NAME);
		value.setValue(attributeValue);
		;

		attribute.getAttributeValues().add(value);

		return attribute;
	}

	@SuppressWarnings("unchecked")
	private <T extends SAMLObject> T createSamlObject(QName defaultName) {
		SAMLObjectBuilder<T> builder = (SAMLObjectBuilder<T>) builderFactory.getBuilder(defaultName);

		return builder.buildObject();
	}

	@SuppressWarnings("unchecked")
	private <T extends XMLObject> T createXMLObject(QName typeName, QName defaultName) {
		XMLObjectBuilder<?> builder = builderFactory.getBuilder(typeName);

		return (T) builder.buildObject(defaultName, typeName);
	}
}
