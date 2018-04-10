package com.SmallTalk.JPARepository;

import com.SmallTalk.model.User.User;

import java.util.List;

//@Repository
public interface AccountRepository /* extends CrudRepository<User, Long> */ {

    List<User> findByLocation(String Location);

}
