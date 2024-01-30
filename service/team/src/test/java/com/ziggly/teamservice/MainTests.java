package com.ziggly.teamservice;


import com.ziggly.model.dto.TeamDTO;
import com.ziggly.model.dto.TeamUserDTO;
import com.ziggly.model.enums.Role;
import com.ziggly.model.team.Team;
import com.ziggly.teamservice.repository.TeamRepository;
import com.ziggly.teamservice.service.TeamServiceImpl;
import com.ziggly.teamservice.service.TeamUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mockito;
import jakarta.servlet.http.HttpServletRequest;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.Optional;

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

	@Test
	void contextLoads() {
	}

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TeamServiceImpl teamService;

	@Autowired
	private TeamUserServiceImpl teamUserService;
	//
	private HttpServletRequest getRequest(){
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-USER-ID")).thenReturn("123");
		return request;
	}
	private HttpServletRequest getRequest(String id){
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-USER-ID")).thenReturn(id);
		return request;
	}

	private Integer getUserId(HttpServletRequest request){
		String userId = request.getHeader("X-USER-ID");
		return Integer.parseInt(userId);
	}
	private void setTestTeamDTO(TeamDTO teamDTO, String name){
		teamDTO.setName(name);
		teamDTO.setDescription("A development team");
		teamDTO.setOwnerId(getUserId(getRequest()));
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
				teamService.createTeam(teamDTO, getRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.BAD_REQUEST,
				teamService.createTeam(teamDTO, getRequest()).getStatusCode()
		);

		// get team
		assertEquals(
				HttpStatus.OK,
				teamService.getTeam(1, getRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamService.getTeam(2, getRequest()).getStatusCode()
		);


		// update team
		TeamDTO updatedTeamDTO;
		updatedTeamDTO = teamDTO;
		setTestTeamDTO(updatedTeamDTO, "updatedTeamDTO");

		assertEquals(
				HttpStatus.OK,
				teamService.updateTeam(1, updatedTeamDTO, getRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.BAD_REQUEST,
				teamService.updateTeam(1, teamDTO, getRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamService.updateTeam(2, updatedTeamDTO, getRequest()).getStatusCode()
		);


		// delete team
		assertEquals(
				HttpStatus.FORBIDDEN,
				teamService.deleteTeam(1, getRequest("2")).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamService.deleteTeam(2, getRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.OK,
				teamService.deleteTeam(1, getRequest()).getStatusCode()
		);


	}

	@Test
	void testTeamUserCRUD() {

		TeamDTO teamDTO = new TeamDTO();
		setTestTeamDTO(teamDTO, "devTeam");

		// create team
		assertEquals(
				HttpStatus.OK,
				teamService.createTeam(teamDTO, getRequest()).getStatusCode()
		);


		TeamUserDTO teamUserDTO = new TeamUserDTO(1, getUserId(getRequest()), 2, Role.MEMBER);

		//create user
		assertEquals(
				HttpStatus.OK,
				teamUserService.addUserToTeam(2, teamUserDTO, getRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.BAD_REQUEST,
				teamUserService.addUserToTeam(2, teamUserDTO, getRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamUserService.addUserToTeam(3, teamUserDTO, getRequest()).getStatusCode()
		);

		//get user
		assertEquals(
				HttpStatus.OK,
				teamUserService.getUsersInTeam(2, getRequest()).getStatusCode()
		);
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamUserService.getUsersInTeam(3, getRequest()).getStatusCode()
		);

		//update user
		TeamUserDTO teamUserDTOModified = new TeamUserDTO(1, getUserId(getRequest()), 2, Role.ADMIN);
		assertEquals(
				HttpStatus.OK,
				teamUserService.updateUserRoleInTeam(2, getUserId(getRequest()), teamUserDTO, getRequest()).getStatusCode()
		);

		assertEquals(
				HttpStatus.NOT_FOUND,
				teamUserService.updateUserRoleInTeam(3, getUserId(getRequest()), teamUserDTO, getRequest()).getStatusCode()
		);

		//delete user
		assertEquals(
				HttpStatus.NOT_FOUND,
				teamUserService.removeUserFromTeam(3, 2, getRequest("2")).getStatusCode()
		);
		assertEquals(
				HttpStatus.OK,
				teamUserService.removeUserFromTeam(2, getUserId(getRequest()), getRequest()).getStatusCode()
		);

	}

}
