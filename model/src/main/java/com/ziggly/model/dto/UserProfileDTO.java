package com.ziggly.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String avatarURL;
    private String title;
    private String bio;
    private Long createdAt;

    public UserProfileDTO(String userName, String firstName, String lastName, String avatarURL, String title, String bio, Long createdAt) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarURL = avatarURL;
        this.title = title;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

}
