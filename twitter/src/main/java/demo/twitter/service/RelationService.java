package demo.twitter.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.twitter.dto.AccountDto;
import demo.twitter.dto.RelationDto;
import demo.twitter.entity.Account;
import demo.twitter.entity.Relation;
import demo.twitter.repository.RelationRepository;

@Service
public class RelationService {

	@Autowired
	private RelationRepository relationRepo;

	@Autowired
	private AccountService accountService;

	public Relation findById(int id) {
		return relationRepo.findById(id).orElse(null);
	}

	public Relation hasRelationWith(Account account, Account target) {
		Relation relation = relationRepo.findByFollowerAndFollowed(account, target);
		return relation != null ? relation : null;
	}

	public Set<RelationDto> getRelations(String id, Account account, String type) {
		Set<Relation> relations = null;
		Account target = account;
		if (!id.equals("me")) {
			target = accountService.findById(id);
		}
		if (type.equals("follows")) {
			relations = relationRepo.findAllByFollower(target);
		}
		if (type.equals("followers")) {
			relations = relationRepo.findAllByFollowed(target);
		}
		return relations != null ? getRelationDtos(relations, account, type) : null;
	}

	public Relation follow(Account account, String targetId) {
		Account target = accountService.findById(targetId);
		Relation relation = hasRelationWith(account, target);
		if (target != null && relation == null) {
			account.setFollowNo(account.getFollowNo() + 1);
			target.setFollowerNo(target.getFollowerNo() + 1);
			relation = new Relation();
			relation.setFollower(account);
			relation.setFollowed(target);
			relation = relationRepo.save(relation);
			accountService.update(account);
			accountService.update(target);
		}
		return relation;
	}

	public void unfollow(Account account, String id, String type) {
		Account target = accountService.findById(id);
		if (target != null) {
			Relation relation = null;
			if (type.equals("following")) {
				account.setFollowNo(account.getFollowNo() - 1);
				target.setFollowerNo(target.getFollowerNo() - 1);
				relation = relationRepo.findByFollowerAndFollowed(account, target);
			}
			if (type.equals("followers")) {
				target.setFollowNo(target.getFollowNo() - 1);
				account.setFollowerNo(account.getFollowerNo() - 1);
				relation = relationRepo.findByFollowerAndFollowed(target, account);
			}
			accountService.update(account);
			accountService.update(target);
			if (relation != null) {
				relationRepo.delete(relation);
			}
		}
	}

	private Set<RelationDto> getRelationDtos(Set<Relation> relations, Account account, String type) {
		Set<RelationDto> result = new HashSet<>();
		for (Relation temp : relations) {
			RelationDto r = new RelationDto();
			AccountDto follower = new AccountDto();
			AccountDto followed = new AccountDto();
			follower.setAccount(temp.getFollower());
			followed.setAccount(temp.getFollowed());
			if (type.equals("following")) {
				if (hasRelationWith(account, temp.getFollowed()) != null) {
					followed.setFollow(true);
				}
			}
			if (type.equals("followers")) {
				if (hasRelationWith(account, temp.getFollower()) != null) {
					follower.setFollow(true);
				}
			}
			r.setFollower(follower);
			r.setFollowed(followed);
			result.add(r);
		}
		return result;
	}

}
