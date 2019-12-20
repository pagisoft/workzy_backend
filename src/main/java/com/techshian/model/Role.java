package com.techshian.model;

import javax.persistence.*;

import org.hibernate.annotations.NaturalId;


@Entity
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
	@NaturalId
	@Column(name="role_name")
    private RoleName name;

    @Column
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
