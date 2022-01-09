package com.security.lab6.user.entity;

import com.security.lab6.config.AttributeEncryptor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Accessors(chain = true)
public class User {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(unique = true, updatable = false, nullable = false)
	private String username;

	private String password;

	@Column(columnDefinition = "bytea")
	@Convert(converter = AttributeEncryptor.class)
	private String phone;

}
