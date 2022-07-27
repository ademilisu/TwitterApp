package demo.twitter.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import demo.twitter.entity.Account;
import demo.twitter.entity.Tweet;
import demo.twitter.entity.Type;

public interface TweetRepository extends JpaRepository<Tweet, String> {

	Set<Tweet> findAllByAccountAndType(Account account, Type tweet);

	Set<Tweet> findAllByTargetAndType(Tweet target, Type type);

	Tweet findByAccountAndTargetAndType(Account account, Tweet target, Type retweet);

	@Query("Select t from Tweet t Where t.account in :accounts Order By t.created Desc")
	Set<Tweet> findHomeTweets(@Param("accounts") List<Account> accounts, PageRequest pageRequest);

	Set<Tweet> findAllByAccountOrderByCreatedDesc(Account account, PageRequest pageRequest);

	Set<Tweet> findAllByTarget(Tweet tweet);

}
