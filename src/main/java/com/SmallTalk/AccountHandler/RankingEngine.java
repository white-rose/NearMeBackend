package com.SmallTalk.AccountHandler;

import com.SmallTalk.model.User.User;

import java.util.Set;

public class RankingEngine {
    public User[] rankByRelevance(User loggedInUser, Set<User> users) {


        return users.parallelStream().sorted((o1, o2) -> {
            if (o1.getSchool().equals(loggedInUser.getSchool())) {
                return 0;
            } else if (o2.getSchool().equals(loggedInUser.getSchool())) {
                return 1;
            }
            if (o1.getEmployer().equals(loggedInUser.getEmployer())) {
                return 0;
            } else if (o2.getEmployer().equals(loggedInUser.getEmployer())) {
                return 1;
            }
            return 0;

        }).toArray(User[]::new);

    }

}
