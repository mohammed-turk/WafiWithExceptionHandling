package com.example.wafi.Repository;

import com.example.wafi.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Users findUsersById(Integer id);
    boolean existsByEmail(String email);

    @Query("select u from Users u where " +
            "(select count(s) from Subscriptions s where s.userId = u.id and s.status = 'ACTIVE') >= 3 " +
            "and (select coalesce(sum(s.charge), 0) from Subscriptions s where s.userId = u.id and s.status = 'ACTIVE') >= 400")
    List<Users> findVipUsers();

    @Query("select u from Users u where " +
            "(select count(i) from IssueReports i where i.userId = u.id and i.status = 'OPEN') >= 2")
    List<Users> findAtRiskUsers();

    @Query("select u from Users u where " +
            "(select count(s) from Subscriptions s where s.userId = u.id and s.status = 'ACTIVE') = 0")
    List<Users> findInactiveUsers();

    @Query("select u from Users u where " +
            "((select count(s) from Subscriptions s where s.userId = u.id and s.status = 'ACTIVE') < 3 " +
            "or (select coalesce(sum(s.charge), 0) from Subscriptions s where s.userId = u.id and s.status = 'ACTIVE') < 200) " +
            "and (select count(i) from IssueReports i where i.userId = u.id and i.status = 'OPEN') < 2 " +
            "and (select count(s) from Subscriptions s where s.userId = u.id and s.status = 'ACTIVE') > 0")
    List<Users> findRegularUsers();
}