package com.SmallTalk.AccountHandler;

import com.SmallTalk.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository <User, Long> {  }
