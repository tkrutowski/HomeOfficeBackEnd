package net.focik.homeoffice.userservice.domain.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.focik.homeoffice.utils.exceptions.HttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static net.focik.homeoffice.userservice.domain.security.constant.SecurityConstant.ACCESS_DENIED_MESSAGE;
import static net.focik.homeoffice.userservice.domain.security.constant.SecurityConstant.FORBIDDEN_MESSAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Nadpisuje oryginalny   response.sendError(403, "Access Denied");
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {// Http403ForbiddenEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
//        HttpResponse httpResponse = new HttpResponse(FORBIDDEN.value(), FORBIDDEN, FORBIDDEN.getReasonPhrase().toUpperCase(), FORBIDDEN_MESSAGE);
//
//        response.setContentType(APPLICATION_JSON_VALUE);
////        response.setStatus(FORBIDDEN.value());
//        response.setStatus(UNAUTHORIZED.value());
//
//        OutputStream outputStream = response.getOutputStream();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(outputStream,httpResponse);
//        outputStream.flush();
        HttpResponse httpResponse = new HttpResponse(UNAUTHORIZED.value(), UNAUTHORIZED, UNAUTHORIZED.getReasonPhrase().toUpperCase(), ACCESS_DENIED_MESSAGE);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());

        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream,httpResponse);
        outputStream.flush();
    }
}
