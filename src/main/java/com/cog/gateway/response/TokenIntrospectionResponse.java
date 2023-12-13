package com.cog.gateway.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenIntrospectionResponse {

    List<String> aud = new ArrayList<>();
    List<String> scope = new ArrayList<>();
    List<String> authorities = new ArrayList<>();
    private float userId;
    private String userName;
    private boolean active;
    private float exp;
    private String authorize;
    private String email;


    @JsonIgnore
    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "TokenIntrospectionResponse{" +
                "aud=" + aud +
                ", scope=" + scope +
                ", authorities=" + authorities +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", active=" + active +
                ", exp=" + exp +
                ", authorize='" + authorize + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

