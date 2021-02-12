package xyz.mainapi.dashboard.user;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserData> getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/logs")
    public Page<UserLogEventData> getCurrentUserLogEvent(Authentication authentication, @PageableDefault Pageable pageable) {
        return userService.getUserLogEvents(authentication, pageable);
    }

    @PutMapping(path = "/picture", consumes = "image/*")
    public UserData updateUserPicture(Authentication authentication, HttpServletRequest request) throws Exception {
        try (InputStream picture = request.getInputStream()) {
            return userService.updateCurrentUserPicture(authentication, picture, request.getContentType(), request.getContentLengthLong());
        }
    }
}
