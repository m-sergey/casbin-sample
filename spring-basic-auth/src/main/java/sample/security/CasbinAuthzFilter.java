package sample.security;

import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import sample.model.Profile;
import sample.repo.ProfileRepo;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
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
        log.info("Filter initialized");
    }

    // In this demo, we use HTTP basic authentication as the authentication method.
    // This method retrieves the user name from the HTTP header and passes it to jCasbin.
    // You can change to your own authentication method like OAuth, JWT, Apache Shiro, etc.
    // You need to implement this getUser() method to make sure jCasbin can get the
    // authenticated user name.
    private Optional<Profile> authenticate(HttpServletRequest request) {

        final String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Basic")) {
            return Optional.empty();
        }

        String base64Credentials = authorization.substring("Basic".length()).trim();
        // credentials = "username:password"
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);

        Optional<Profile> profile = userRepo.find(values[0]);
        if(profile.isEmpty() || !"password".equals(values[1])) {
            return Optional.empty();
        }

        return profile;
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

        if (!enforcer.enforce(profile.get().getUsername(), path, method)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }
}
