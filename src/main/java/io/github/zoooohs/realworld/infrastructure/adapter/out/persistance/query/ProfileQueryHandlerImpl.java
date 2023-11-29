package io.github.zoooohs.realworld.infrastructure.adapter.out.persistance.query;

import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.application.port.out.persistance.query.ProfileQueryHandler;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileQueryHandlerImpl implements ProfileQueryHandler {
    // TODO: mybatis 고려?
    private final DataSource dataSource;

    @Override
    public Optional<ProfileResponse> findProfile(UserId currentUserId, String username) {
        String id = currentUserId.id();
        ProfileResponse profileResponse;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT u.username as USERNAME, u.bio as BIO, u.image as IMAGE, " +
                "MAX(CASE WHEN fr.follower_id = ? THEN TRUE ELSE FALSE END) AS FOLLOWING " +
                "FROM USERS AS u " +
                "LEFT JOIN USER_FOLLOW_RELATIONS fr ON fr.followee_id = u.id " +
                "WHERE U.USERNAME = ? " +
                "GROUP BY U.ID;";
        try {
            profileResponse = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> ProfileResponse.builder()
                            .username(rs.getString("USERNAME"))
                            .bio(rs.getString("BIO"))
                            .image(rs.getString("IMAGE"))
                            .following(rs.getBoolean("FOLLOWING"))
                            .build(),
                    id, username);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(profileResponse);
    }
}
