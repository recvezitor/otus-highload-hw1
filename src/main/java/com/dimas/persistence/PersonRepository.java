package com.dimas.persistence;

import com.dimas.domain.entity.Person;
import com.dimas.exception.MyJdbcException;
import com.dimas.exception.NotFoundJdbcException;
import com.dimas.log.LogArguments;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@LogArguments
public class PersonRepository {

    private final AgroalDataSource dataSource;


    public Person create(Person request) {
        log.info("persisting request person={}", request);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    insert into %s.person(first_name, second_name, birthdate, biography, city, created_at, password)
                    values (?, ?, ?, ?, ?, ?, ?)
                    RETURNING id
                    """.formatted(SCHEMA_NAME), Statement.RETURN_GENERATED_KEYS);
            var id = request.getId();
            if (!isNull(id)) {
                log.warn("id will be overrriden");
            }
            preparedStatement.setString(1, request.getFirstName());
            preparedStatement.setString(2, request.getSecondName());
            preparedStatement.setDate(3, toDate(request.getBirthdate()));
            preparedStatement.setString(4, request.getBiography());
            preparedStatement.setString(5, request.getCity());
            preparedStatement.setTimestamp(6, toTimestamp(LocalDateTime.now()));
            preparedStatement.setString(7, request.getPassword());
            try {
                connection.beginRequest();
                log.info("commit person={}", request);
                int i = preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                rs.next();
                UUID generatedUUID = getUUID(rs, "id");
                log.info("generated id={}", generatedUUID);
                request.setId(generatedUUID);
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

    public Optional<Person> findByName(String name) {
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

    public List<Person> search(String firstName, String lastName) {//warn, no paging
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    select * from %s.person where lower(first_name) LIKE ? AND lower(second_name) LIKE ? LIMIT 10000
                    """.formatted(SCHEMA_NAME));
            preparedStatement.setString(1, firstName.toLowerCase() + "%");
            preparedStatement.setString(2, lastName.toLowerCase() + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Person> result = new ArrayList<>();
            while (resultSet.next()) {
                Person entity = map(resultSet);
                result.add(entity);
            }
            return result;
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
