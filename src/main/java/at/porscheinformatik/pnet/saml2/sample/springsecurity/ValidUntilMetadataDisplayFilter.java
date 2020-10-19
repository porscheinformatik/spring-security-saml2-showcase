package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import org.joda.time.DateTime;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLObjectHelper;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;

public class ValidUntilMetadataDisplayFilter extends MetadataDisplayFilter {

	@Override
	protected String getMetadataAsString(EntityDescriptor descriptor) throws MarshallingException {
		EntityDescriptor clonedDescriptor;
		try {
			clonedDescriptor = XMLObjectHelper.cloneXMLObject(descriptor);
			clonedDescriptor.setValidUntil(new DateTime().plusDays(6));

			return super.getMetadataAsString(clonedDescriptor);
		} catch (UnmarshallingException e) {
			throw new MarshallingException("Error setting valid until for metadata", e);
		}
	}

}
