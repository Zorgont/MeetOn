package com.example.meetontest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class RequestDTO {
    private Long id;
    private String about;
    private Long meeting_id;
    private Long user_id;
    private String status;
}
