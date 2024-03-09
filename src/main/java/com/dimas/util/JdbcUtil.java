package com.dimas.util;

import com.dimas.exception.MyJdbcException;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static java.util.Objects.isNull;

@UtilityClass
public class JdbcUtil {

    public static java.sql.Date toDate(LocalDate date) {
        if (isNull(date)) return null;
        return java.sql.Date.valueOf(date);
    }

    public static java.sql.Timestamp toTimestamp(LocalDateTime date) {
        if (isNull(date)) return null;
        return java.sql.Timestamp.valueOf(date);
    }

    public static LocalDate getLocalDate(ResultSet resultSet, String column) {
        try {
            var source = resultSet.getDate(column);
            return isNull(source) ? null : new Date(source.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (SQLException e) {
            throw new MyJdbcException(e);
        }
    }

    public static LocalDateTime getLocalDateTime(ResultSet resultSet, String column) {
        try {
            var source = resultSet.getDate(column);
            return isNull(source) ? null : LocalDateTime.ofInstant(new Date(source.getTime()).toInstant(), ZoneId.systemDefault());
        } catch (SQLException e) {
            throw new MyJdbcException(e);
        }
    }

    public static UUID getUUID(ResultSet resultSet, String column) {
        try {
            var source = resultSet.getString(column);
            return isNull(source) ? null : UUID.fromString(source);
        } catch (SQLException e) {
            throw new MyJdbcException(e);
        }
    }

}
