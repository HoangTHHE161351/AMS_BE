//package vn.travel.repository.impl;
//
//import org.springframework.stereotype.Repository;
//import vn.travel.config.authen.entity.RefreshToken;
//
//import java.time.Instant;
//import java.util.Optional;
//
//@Repository
//public class RefreshTokenRepositoryImpl implements RefreshTokenRepository_ {
//    @Override
//    public Optional<RefreshToken> findById(Long id) {
//        RefreshToken refreshToken = getRefreshToken();
//        return Optional.of(refreshToken);
//    }
//
//    @Override
//    public Optional<RefreshToken> findByToken(String token) {
//        RefreshToken refreshToken = getRefreshToken();
//        return Optional.of(refreshToken);
//    }
//
//    @Override
//    public RefreshToken save(RefreshToken refreshToken) {
//        refreshToken.setId(1L);
//        return refreshToken;
//    }
//
//    @Override
//    public void delete(RefreshToken token) {
//        // todo: delete refresh token
//    }
//
//    @Override
//    public Optional<RefreshToken> findByUserId(Long userId) {
//        RefreshToken refreshToken = getRefreshToken();
//        return Optional.of(refreshToken);
//    }
//
//    @Override
//    public Long deleteByUser(User User) {
//        RefreshToken refreshToken = getRefreshToken();
//        return refreshToken.getId();
//    }
//
//    private User getUser() {
//        return null;
//    }
//
//    private RefreshToken getRefreshToken() {
//        User user = getUser();
//        return new RefreshToken(1L, user, "asdqeqweqwe1313423431323123qwewqeqwe", Instant.now());
//    }
//}
