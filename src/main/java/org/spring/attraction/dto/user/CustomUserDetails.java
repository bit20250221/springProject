package org.spring.attraction.dto.user;

import org.spring.attraction.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    // ViewUserDTO를 반환하는 메서드
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override // 사용자의 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> user.getUserType().name()); // 유저 타입을 권한으로 반환
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPass(); // 비밀번호는 반환하지 않음 (보안상 필요 없음)
    }

    @Override
    public String getUsername() {
        return user.getUserLoginId(); // 유저 로그인 ID 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (true = 만료되지 않음)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부 (true = 잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 인증 정보 만료 여부 (true = 만료되지 않음)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (true = 활성화됨)
    }
}
