package at.porscheinformatik.pnet.saml2.sample.springsecurity;

public class UserData {

	private final String externalId;

	public UserData(String externalId) {
		super();

		this.externalId = externalId;
	}

	public String getExternalId() {
		return externalId;
	}

}
