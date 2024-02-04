package com.ziggly.teamservice;


import com.ziggly.model.dto.TeamDTO;
import com.ziggly.model.dto.TeamUserDTO;
import com.ziggly.model.enums.Role;
import com.ziggly.teamservice.repository.TeamRepository;
import com.ziggly.teamservice.service.TeamServiceImpl;
import com.ziggly.teamservice.service.TeamUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.ziggly.model.utils.Utils.getMockupRequest;
import static com.ziggly.model.utils.Utils.getUserId;
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest
@Testcontainers
class MainTests {
	@Container
	private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("test")
			.withUsername("user")
			.withPassword("pass");

	@DynamicPropertySource
	static void postgresProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
	}

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TeamServiceImpl teamService;

	@Autowired
	private TeamUserServiceImpl teamUserService;
	//

	private void setTestTeamDTO(TeamDTO teamDTO, String name){
		teamDTO.setName(name);
		teamDTO.setDescription("A development team");
		teamDTO.setOwnerId(getUserId(getMockupRequest()));
		teamDTO.setCreatedAt(System.currentTimeMillis());

	}

	//
	@Test
	void testTeamCRUD() {
		TeamDTO teamDTO = new TeamDTO();
		setTestTeamDTO(teamDTO, "devTeam");

		// create team
		assertEquals(
				HttpStatus.OK,
				teamService.createTeam(teamDTO, getMockupRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.BAD_REQUEST,
				teamService.createTeam(teamDTO, getMockupRequest()).getStatusCode()
		);

		// get team
		assertEquals(
				HttpStatus.OK,
				teamService.getTeam(1, getMockupRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamService.getTeam(2, getMockupRequest()).getStatusCode()
		);


		// update team
		TeamDTO updatedTeamDTO;
		updatedTeamDTO = teamDTO;
		setTestTeamDTO(updatedTeamDTO, "updatedTeamDTO");

		assertEquals(
				HttpStatus.OK,
				teamService.updateTeam(1, updatedTeamDTO, getMockupRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.BAD_REQUEST,
				teamService.updateTeam(1, teamDTO, getMockupRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamService.updateTeam(2, updatedTeamDTO, getMockupRequest()).getStatusCode()
		);


		// delete team
		assertEquals(
				HttpStatus.FORBIDDEN,
				teamService.deleteTeam(1, getMockupRequest("2")).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamService.deleteTeam(2, getMockupRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.OK,
				teamService.deleteTeam(1, getMockupRequest()).getStatusCode()
		);


	}

	@Test
	void testTeamUserCRUD() {

		TeamDTO teamDTO = new TeamDTO();
		setTestTeamDTO(teamDTO, "devTeam");

		// create team
		assertEquals(
				HttpStatus.OK,
				teamService.createTeam(teamDTO, getMockupRequest()).getStatusCode()
		);


		TeamUserDTO teamUserDTO = new TeamUserDTO(1, getUserId(getMockupRequest()), 2, Role.MEMBER);

		//create user
		assertEquals(
				HttpStatus.OK,
				teamUserService.addUserToTeam(2, teamUserDTO, getMockupRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.BAD_REQUEST,
				teamUserService.addUserToTeam(2, teamUserDTO, getMockupRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamUserService.addUserToTeam(3, teamUserDTO, getMockupRequest()).getStatusCode()
		);

		//get user
		assertEquals(
				HttpStatus.OK,
				teamUserService.getUsersInTeam(2, getMockupRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamUserService.getUsersInTeam(3, getMockupRequest()).getStatusCode()
		);

		//update user
		TeamUserDTO teamUserDTOModified = new TeamUserDTO(1, getUserId(getMockupRequest()), 2, Role.ADMIN);
		assertEquals(
				HttpStatus.OK,
				teamUserService.updateUserRoleInTeam(2, getUserId(getMockupRequest()), teamUserDTO, getMockupRequest()).getStatusCode()
		);

		assertEquals(
				HttpStatus.NOT_FOUND,
				teamUserService.updateUserRoleInTeam(3, getUserId(getMockupRequest()), teamUserDTO, getMockupRequest()).getStatusCode()
		);

		//delete user
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamUserService.removeUserFromTeam(3, 2, getMockupRequest("2")).getStatusCode()
		);
		assertEquals(
				HttpStatus.OK,
				teamUserService.removeUserFromTeam(2, getUserId(getMockupRequest()), getMockupRequest()).getStatusCode()
		);

	}

}
