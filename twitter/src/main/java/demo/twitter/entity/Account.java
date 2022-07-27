package demo.twitter.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "account")
public class Account {

	@Id
	private String id;
	private String name;
	@Column(name = "follow")
	private int followNo;
	@Column(name = "follower")
	private int followerNo;

	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Image image;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@JsonIgnore
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private Set<Tweet> tweets;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "likes", joinColumns = @JoinColumn(name = "liker_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private Set<Tweet> likes;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "retweet", joinColumns = @JoinColumn(name = "retweeter_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private Set<Tweet> retweets;

	@JsonIgnore
	@OneToMany(mappedBy = "follower")
	private Set<Relation> follows;

	public void like(Tweet tweet) {
		if (likes == null) {
			likes = new HashSet<>();
		}
		likes.add(tweet);
	}

	public void undoLike(Tweet tweet) {
		if (likes != null) {
			likes.remove(tweet);
		}
	}

	public void retweet(Tweet tweet) {
		if (retweets == null) {
			retweets = new HashSet<>();
		}
		retweets.add(tweet);
	}

	public void undoRetweet(Tweet tweet) {
		if (retweets != null) {
			retweets.remove(tweet);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(Set<Tweet> tweets) {
		this.tweets = tweets;
	}

	public Set<Tweet> getLikes() {
		return likes;
	}

	public void setLikes(Set<Tweet> likes) {
		this.likes = likes;
	}

	public Set<Tweet> getRetweets() {
		return retweets;
	}

	public void setRetweets(Set<Tweet> retweets) {
		this.retweets = retweets;
	}

	public int getFollowNo() {
		return followNo;
	}

	public void setFollowNo(int followNo) {
		this.followNo = followNo;
	}

	public int getFollowerNo() {
		return followerNo;
	}

	public void setFollowerNo(int followerNo) {
		this.followerNo = followerNo;
	}

	public Set<Relation> getFollows() {
		return follows;
	}

	public void setFollows(Set<Relation> follows) {
		this.follows = follows;
	}

}
