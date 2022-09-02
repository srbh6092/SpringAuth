package com.srbh6092.springauth.controller;

import com.srbh6092.springauth.entity.Role;
import com.srbh6092.springauth.entity.RoleType;
import com.srbh6092.springauth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @GetMapping
    public List<Role> getRole(){
        return roleRepository.findAll();
    }
}
