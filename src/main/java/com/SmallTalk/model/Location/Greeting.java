package com.SmallTalk.model.Location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by mrrobot on 7/5/17.
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId () {
        return id;
    }

    public String getContent() {
        return content;
    }

}
