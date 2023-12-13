package com.cog.gateway.config.auth;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


@Getter
@Slf4j
public class AuthConfig {

    private Map <String, String> authorities;


    public void setAuthorities(Map <String, String> authorities) {
        this.authorities = authorities;
    }

    public List <String> getAuthoritiesByUrl(String url) {
        log.info ("URL :: " +url);
        Optional <String> first = Optional.empty ();
        for (Map.Entry <String, String> entry : authorities.entrySet ()) {
            if (url.matches (entry.getKey ().replaceAll ("_\\.([^\\W]+)\\._|_\\.([^\\W]+)","(.*)"))) {
                String value = entry.getValue ();
                first = Optional.of (value);
                break;
            }
        }
        if (first.isPresent ()) {
            log.info (authorities.toString ());
            String s = first.get ();
            log.info ("LOG:: getAuthoritiesByUrl " + s);
            return new ArrayList<>(Arrays.asList(s.split(",")));
        }
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "Config{" + "authorities=" + authorities + '}';
    }
}

