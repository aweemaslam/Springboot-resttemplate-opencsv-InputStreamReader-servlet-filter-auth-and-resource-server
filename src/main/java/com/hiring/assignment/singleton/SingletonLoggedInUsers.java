package com.hiring.assignment.singleton;

import com.hiring.assignment.oauth2.service.UserEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Scope(value = "singleton")
public class SingletonLoggedInUsers {
    private Map<String, UserEntity> userCredentials = null;
    private static volatile SingletonLoggedInUsers loggedInUsers;

    private SingletonLoggedInUsers() {
        setUserCredentials(new LinkedHashMap<>());
    }

    public static SingletonLoggedInUsers getInstance() {
        if (loggedInUsers == null) {
            loggedInUsers = new SingletonLoggedInUsers();
        }

        return loggedInUsers;
    }

    public Map<String, UserEntity> getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(Map<String, UserEntity> userCredentials) {
        this.userCredentials = userCredentials;
    }
}
