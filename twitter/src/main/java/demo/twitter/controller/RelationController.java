package demo.twitter.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.twitter.dto.RelationDto;
import demo.twitter.entity.Account;
import demo.twitter.entity.Relation;
import demo.twitter.service.RelationService;
import demo.twitter.service.UserService;

@RestController
@RequestMapping("/relations")
public class RelationController {

	@Autowired
	private RelationService relationService;

	@Autowired
	private UserService userService;

	@GetMapping("/{id}/follows")
	public Set<RelationDto> getFollows(@PathVariable String id, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? relationService.getRelations(id, account, "follows") : null;
	}

	@GetMapping("/{id}/followers")
	public Set<RelationDto> getFollowers(@PathVariable String id, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? relationService.getRelations(id, account, "followers") : null;
	}

	@GetMapping("/follow/{id}")
	public Relation follow(@PathVariable String id, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? relationService.follow(account, id) : null;
	}

	@GetMapping("/unfollow/{id}")
	public void unfollow(@PathVariable String id, @RequestParam("type") String type, Principal principal) {
		Account account = userService.getAccount(principal);
		if (account != null) {
			relationService.unfollow(account, id, type);
		}
	}
}
