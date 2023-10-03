package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (rowSet.next()) {
            User user = User.builder()
                    .id(rowSet.getInt("user_id"))
                    .name(rowSet.getString("name"))
                    .login(rowSet.getString("login"))
                    .email(rowSet.getString("email"))
                    .birthday(Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate())
                    .build();
            users.add(user);
        }
        return users;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(toMap(user)).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        String sqlQuery = "UPDATE users " +
                "SET email=?, login=?, name=?, birthday=? " +
                "WHERE user_id=?";

        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        String sqlQuery =
                "DELETE " +
                        "FROM users " +
                        "WHERE user_id = ?";

        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public Optional<User> findUser(int userId) {
        String sqlQuery =
                "SELECT user_id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE user_id = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser, userId);
        return users.stream().findFirst();
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sqlQuery =
                "INSERT " +
                        "INTO friends (user_id, friend_id) " +
                        "VALUES(?, ?)";

        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlQuery =
                "DELETE " +
                        "FROM friends " +
                        "WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public Collection<User> findAllFriends(int id) {
        findUser(id);
        String sqlQuery =
                "SELECT user_id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE user_id " +
                        "IN(SELECT friend_id " +
                        "FROM friends " +
                        "WHERE user_id=?)";

        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::makeUser, id));
    }

    @Override
    public Collection<User> findCommonFriends(int id, int otherId) {
        String sqlQuery =
                "SELECT user_id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE user_id " +
                        "IN(SELECT friend_id " +
                        "FROM friends " +
                        "WHERE user_id = ?) " +
                        "AND user_id " +
                        "IN(SELECT friend_id " +
                        "FROM friends " +
                        "WHERE user_id = ?)";

        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::makeUser, id, otherId));
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

}
