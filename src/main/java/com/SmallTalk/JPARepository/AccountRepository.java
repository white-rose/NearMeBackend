package com.SmallTalk.JPARepository;

import com.SmallTalk.model.User.User;

//@Repository
public interface AccountRepository /*extends Repository<User, Long>*/ {

    /**
     *
     * @param user
     * @param <S>
     * @return
     */
   <S extends User> S save(S user);

}
