package com.ame.rest.user;

import javax.persistence.Entity;

import com.ame.rest.user.developer.Developer;
import com.ame.rest.user.writer.Writer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.AccessLevel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import java.util.regex.Pattern;


@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @Type(value = Writer.class, name = "writer"),
  @Type(value = Developer.class, name = "developer")
})


@Entity
@NoArgsConstructor @Getter @Setter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="DISCRIMINATOR")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name="email", unique=true)
    private String  email;

    @NonNull
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String password;

    private String[] roles;
    private boolean verified;


    public User(String email, String password) {
        this.email = email;
        verified = false;
        this.password = password;
    }

    @JsonProperty
    public void setPassword(String password) {
		this.password = password;
	}

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    abstract public String[] getDefaultRoles();
}
