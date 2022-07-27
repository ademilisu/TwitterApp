package demo.twitter.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "relation")
public class Relation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	@JoinColumn(name = "follower_id")
	private Account follower;

	@OneToOne
	@JoinColumn(name = "followed_id")
	private Account followed;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Account getFollower() {
		return follower;
	}

	public void setFollower(Account follower) {
		this.follower = follower;
	}

	public Account getFollowed() {
		return followed;
	}

	public void setFollowed(Account followed) {
		this.followed = followed;
	}

	@Override
	public String toString() {
		return "Relation [id=" + id + ", follower=" + follower + ", followed=" + followed + "]";
	}

}
