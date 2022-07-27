package demo.twitter.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import demo.twitter.dto.TweetDto;
import demo.twitter.entity.Account;
import demo.twitter.entity.Image;
import demo.twitter.service.TweetService;
import demo.twitter.service.UserService;

@RestController
@RequestMapping("/tweets")
public class TweetController {

	@Autowired
	private TweetService tweetService;

	@Autowired
	private UserService userService;

	@GetMapping("/list/{id}")
	public Set<TweetDto> list(@PathVariable String id, @RequestParam("type") String type,
			@RequestParam("current") int page, @RequestParam("size") int size, Principal principal) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Account account = userService.getAccount(principal);
		return account != null ? tweetService.list(account, id, type, pageRequest) : null;
	}

	@GetMapping("/tweet/{id}")
	public TweetDto getTweet(@PathVariable String id, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? tweetService.getTweet(account, id) : null;
	}

	@GetMapping("/{id}/comments")
	public Set<TweetDto> getComments(@PathVariable String id, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? tweetService.getComments(account, id) : null;
	}

	@PostMapping("/tweet")
	public TweetDto newTweet(@RequestBody TweetDto tweetDto, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? tweetService.newTweet(account, tweetDto) : null;
	}

	@GetMapping("/retweet/{id}")
	public TweetDto newRetweet(@PathVariable String id, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? tweetService.newRetweet(account, id) : null;
	}

	@PostMapping("/comment")
	public TweetDto newComment(@RequestBody TweetDto tweetDto, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? tweetService.newCommentAndQuote(account, tweetDto, "comment") : null;
	}

	@PostMapping("/quote")
	public TweetDto newQuote(@RequestBody TweetDto tweetDto, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? tweetService.newCommentAndQuote(account, tweetDto, "quote") : null;
	}

	@GetMapping("/like/{id}")
	public TweetDto like(@PathVariable String id, Principal principal) {
		Account account = userService.getAccount(principal);
		return account != null ? tweetService.like(account, id) : null;
	}

	@PostMapping("/{tweetId}/image")
	public Image uploadTweetImage(@PathVariable String tweetId, @RequestBody MultipartFile file) {
		return tweetService.uploadTweetImage(file, tweetId);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		tweetService.deleteTweet(id);
	}

}
