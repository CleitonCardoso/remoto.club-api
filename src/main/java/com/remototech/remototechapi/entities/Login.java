package com.remototech.remototechapi.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email") })
@DynamicUpdate
public class Login implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	private UUID uuid;

	@Column(nullable = false)
	@NotBlank(message = "Nome de usuário é obrigatório.")
	private String username;

	@Column(nullable = false)
	@NotBlank(message = "Email é obrigatório.")
	private String email;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false, length = 512)
	private String password;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = true, length = 512)
	private String linkedInId;

	@OneToOne(mappedBy = "login", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	private AppUser user;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "tenant_uuid", referencedColumnName = "uuid")
	private Tenant tenant;

	@OneToOne(mappedBy = "login")
	private Candidate candidate;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(updatable = false)
	private LocalDateTime createdDate;

	private LocalDateTime lastUpdate;

	@PrePersist
	public void prePersist() {
		createdDate = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		lastUpdate = LocalDateTime.now();
	}

	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
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

}
