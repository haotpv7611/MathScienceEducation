package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.models.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private long id;
	private String fullName;
	private String username;
	private int gradeId;
	@JsonIgnore
	private String password;

	private List<GrantedAuthority> authorities;

	public UserPrinciple(long id, String fullName, int gradeId, String username, String password, List<GrantedAuthority> authorities) {
		this.id = id;
		this.fullName = fullName;
		this.gradeId = gradeId;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserPrinciple build(Account account) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("admin"));
		authorities.add(new SimpleGrantedAuthority("staff"));
		authorities.add(new SimpleGrantedAuthority("student"));

		String role = account.getRole().getDescription();
		if (role.equalsIgnoreCase("student")) {

			return new UserPrinciple(account.getId(), account.getFullName(),
					account.getStudentProfile().getClasses().getSchoolGrade().getGrade().getId(), account.getUsername(), account.getPassword(),
					authorities);
		} else {
			return new UserPrinciple(account.getId(), account.getFullName(), 0, account.getUsername(), account.getPassword(), authorities);
		}
	}

	/**
	 * @return the gradeId
	 */
	public int getGradeId() {
		return gradeId;
	}

	public long getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
