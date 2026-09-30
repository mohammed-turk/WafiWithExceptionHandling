package com.example.wafi.Service;

import com.example.wafi.API.APIException;
import com.example.wafi.Model.IssueReports;
import com.example.wafi.Model.Subscriptions;
import com.example.wafi.Model.Users;
import com.example.wafi.Repository.IssueReportsRepository;
import com.example.wafi.Repository.SubscriptionsRepository;
import com.example.wafi.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final IssueReportsRepository issueReportsRepository;

    private Users getUserOrThrow(Integer userId){
        Users user = usersRepository.findUsersById(userId);
        if (user == null)
            throw new APIException("user not found");
        if (user.getId() != null)
            throw new APIException("id is auto generated, do not send it");
        return user;
    }

    public List<Users> get(){
        return usersRepository.findAll();
    }

    public void add(Users user){
        if (usersRepository.existsByEmail(user.getEmail()))
            throw new APIException("user email already exist");
        usersRepository.save(user);
    }

    public void update(Integer userId, Users user){
        Users oldUser = getUserOrThrow(userId);

        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setPhoneNumber(user.getPhoneNumber());
        usersRepository.save(oldUser);

    }

    public void delete(Integer userId){
        Users user = getUserOrThrow(userId);
        usersRepository.delete(user);

    }

    public Integer getSubscriptionCount(Integer userId) {
        getUserOrThrow(userId);
        return subscriptionsRepository.findSubscriptionsByUserId(userId).size();
    }

    public List<IssueReports> getUserIssueReports(Integer userId) {
        getUserOrThrow(userId);
        return issueReportsRepository.findIssueReportsByUserId(userId);
    }

    public List<Users> getVipUsers() {
        return usersRepository.findVipUsers();
    }

    public List<Users> getRegularUsers() {
        return usersRepository.findRegularUsers();
    }

    public List<Users> getAtRiskUsers() {
        return usersRepository.findAtRiskUsers();
    }

    public List<Users> getInactiveUsers() {
        return usersRepository.findInactiveUsers();
    }
}