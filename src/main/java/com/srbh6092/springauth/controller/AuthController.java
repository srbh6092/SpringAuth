package com.srbh6092.springauth.controller;

import com.srbh6092.springauth.entity.Role;
import com.srbh6092.springauth.entity.RoleType;
import com.srbh6092.springauth.entity.User;
import com.srbh6092.springauth.payload.request.LoginRequest;
import com.srbh6092.springauth.payload.request.SignupRequest;
import com.srbh6092.springauth.payload.response.JwtResponse;
import com.srbh6092.springauth.payload.response.MessageResponse;
import com.srbh6092.springauth.repository.RoleRepository;
import com.srbh6092.springauth.repository.UserRepository;
import com.srbh6092.springauth.securityjwt.jwt.JwtUtils;
import com.srbh6092.springauth.securityjwt.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser (@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username already taken!"));

        if (userRepository.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Account already exists with this email!"));

        User user  =  User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword())).build();

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles =new HashSet<>();
        Optional<Role> role;
        if(strRoles == null) {
            System.err.println("strRoles= "+strRoles);
            role = roleRepository.findRoleByName(RoleType.USER);
            if(!role.isPresent())
                throw new RuntimeException("Error: Role not found!");
            roles.add(role.get());
        } else {
            System.err.println(strRoles);
            for(String r: strRoles){
                switch (r) {
                    case "admin":
                    case "ADMIN":
                        role = roleRepository.findRoleByName(RoleType.ADMIN);
                        break;
                    case "moderator":
                    case "MODERATOR":
                        role = roleRepository.findRoleByName(RoleType.MODERATOR);
                        break;
                    default:
                        role = roleRepository.findRoleByName(RoleType.USER);
                }
                System.err.println("Role="+role.get());
                if(!role.isPresent())
                    throw new RuntimeException("Error: Role not found!");
                roles.add(role.get());
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/user")
    public List<User> getall() {
        return userRepository.findAll();
    }
}