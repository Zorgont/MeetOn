package com.example.meetontest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class RequestDTO {
    private Long id;
    private String about;
    private Long meeting_id;
    private Long user_id;
    private String status;
}