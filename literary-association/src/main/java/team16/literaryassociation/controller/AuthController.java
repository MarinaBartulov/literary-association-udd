package team16.literaryassociation.controller;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.JwtAuthenticationRequestDTO;
import team16.literaryassociation.dto.UserTokenStateDTO;
import team16.literaryassociation.model.User;
import team16.literaryassociation.security.TokenUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IdentityService identityService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequestDTO authenticationRequest,
                                                       HttpServletResponse response) throws AuthenticationException, IOException {
        Authentication authentication = null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        String role = user.getRoles().iterator().next().getName();
        identityService.setAuthenticatedUserId(authenticationRequest.getUsername());
        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn, role, user.getId()));
    }
}
