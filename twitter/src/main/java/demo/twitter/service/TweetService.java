package demo.twitter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import demo.twitter.dto.TweetDto;
import demo.twitter.entity.Account;
import demo.twitter.entity.Image;
import demo.twitter.entity.Relation;
import demo.twitter.entity.Tweet;
import demo.twitter.entity.Type;
import demo.twitter.repository.TweetRepository;

@Service
public class TweetService {

	@Autowired
	private TweetRepository tweetRepo;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ImageService imageService;

	public Tweet findTweet(String id) {
		return tweetRepo.findById(id).orElse(null);
	}

	public TweetDto getTweet(Account account, String id) {
		TweetDto t = new TweetDto();
		Tweet target = findTweet(id);
		if (target != null) {
			t = checkLiking(t, target, account);
			t = checkRetweeting(t, target, account);
			t.setTweet(target);
			t.setCode(10);
		}
		return t;
	}

	public Tweet save(Account account, Tweet tweet) {
		tweet.setId(UUID.randomUUID().toString());
		tweet.setAccount(account);
		tweet.setCreated(new Date());
		return tweetRepo.save(tweet);
	}

	public Tweet update(Tweet tweet) {
		return tweetRepo.save(tweet);
	}

	public Set<TweetDto> list(Account account, String id, String type, PageRequest pageRequest) {
		Set<Tweet> tweets = null;
		Set<TweetDto> result = new HashSet<>();
		if (type.equals("profile")) {
			if (id.equals("me")) {
				tweets = tweetRepo.findAllByAccountOrderByCreatedDesc(account, pageRequest);
			} else {
				Account target = accountService.findById(id);
				if (target != null) {
					tweets = tweetRepo.findAllByAccountOrderByCreatedDesc(target, pageRequest);
				}
			}
		}
		if (type.equals("home")) {
			Set<Relation> relations = account.getFollows();
			List<Account> accounts = new ArrayList<>();
			if (relations != null) {
				for (Relation temp : relations) {
					accounts.add(temp.getFollowed());
				}
			}
			accounts.add(account);
			tweets = tweetRepo.findHomeTweets(accounts, pageRequest);
		}
		if (tweets != null) {
			for (Tweet temp : tweets) {
				if (!temp.getType().equals(Type.DELETED)) {
					TweetDto t = new TweetDto();
					t.setTweet(temp);
					t = checkLiking(t, temp, account);
					t = checkRetweeting(t, temp, account);
					result.add(t);
				} else {
					if (tweetRepo.findAllByTarget(temp).size() == 0) {
						delete(temp.getId());
					}
				}
			}
		}
		return result;
	}

	public Set<TweetDto> getComments(Account account, String id) {
		Set<TweetDto> result = null;
		Tweet target = findTweet(id);
		if (target != null) {
			result = new HashSet<>();
			Set<Tweet> list = tweetRepo.findAllByTargetAndType(target, Type.COMMENT);
			for (Tweet temp : list) {
				TweetDto t = new TweetDto();
				t = checkLiking(t, temp, account);
				t = checkRetweeting(t, temp, account);
				t.setTweet(temp);
				result.add(t);
			}
		}
		return result;
	}

	public TweetDto newTweet(Account account, TweetDto tweetDto) {
		Tweet tweet = tweetDto.getTweet();
		tweet.setType(Type.TWEET);
		tweet = save(account, tweet);
		tweetDto.setTweet(tweet);
		return tweetDto;
	}

	public TweetDto newRetweet(Account account, String id) {
		TweetDto t = new TweetDto();
		t.setCode(10);
		Tweet tweet = new Tweet();
		Tweet target = findTweet(id);
		if (target != null) {
			if (target.hasRetweet(account)) {
				t.setCode(5);
				target.setRetweetNo(target.getRetweetNo() - 1);
				tweet = tweetRepo.findByAccountAndTargetAndType(account, target, Type.RETWEET);
				delete(tweet.getId());
				account.undoRetweet(target);
				t.setTweet(tweet);

			} else {
				target.setRetweetNo(target.getRetweetNo() + 1);
				account.retweet(target);
				tweet.setTarget(target);
				tweet.setType(Type.RETWEET);
				tweet = save(account, tweet);
				t.setRetweeted(true);
				t.setTweet(tweet);
			}
			target = update(target);
			accountService.update(account);
		}
		return t;
	}

	public TweetDto newCommentAndQuote(Account account, TweetDto tweetDto, String type) {
		Tweet tweet = tweetDto.getTweet();
		Tweet target = tweet.getTarget();
		if (type.equals("comment")) {
			tweet.setType(Type.COMMENT);
			target.setCommentNo(target.getCommentNo() + 1);
		} else {
			tweet.setType(Type.QUOTE);
			target.setRetweetNo(target.getRetweetNo() + 1);
		}
		target = update(target);
		tweet.setTarget(target);
		tweet = save(account, tweet);
		tweetDto.setTweet(tweet);
		return tweetDto;
	}

	public TweetDto like(Account account, String id) {
		TweetDto t = new TweetDto();
		t.setCode(10);
		Tweet tweet = findTweet(id);
		if (tweet != null) {
			if (tweet.getType().equals(Type.RETWEET)) {
				tweet = tweet.getTarget();
			}
			if (tweet.hasLike(account)) {
				t.setCode(5);
				tweet.setLikeNo(tweet.getLikeNo() - 1);
				account.undoLike(tweet);
			} else {
				tweet.setLikeNo(tweet.getLikeNo() + 1);
				account.like(tweet);
			}
			update(tweet);
			accountService.update(account);
		}
		return t;
	}

	public Image uploadTweetImage(MultipartFile file, String tweetId) {
		Tweet tweet = findTweet(tweetId);
		Image image = null;
		if (tweet != null && tweet.getImage() != null) {
			image = tweet.getImage();
		} else {
			image = new Image();
			image.setTweet(tweet);
		}
		if (file != null) {
			image = imageService.buildImage(tweetId, image, file);
			tweet.setImage(image);
			update(tweet);
		}
		return image;
	}

	public void deleteTweet(String id) {
		Tweet tweet = findTweet(id);
		if (tweet != null) {
			if (tweet.getTarget() != null) {
				Tweet target = tweet.getTarget();
				if (tweet.getType().equals(Type.RETWEET) || tweet.getType().equals(Type.QUOTE)) {
					target.setRetweetNo(target.getRetweetNo() - 1);
				}
				if (tweet.getType().equals(Type.COMMENT)) {
					target.setCommentNo(target.getCommentNo() - 1);
				}
				update(target);
			}
			Set<Tweet> tweets = tweetRepo.findAllByTarget(tweet);
			if (tweets != null) {
				Tweet t = new Tweet();
				t.setContent("Tweet not available");
				t.setId(UUID.randomUUID().toString());
				t.setAccount(tweet.getAccount());
				t.setType(Type.DELETED);
				t = update(t);
				for (Tweet temp : tweets) {
					temp.setTarget(t);
					update(temp);
				}
			}
			if (tweet.getImage() != null) {
				imageService.delete(tweet.getImage());
			}
			delete(id);
		}
	}

	public void delete(String id) {
		tweetRepo.deleteById(id);
	}

	private TweetDto checkLiking(TweetDto tweetDto, Tweet target, Account account) {
		if (target.hasLike(account)) {
			tweetDto.setLiked(true);
		}
		return tweetDto;
	}

	private TweetDto checkRetweeting(TweetDto tweetDto, Tweet target, Account account) {
		if (target.hasRetweet(account)) {
			tweetDto.setRetweeted(true);
		}
		return tweetDto;
	}

}
