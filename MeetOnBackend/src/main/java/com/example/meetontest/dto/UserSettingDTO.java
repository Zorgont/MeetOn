package com.example.meetontest.dto;

import com.example.meetontest.entities.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter @Getter @NoArgsConstructor
public class UserSettingDTO {
    private String firstName;
    private String secondName;
    private String about;
}

