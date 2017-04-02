package com.github.kickshare.db.dao;

import com.github.kickshare.db.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 31.3.2017
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
