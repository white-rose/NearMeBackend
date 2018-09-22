package com.SmallTalk.AccountHandler;

import com.SmallTalk.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository <User, Long> {

    List<User> findByUsername(String username);

}
