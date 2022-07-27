package demo.twitter.dto;

import demo.twitter.entity.Account;

public class AccountDto {

	private int code;
	private Account account;
	private boolean follow;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean isFollow() {
		return follow;
	}

	public void setFollow(boolean follow) {
		this.follow = follow;
	}

}
