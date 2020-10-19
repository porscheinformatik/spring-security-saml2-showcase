package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/user")
public class UserDataController {

	@GetMapping
	public UserData getUser() {
		return new UserData("Test");
	}

}
