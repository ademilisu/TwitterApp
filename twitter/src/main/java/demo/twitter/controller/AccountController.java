package demo.twitter.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import demo.twitter.dto.AccountDto;
import demo.twitter.entity.Account;
import demo.twitter.service.AccountService;
import demo.twitter.service.UserService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@GetMapping("/principal")
	public Account getCurrentAccount(Principal principal) {
		return userService.getAccount(principal);
	}

	@GetMapping("/{id}")
	public AccountDto getSomeone(@PathVariable String id, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? accountService.get(account, id) : null;
	}

	@PutMapping("/image")
	public Account updateProfile(@RequestBody MultipartFile file, @RequestParam("defaultImage") String defaultImage,
			Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? accountService.save(account, file, defaultImage) : null;
	}

	@GetMapping("/search")
	public Set<AccountDto> search(@RequestParam("name") String name, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? accountService.search(name, account) : null;
	}
}
