//package com.uny.unydatabaseredmine.auth.service;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.uny.unydatabaseredmine.auth.models.Employee;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//public class UserDetailsImpl implements UserDetails {
//    private static final long serialVersionUID = 1L;
//    @Getter
//    @Setter
//    private Long id;
//
//    private String name;
//
//    private String email;
//
//    @JsonIgnore
//    private String password;
//    @Getter
//    @Setter
//    private String job_title;
//
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public UserDetailsImpl(Long id, String name, String email, String password,
//                           Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.name = name ;
//        this.email = email;
//        this.password = password;
//        this.authorities = authorities;
//    }
//
//    public static UserDetailsImpl build(Employee user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
//                .collect(Collectors.toList());
//
//        return new UserDetailsImpl(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getPassword(),
//                authorities);
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//    public String getEmail() {
//
//        return email;
//    }
//
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//        if (o == null || getClass() != o.getClass())
//            return false;
//        UserDetailsImpl user = (UserDetailsImpl) o;
//        return Objects.equals(id, user.id);
//    }
//}
