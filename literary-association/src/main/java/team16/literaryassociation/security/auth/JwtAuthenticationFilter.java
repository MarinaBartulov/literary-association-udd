package team16.literaryassociation.security.auth;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import team16.literaryassociation.security.TokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private TokenUtils tokenUtils;
    private UserDetailsService userDetailsService;
    private IdentityService identityService;

    public JwtAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService,
                                   IdentityService identityService) {
        this.tokenUtils = tokenHelper;
        this.userDetailsService = userDetailsService;
        this.identityService = identityService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    {
        String username;
        String authToken = tokenUtils.getToken(request);

        if (authToken != null) {
            username = tokenUtils.getUsernameFromToken(authToken);

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (tokenUtils.validateToken(authToken, userDetails)) {

                    List<Group> groups = this.identityService.createGroupQuery().groupMember(username).list();
                    List<String> userIds = groups.stream().map(Group::getId).collect(Collectors.toList());
                    Authentication auth = new Authentication(username, userIds);
                    this.identityService.setAuthentication(auth);
                    this.identityService.setAuthenticatedUserId(username);

                    JwtBasedAuthentication authentication = new JwtBasedAuthentication(userDetails);
                    authentication.setToken(authToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {

            if (identityService.getCurrentAuthentication() == null) {
                List<String> userIds = new ArrayList<>();
                userIds.add("guests");
                Authentication auth = new Authentication("guest", userIds);
                this.identityService.setAuthenticatedUserId("guest");
                this.identityService.setAuthentication(auth);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }
    }
}
