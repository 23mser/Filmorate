package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void likeFilm(Integer filmId, Integer userId) {
        final String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        final String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        int status = jdbcTemplate.update(sqlQuery, filmId, userId);
        if (status != 1) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
    }
}
