package com.example.logindemo.security.services;

import com.example.logindemo.exception.responsecode.MgrResponseCode;
import com.example.logindemo.models.RefreshToken;
import com.example.logindemo.models.User;
import com.example.logindemo.repository.RefreshTokenRepository;
import com.example.logindemo.repository.UserRepository;
import com.example.logindemo.exception.tokenrefresh.TokenRefreshException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenService {

    /** 24小時 */
    @Value("${logindemo.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Resource
    private RefreshTokenRepository refreshTokenRepository;

    @Resource
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * 產生 refresh token 並存至 db
     *
     * @param userId 使用者id
     * */
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(MgrResponseCode.REFRESH_TOKEN_EXPIRED,token.getToken());
        }
        return token;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByUserId(User user) {
        return refreshTokenRepository.deleteByUser(user);
    }
}
