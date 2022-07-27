package demo.twitter.service;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import demo.twitter.entity.Account;
import demo.twitter.entity.User;
import demo.twitter.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AccountService accountService;

	public User findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public User findById(String id) {
		return userRepo.findById(id).orElse(null);
	}

	public User save(User user) {
		user.setId(UUID.randomUUID().toString());
		user = userRepo.save(user);
		Account account = new Account();
		account.setName(user.getUsername());
		account.setUser(user);
		account = accountService.save(account, null, "");
		return user;
	}

	public void delete(String id) {
		userRepo.deleteById(id);
	}

	public Account getAccount(Principal principal) {
		Account account = null;
		if (principal != null) {
			User user = findByUsername(principal.getName());
			if (user != null) {
				account = user.getAccount();
			}
		}
		return account;
	}
}
