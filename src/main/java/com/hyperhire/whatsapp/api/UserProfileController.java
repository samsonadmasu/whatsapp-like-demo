package com.hyperhire.whatsapp.api;

import com.hyperhire.whatsapp.dto.request.UserProfileDTO;
import com.hyperhire.whatsapp.dto.response.paged.PagedResponse;
import com.hyperhire.whatsapp.entity.User;
import com.hyperhire.whatsapp.services.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @Operation(summary = "Get/view user profile")
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        User user = userProfileService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create new user profile")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Perform input validation
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Invoke the service method to create the user
        User createdUser = userProfileService.createUser(user);

        // Return the created user in the response body with HTTP status code 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody UserProfileDTO updatedProfile) {
        try {
            userProfileService.updateUserProfile(userId, updatedProfile);
            return ResponseEntity.ok("User profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user profile: " + e.getMessage());
        }
    }

    @Operation(summary = "Get all available users")
    @GetMapping()
    public ResponseEntity<PagedResponse<User>> getAllUsersProfile(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<User> userPage = userProfileService.getAllUsers(page, size);

        PagedResponse<User> response = new PagedResponse<>();
        response.setContent(userPage.getContent());
        response.setTotalPages(userPage.getTotalPages());
        response.setTotalElements(userPage.getTotalElements());
        response.setPageNumber(userPage.getNumber());
        response.setPageSize(userPage.getSize());

        if (!userPage.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
