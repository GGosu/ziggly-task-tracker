package com.ziggly.model.dto;


import com.ziggly.model.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class TeamUserDTO {
    private Integer id;
    private Integer userId;
    private Integer teamId;
    private Role role;


    public TeamUserDTO(Integer id, Integer userId, Integer teamId, Role role) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
}
