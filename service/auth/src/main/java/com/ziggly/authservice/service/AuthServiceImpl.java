package com.ziggly.authservice.service;

import com.ziggly.authservice.api.AuthService;
import com.ziggly.authservice.repository.UserProfileRepository;
import com.ziggly.authservice.repository.UserRepository;
import com.ziggly.authservice.repository.UserSessionRepository;
import com.ziggly.model.dto.AuthRequest;
import com.ziggly.model.dto.SignUpRequest;
import com.ziggly.model.dto.UserSessionDTO;
import com.ziggly.model.user.User;
import com.ziggly.model.user.UserProfile;
import com.ziggly.model.user.UserSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import java.security.Key;
import java.util.*;

import eu.bitwalker.useragentutils.*;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor

public class AuthServiceImpl implements AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserSessionRepository userSessionRepository;



    ////////
    private static final String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final Integer tokenValidDays = 30;

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date getExpirationDateInDays(int days) {
        long expirationTime = 1000L * 60 * 60 * 24 * days;
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    private String generateToken(Integer userId){
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpirationDateInDays(tokenValidDays))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private List<UserSession> getUserSessionsByUserName(String userName){
        User user = userRepository.findByUserName(userName).orElse(null);
        List<UserSession> userSessions = Collections.emptyList();
        if (user != null){
            userSessions = userSessionRepository.findByUser(user);
        }
        return userSessions;
    }
    private List<UserSession> getUserSessionsByUserId(Integer id){
        User user = userRepository.findById(id).orElse(null);
        List<UserSession> userSessions = Collections.emptyList();
        if (user != null){
            userSessions = userSessionRepository.findByUser(user);
        }
        return userSessions;
    }

    private static List<UserSessionDTO> getUserSessionDTOS(List<UserSession> userSessions) {
        List<UserSessionDTO> userSessionDTOS  = new ArrayList<>();

        for(UserSession userSession : userSessions){
            UserSessionDTO userSessionDTO = new UserSessionDTO();

            userSessionDTO.setId(userSession.getId());
            userSessionDTO.setUserAgent(userSession.getUserAgent());
            userSessionDTO.setApp(userSession.getApp());
            userSessionDTO.setOs(userSession.getOs());
            userSessionDTO.setLoginFirst(userSession.getLoginFirst());
            userSessionDTO.setLoginLast(userSession.getLoginLast());

            userSessionDTOS.add(userSessionDTO);

        }
        return userSessionDTOS;
    }

    //


    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Integer.parseInt(claims.getSubject());
        } catch (SignatureException e) {
            // Handle the exception if the token signature is invalid
            // This might mean the token has been tampered with or is invalid
            return null;
        } catch (Exception e) {
            // Handle other exceptions such as parsing errors
            return null;
        }
    }


    private String extractDeviceInfo(String userAgentString) {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);


        Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();

        return os.getName() + " " + browser.getName();
    }
    private String getUserAgentBrowser(String userAgentString){
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        return userAgent.getBrowser().getName();
    }
    private String getUserAgentOS(String userAgentString){
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        return userAgent.getOperatingSystem().getName();
    }
    private String getUserAgent(HttpServletRequest request){
        return request.getHeader("User-Agent");
    }
    private String getUserAgentDisplayName(HttpServletRequest request){
        return extractDeviceInfo(getUserAgent(request));
    }


    private UserSession getUserSessionByRequestSessions(List <UserSession> userSessions, HttpServletRequest request) {
        for(UserSession session : userSessions){

            if(Objects.equals(session.getUserAgent(), getUserAgent(request))){
                return session;
            }
        }
        return null;
    }
    private UserSession getUserSessionByTokenSessions(List <UserSession> userSessions, String token) {
        for(UserSession session : userSessions){

            if(Objects.equals(session.getToken(), token)){
                return session;
            }
        }
        return null;
    }

    private UserSession getUserSessionByToken(String token) {
        Integer id = getUserIdFromToken(token);
        if (id != null) {

            User user = userRepository.findById(id).orElse(null);
            if (user != null) {

                List<UserSession> userSessions = getUserSessionsByUserId(id);
                if (!userSessions.isEmpty()) {

                    return getUserSessionByTokenSessions(userSessions, token);

                }
            }
        }

        return null;
    }

    private UserSession generateNewSession(User user, HttpServletRequest request){
        UserSession userSession = new UserSession();

        userSession.setUser(user);
        userSession.setUserAgent(getUserAgent(request));
        userSession.setApp(getUserAgentBrowser(getUserAgent(request)));
        userSession.setOs(getUserAgentOS(getUserAgent(request)));
        userSession.setIp(request.getRemoteAddr());
        userSession.setLoginFirst(System.currentTimeMillis());
        userSession.setLoginLast(System.currentTimeMillis());
        userSession.setToken(generateToken(user.getId()));

        return userSession;
    }
    ///////

    private boolean isValidName(String name, boolean isUserName){
        if (!StringUtils.hasText(name)) {
            return false;
        }

        if (name.length() < 3) {
            return false;
        }

        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return false;
            }
            if (!isUserName){
                if (Character.isDigit(c)) {
                    return false;
                }
            }
        }
        // to-do: add check in db for cursed words
        return true;
    }
    private boolean isValidEmail(String email){
        if(email == null || !email.contains(".") || !email.contains("@")){
            return false;
        }
        // to-do: add check on db for cursed words

        return true;
    }
    public boolean isValidPassword(String password){
        if(password == null || password.length() < 8){
            return false;
        }
        boolean lowerCase = false;
        boolean upperCase = false;
        boolean specials = false;
        boolean numbers = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upperCase = true;
            }
            if (Character.isLowerCase(c)) {
                lowerCase = true;
            }
            if (Character.isDigit(c)) {
                numbers = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                if (!Character.isWhitespace(c)) {
                    specials = true;
                }
            }
        }
        return (lowerCase && upperCase && specials && numbers);
    }
    @Override
    public ResponseEntity<String> signUp(SignUpRequest signUpRequest) {

        //validate data input
        if(!isValidName(signUpRequest.getUserName(), true)){
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Invalid user name!");
        }
        if(!isValidName(signUpRequest.getFirstName(), false)){
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Invalid first name!");
        }
        if(!isValidName(signUpRequest.getLastName(), false)){
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Invalid last name!");
        }
        if(!isValidEmail(signUpRequest.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Invalid email!");
        }
        if(!isValidPassword(signUpRequest.getPassword())){
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Invalid password!");
        }

        //  check for existing entries in db
        if (userRepository.findByUserName(signUpRequest.getUserName()).isPresent()){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists!");
        }
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already exists.");
        }

        // fill new user data
        User user = new User();
        UserProfile userProfile = new UserProfile();

        user.setUserName(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        user = userRepository.save(user);

        userProfile.setUser(user);
        userProfile.setFirstName(signUpRequest.getFirstName());
        userProfile.setLastName(signUpRequest.getLastName());
        userProfile.setCreatedAt(System.currentTimeMillis());

        userProfile = userProfileRepository.save(userProfile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Success!");
    }

    @Override
    public ResponseEntity<String> tokenValidate(String token, HttpServletRequest request) {

        UserSession userSession = getUserSessionByToken(token);
        if(userSession != null){

            userSession.setLoginLast(System.currentTimeMillis());

            // probably not a correct ip will be given
            userSession.setIp(request.getRemoteAddr());

            userSessionRepository.save(userSession);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(userSession.getUser().getId().toString());

        }

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Token is invalid!");
    }

    @Override
    public ResponseEntity<String> login(AuthRequest authRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if(authentication.isAuthenticated()){

            List<UserSession> userSessions = getUserSessionsByUserName(authRequest.getUsername());
            if (!userSessions.isEmpty()){

                UserSession userSession = getUserSessionByRequestSessions(userSessions, request);
                if(userSession != null){

                    userSession.setLoginLast(System.currentTimeMillis());
                    userSession.setIp(request.getRemoteAddr());

                    userSessionRepository.save(userSession);

                    return ResponseEntity
                            .status(HttpStatus.ACCEPTED)
                            .body(userSession.getToken());
                }
            }

            User user = userRepository.findByUserName(authRequest.getUsername()).orElse(null);
            if (user != null){
                UserSession userSession = generateNewSession(user, request);

                userSessionRepository.save(userSession);

                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(userSession.getToken());
            }

        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Couldn't Authorize!");
    }

    @Override
    public ResponseEntity<String> logout(String token, HttpServletRequest request) {
        ResponseEntity <String> validationResponse = tokenValidate(token, request);

        if(validationResponse.getStatusCode() == HttpStatus.ACCEPTED){

            UserSession userSession = getUserSessionByToken(token);
            if(userSession != null){
                userSessionRepository.deleteById(userSession.getId());
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("Logout Successful!");
            }
        }
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Couldn't Complete!");
    }


    @Override
    public ResponseEntity<List<UserSessionDTO>> getSessions(String token, HttpServletRequest request) {
        ResponseEntity <String> validationResponse = tokenValidate(token, request);

        if(validationResponse.getStatusCode() == HttpStatus.ACCEPTED) {

            Integer userId = getUserIdFromToken(token);
            List<UserSession> userSessions = getUserSessionsByUserId(userId);
            List<UserSessionDTO> userSessionDTOS = getUserSessionDTOS(userSessions);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userSessionDTOS);

        }

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(null);
    }


    @Override
    public ResponseEntity<String> deleteSession(Integer sessionId, String token, HttpServletRequest request) {

        ResponseEntity <String> validationResponse = tokenValidate(token, request);

        if(validationResponse.getStatusCode() == HttpStatus.ACCEPTED) {
            Integer userId = getUserIdFromToken(token);

            UserSession userSession = userSessionRepository.findById(sessionId).orElse(null);
            Optional<UserSession> optionalUserSession = userSessionRepository.findById(sessionId);

            if(userSession != null && userSession.getUser() != null){

                if(userSession.getUser().getId().equals(userId)){
                    userSessionRepository.deleteById(sessionId);

                    return ResponseEntity
                            .status(HttpStatus.ACCEPTED)
                            .body("User session successfully deleted!");

                }
            }

        }
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Couldn't Complete!");
    }

}
