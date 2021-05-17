package com.example.meetontest;

import com.example.meetontest.rating.recommendation.MeetingRecommendationsService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.services.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class MeetontestApplicationTests {
    @Autowired
    private MeetingRecommendationsService meetingRecommendationsService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserService userService;



    @Test
    void contextLoads() {
    }

    @Test
    void getRecommendations() {
//        List<Meeting> meetings = meetingRecommendationsService.getRecommendations(meetingService.getMeetingsByTags(new ArrayList<String>()), userService.getUserById(1L), 5);
//        return;
    }
    @Test
    void getBytesFromImage() throws IOException {
    }
    @Test
    void saveImageFromDb() throws IOException {
//        byte[] byteFile = userService.getUserAvatar(1L);
//        FileOutputStream fos = new FileOutputStream("C:\\Users\\kiril\\OneDrive\\java_final_new\\MeetOn\\MeetOnBackend\\image\\output.jpg");
//        fos.write(byteFile);
//        fos.close();
    }
}
