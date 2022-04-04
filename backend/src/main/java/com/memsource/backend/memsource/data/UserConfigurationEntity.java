package com.memsource.backend.memsource.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class UserConfigurationEntity {

    @GeneratedValue
    @Id
    private Long id;

    private String userName;

    private String password;
}
