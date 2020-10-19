package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import org.joda.time.DateTime;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;

public class PnetMetadataDisplayFilter extends MetadataDisplayFilter {

	@Override
	protected String getMetadataAsString(EntityDescriptor descriptor) throws MarshallingException {
		descriptor.setValidUntil(new DateTime().plusDays(6));
		return super.getMetadataAsString(descriptor);
	}

}
