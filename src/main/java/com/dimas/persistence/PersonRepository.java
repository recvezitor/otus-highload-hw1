package com.dimas.persistence;

import com.dimas.domain.entity.Person;
import com.dimas.exception.MyJdbcException;
import com.dimas.exception.NotFoundJdbcException;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.dimas.util.Const.SCHEMA_NAME;
import static com.dimas.util.JdbcUtil.getLocalDate;
import static com.dimas.util.JdbcUtil.getLocalDateTime;
import static com.dimas.util.JdbcUtil.getUUID;
import static com.dimas.util.JdbcUtil.toDate;
import static com.dimas.util.JdbcUtil.toTimestamp;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PersonRepository {

    private final AgroalDataSource dataSource;


    public Person create(Person request) {
        log.info("persisting request person={}", request);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    insert into %s.person(id, first_name, second_name, birthdate, biography, city, created_at, password)
                    values (?, ?, ?, ?, ?, ?, ?, ?)
                    """.formatted(SCHEMA_NAME));
            var id = request.getId();
            if (!isNull(id)) {
                throw new MyJdbcException("id is not null");
            }
            request.setId(UUID.randomUUID());
            PGobject pGobject = new PGobject();
            pGobject.setType("uuid");
            pGobject.setValue(request.getId().toString());
            preparedStatement.setObject(1, pGobject, Types.OTHER);
            preparedStatement.setString(2, request.getFirstName());
            preparedStatement.setString(3, request.getSecondName());
            preparedStatement.setDate(4, toDate(request.getBirthdate()));
            preparedStatement.setString(5, request.getBiography());
            preparedStatement.setString(6, request.getCity());
            preparedStatement.setTimestamp(7, toTimestamp(LocalDateTime.now()));
            preparedStatement.setString(8, request.getPassword());
            try {
                connection.beginRequest();
                log.info("commit person={}", request);
                int resultSet = preparedStatement.executeUpdate();
                if (resultSet <= 0) {
                    throw new MyJdbcException("JDBC exception. Unable to create person");
                }
                connection.commit();
                connection.setAutoCommit(true);
                return request;
            } catch (SQLException ex) {
                log.error("Transaction failed to commit, rolling back", ex);
                connection.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            log.error("JDBC exception", e);
            throw new MyJdbcException("JDBC exception", e);
        }
    }


    public Person getById(UUID id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundJdbcException("Person not found for id='%s'".formatted(id)));
    }

    public Optional<Person> findById(UUID id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    select * from %s.person where id = ? LIMIT 1
                    """.formatted(SCHEMA_NAME));
            PGobject pGobject = new PGobject();
            pGobject.setType("uuid");
            pGobject.setValue(id.toString());
            preparedStatement.setObject(1, pGobject, Types.OTHER);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Person entity = map(resultSet);
                return Optional.of(entity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new MyJdbcException("JDBC exception", e);
        }
    }

    public Person getByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new NotFoundJdbcException("Person not found for name='%s'".formatted(name)));
    }

    public Optional<Person> findByName(String name) {//TODO handle case when found more then 1
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    select * from %s.person where first_name = ?  LIMIT 1
                    """.formatted(SCHEMA_NAME));
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Person entity = map(resultSet);
                return Optional.of(entity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new MyJdbcException("JDBC exception", e);
        }
    }

    private Person map(ResultSet resultSet) throws SQLException {
        Person entity = new Person();
        entity.setId(getUUID(resultSet, "id"));
        entity.setFirstName(resultSet.getString("first_name"));
        entity.setSecondName(resultSet.getString("second_name"));
        entity.setBirthdate(getLocalDate(resultSet, "birthdate"));
        entity.setBiography(resultSet.getString("biography"));
        entity.setCity(resultSet.getString("city"));
        entity.setCreatedAt(getLocalDateTime(resultSet, "created_at"));
        entity.setUpdatedAt(getLocalDateTime(resultSet, "updated_at"));
        entity.setPassword(resultSet.getString("password"));
        return entity;
    }

}
