package demo.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.twitter.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);

}
