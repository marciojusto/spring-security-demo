package com.example.demo.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.security.ApplicationUserPermission.COURSE_READ;
import static com.example.demo.security.ApplicationUserPermission.COURSE_WRITE;
import static com.example.demo.security.ApplicationUserPermission.STUDENT_READ;
import static com.example.demo.security.ApplicationUserPermission.STUDENT_WRITE;

@AllArgsConstructor
@Getter
public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    ADMIN_TRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final Set<ApplicationUserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissionsList = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissionsList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissionsList;
    }
}
