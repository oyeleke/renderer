package com.example.oyeleke.renderer.Models;

import java.io.Serializable;

/**
 * Created by oyeleke on 2/25/18.
 */

public class Posts implements Serializable {

    private String post_body;
    private String isPicture;

    public Posts() {
    }

    public Posts(String post_body, String isPicture) {

        this.post_body = post_body;
        this.isPicture = isPicture;
    }


    public String getPost_body() {
        return post_body;
    }

    public String getIsPicture() {
        return isPicture;
    }

}
