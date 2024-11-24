package com.uny.unydatabaseredmine.auth.payload.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicRequest {

    private long user_id;


    private String topicName;

}
