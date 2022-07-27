package demo.twitter.dto;

public class RelationDto {

	private int code;
	private AccountDto follower;
	private AccountDto followed;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public AccountDto getFollower() {
		return follower;
	}

	public void setFollower(AccountDto follower) {
		this.follower = follower;
	}

	public AccountDto getFollowed() {
		return followed;
	}

	public void setFollowed(AccountDto followed) {
		this.followed = followed;
	}

}
