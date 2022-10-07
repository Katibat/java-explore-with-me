package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.join;

@Repository
@RequiredArgsConstructor
public class StatsRepository {
    public static final String TABLE_NAME = "stats";
    private final JdbcTemplate jdbcTemplate;

    public EndpointHit save(EndpointHit endpointHit) {
        if (endpointHit.getTimestamp() == null) {
            endpointHit.setTimestamp(LocalDateTime.now());
        }
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_NAME).usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("app", endpointHit.getApp())
                .addValue("uri", endpointHit.getUri())
                .addValue("ip", endpointHit.getIp())
                .addValue("timestamp", endpointHit.getTimestamp());
        Number id = jdbcInsert.executeAndReturnKey(parameters);
        endpointHit.setId(id.longValue());
        return endpointHit;
    }

    public List<ViewStatsDto> getStats(List<String> uris,
                                       Boolean unique,
                                       LocalDateTime start,
                                       LocalDateTime end,
                                       String appName) {
        String sql = format("SELECT uri, COUNT(%s) count FROM %s WHERE timestamp>? AND timestamp<? AND app=? %s " +
                        "GROUP BY uri", unique ? "DISTINCT ip" : "*", TABLE_NAME, uris == null ? "" : "AND uri IN(?)");
        if (uris == null) {
            return jdbcTemplate.query(sql, this::mapRowToViewStatsDto, start, end, appName);
        }
        return jdbcTemplate.query(sql, this::mapRowToViewStatsDto, start, end, appName, join(", ", uris));
    }

    private ViewStatsDto mapRowToViewStatsDto(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() == 0) return null;
        return ViewStatsDto.builder()
                .uri(rs.getString("uri"))
                .hits(rs.getLong("count"))
                .build();
    }

    public Integer getViews(String uri) {
        String sql = "SELECT count(id) FROM stats WHERE uri = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, uri);
        Integer count = 0;
        if (rowSet.next()) {
            count++;
        }
        return count;
    }
}