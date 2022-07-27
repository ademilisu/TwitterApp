package demo.twitter.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.twitter.entity.Account;
import demo.twitter.entity.Relation;

public interface RelationRepository extends JpaRepository<Relation, Integer> {

	Set<Relation> findAllByFollower(Account account);

	Set<Relation> findAllByFollowed(Account account);

	Relation findByFollowerAndFollowed(Account account, Account target);

}
