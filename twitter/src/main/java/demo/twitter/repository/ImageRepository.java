package demo.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.twitter.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}
