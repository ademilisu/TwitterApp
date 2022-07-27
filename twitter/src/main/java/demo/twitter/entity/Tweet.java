package demo.twitter.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tweet")
public class Tweet {

	@Id
	private String id;
	private String content;

	@Column(name = "retweets")
	private int retweetNo;

	@Column(name = "comments")
	private int commentNo;

	@Column(name = "likes")
	private int likeNo;
	private Date created;
	private Type type;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

	@JsonIgnore
	@ManyToMany()
	@JoinTable(name = "likes", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "liker_id"))
	private Set<Account> likes;

	@JsonIgnore
	@ManyToMany()
	@JoinTable(name = "retweet", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "retweeter_id"))
	private Set<Account> retweeters;

	@OneToOne
	@JoinColumn(name = "target_id")
	private Tweet target;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "image_id")
	private Image image;

	public boolean hasLike(Account acnt) {
		if (likes != null) {
			return likes.contains(acnt);
		} else
			return false;
	}

	public boolean hasRetweet(Account acnt) {
		if (retweeters != null) {
			return retweeters.contains(acnt);
		} else
			return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRetweetNo() {
		return retweetNo;
	}

	public void setRetweetNo(int retweetNo) {
		this.retweetNo = retweetNo;
	}

	public int getCommentNo() {
		return commentNo;
	}

	public void setCommentNo(int commentNo) {
		this.commentNo = commentNo;
	}

	public int getLikeNo() {
		return likeNo;
	}

	public void setLikeNo(int likeNo) {
		this.likeNo = likeNo;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Set<Account> getLikes() {
		return likes;
	}

	public void setLikes(Set<Account> likes) {
		this.likes = likes;
	}

	public Set<Account> getRetweeters() {
		return retweeters;
	}

	public void setRetweeters(Set<Account> retweeters) {
		this.retweeters = retweeters;
	}

	public Tweet getTarget() {
		return target;
	}

	public void setTarget(Tweet target) {
		this.target = target;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Tweet [id=" + id + ", content=" + content + ", retweetNo=" + retweetNo + ", commentNo=" + commentNo
				+ ", likeNo=" + likeNo + "]";
	}

}
