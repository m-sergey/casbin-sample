package sample.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import sample.model.Profile;
import sample.repo.ProfileRepo;
import sample.security.func.ContainsFunc;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@WebFilter("/*")
@Slf4j
public class CasbinAuthzFilter implements Filter{

    private static ProfileRepo userRepo;
    private static Enforcer enforcer;

    // Initialize jCasbin's enforcer with model and policy rules.
    // Here we load policy from file, you can choose to load policy from database.
    public void init(FilterConfig filterConfig) throws ServletException {
        userRepo = new ProfileRepo();
        URL model = getClass().getClassLoader().getResource("authz_model.conf");
        URL policy = getClass().getClassLoader().getResource("authz_policy.csv");
        enforcer = new Enforcer(model.getPath(), policy.getFile());
        enforcer.addFunction(ContainsFunc.getNameStatic(), new ContainsFunc());
        log.info("Filter initialized");
    }

    public void destroy() {
    }

    // Filters all requests through jCasbin's authorization.
    // If jCasbin allows the request, pass the request to next handler.
    // If jCasbin denies the request, return HTTP 403 Forbidden.
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Optional<Profile> profile = authenticate(request);
        if(profile.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (!enforcer.enforce(profile.get(), path, method)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }



    // In this demo, we use HTTP basic authentication as the authentication method.
    // This method retrieves the user name from the HTTP header and passes it to jCasbin.
    // You can change to your own authentication method like OAuth, JWT, Apache Shiro, etc.
    // You need to implement this getUser() method to make sure jCasbin can get the
    // authenticated user name.
    private Optional<Profile> authenticate(HttpServletRequest request) {

        final String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            return Optional.empty();
        }

        // Get jwt token and validate
        final String token = authorization.split(" ")[1].trim();

        Optional<DecodedJWT> jwt = validate(token);

        if (jwt.isEmpty()) {
            return Optional.empty();
        }

        Optional<Profile> profile = Optional.of(new Profile(jwt.get().getSubject(),
                jwt.get().getClaim("perms").asList(String.class)));

        return profile;
    }

    private Optional<DecodedJWT> validate(String token) {
        DecodedJWT jwt = JWT.decode(token);

        //TODO Add extra validation

        return Optional.of(jwt);
    }
}
