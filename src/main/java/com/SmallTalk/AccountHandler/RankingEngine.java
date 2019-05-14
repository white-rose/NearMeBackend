package com.SmallTalk.AccountHandler;

import com.SmallTalk.model.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RankingEngine {

  public List<User> rankByRelevance(User loggedInUser, Set<User> users) {

    List<User> sortedUsers = new ArrayList<>();

    users.forEach(
        user -> {
          if (user.getSchool().equals(loggedInUser.getSchool())) {
            sortedUsers.add(user);
          }
        });

    users.forEach(
        user -> {
          if (!user.getSchool().equals(loggedInUser.getSchool())) {
            sortedUsers.add(user);
          }
        });

    return sortedUsers;

    //        return users.stream().sorted((o1, o2) -> {
    //            if (o1.getSchool().equals(loggedInUser.getSchool()) &&
    // (!o2.getSchool().equals(loggedInUser.getSchool()))) {
    //                return -1;
    //            } else {
    //                return 1;
    //            }
    ////            if (o1.getEmployer().equals(loggedInUser.getEmployer())) {
    ////                return 0;
    ////            } else if (o2.getEmployer().equals(loggedInUser.getEmployer())) {
    ////                return 1;
    ////            }
    ////            return 0;
    //        }).toArray(User[]::new);
  }
}
