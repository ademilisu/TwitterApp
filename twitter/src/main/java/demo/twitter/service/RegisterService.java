package demo.twitter.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import demo.twitter.dto.AccountDto;
import demo.twitter.dto.LoginRequest;
import demo.twitter.entity.User;

@Service
public class RegisterService {

	private MyUserDetailService myUserDetailService;
	private UserService userService;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authManager;

	public RegisterService(MyUserDetailService myUserDetailService, UserService userService,
			PasswordEncoder passwordEncoder, AuthenticationManager authManager) {
		this.myUserDetailService = myUserDetailService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.authManager = authManager;
	}

	public AccountDto login(LoginRequest loginRequest) {
		UserDetails usr = myUserDetailService.loadUserByUsername(loginRequest.getUsername());
		AccountDto ac = new AccountDto();
		if (usr != null) {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					loginRequest.getUsername(), loginRequest.getPassword());
			Authentication authentication = authManager.authenticate(token);
			if (authentication.getPrincipal() != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				User user = userService.findByUsername(loginRequest.getUsername());
				ac.setCode(10);
				ac.setAccount(user.getAccount());
			}
		}
		return ac;
	}

	public AccountDto register(LoginRequest loginRequest) {
		AccountDto ac = new AccountDto();
		User user = userService.findByUsername(loginRequest.getUsername());
		if (user == null) {
			user = new User();
			user.setUsername(loginRequest.getUsername());
			user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
			user = userService.save(user);
			ac.setCode(10);
		}
		return ac;
	}

}
