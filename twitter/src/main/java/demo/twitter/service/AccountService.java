package demo.twitter.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import demo.twitter.dto.AccountDto;
import demo.twitter.entity.Account;
import demo.twitter.entity.Image;
import demo.twitter.repository.AccountRepository;
import demo.twitter.repository.RelationRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private RelationRepository relationRepo;

	@Autowired
	private ImageService imageService;

	public Account findById(String id) {
		return accountRepo.findById(id).orElse(null);
	}

	public Account save(Account account, MultipartFile file, String img) {
		if (account.getId() == null) {
			account.setId(UUID.randomUUID().toString());
		}
		account = accountRepo.save(account);
		if (file != null || img.equals("default")) {
			Image image = imageService.save(account, file, img);
			account.setImage(image);
		}
		return account;
	}

	public Account update(Account account) {
		return accountRepo.save(account);
	}

	public Set<AccountDto> search(String name, Account account) {
		Set<AccountDto> result = new HashSet<>();
		Set<Account> accounts = accountRepo.findAllByNameStartsWith(name);
		if (accounts != null) {
			for (Account temp : accounts) {
				AccountDto ac = new AccountDto();
				ac.setAccount(temp);
				if (relationRepo.findByFollowerAndFollowed(account, temp) != null) {
					ac.setFollow(true);
				}
				result.add(ac);
			}
		}
		return result;
	}

	public AccountDto get(Account account, String id) {
		Account target = findById(id);
		AccountDto ac = new AccountDto();
		if (target != null) {
			ac.setAccount(target);
			if (relationRepo.findByFollowerAndFollowed(account, target) != null) {
				ac.setFollow(true);
			}
		}
		return ac;
	}

}
