package demo.twitter.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.twitter.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

	Set<Account> findAllByNameStartsWith(String name);

}
