package demo.twitter.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import demo.twitter.dto.AccountDto;
import demo.twitter.dto.LoginRequest;
import demo.twitter.service.RegisterService;

@RestController
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	private RegisterService registerService;

	@PostMapping("/login")
	public AccountDto login(@RequestBody LoginRequest loginRequest) {
		return registerService.login(loginRequest);
	}

	@PostMapping("/signup")
	public AccountDto register(@RequestBody LoginRequest loginRequest) {
		return registerService.register(loginRequest);
	}

}
