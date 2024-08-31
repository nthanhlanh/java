package com.alibou.security.service.security;

import com.alibou.security.domain.Token;
import com.alibou.security.domain.User;
import com.alibou.security.dto.TokenType;
import com.alibou.security.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false)
                .build();
        tokenRepository.save(token);
    }

}
