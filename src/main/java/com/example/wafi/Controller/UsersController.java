package com.example.wafi.Controller;

import com.example.wafi.API.APIResponse;
import com.example.wafi.Model.IssueReports;
import com.example.wafi.Model.Users;
import com.example.wafi.Service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        List<Users> users = usersService.get();
        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Users user){
        usersService.add(user);
        return ResponseEntity.status(200).body(new APIResponse("user added successfully"));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> update(@PathVariable Integer userId, @RequestBody @Valid Users user){
        usersService.update(userId, user);
        return ResponseEntity.status(200).body(new APIResponse("updated successfully"));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable Integer userId){
        usersService.delete(userId);
        return ResponseEntity.status(200).body(new APIResponse("deleted successfully"));

    }

    @GetMapping("/subscriptions-count/{userId}")
    public ResponseEntity<?> getSubscriptionCount(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(usersService.getSubscriptionCount(userId));
    }

    @GetMapping("/issue-reports/{userId}")
    public ResponseEntity<?> getIssueReports(@PathVariable Integer userId){
        List<IssueReports> reports = usersService.getUserIssueReports(userId);
        return ResponseEntity.status(200).body(reports);
    }

    @GetMapping("/vip")
    public ResponseEntity<?> getVipUsers(){
        List<Users> users = usersService.getVipUsers();
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/regular")
    public ResponseEntity<?> getRegularUsers(){
        List<Users> users = usersService.getRegularUsers();
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/at-risk")
    public ResponseEntity<?> getAtRiskUsers(){
        List<Users> users = usersService.getAtRiskUsers();
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/inactive")
    public ResponseEntity<?> getInactiveUsers(){
        List<Users> users = usersService.getInactiveUsers();
        return ResponseEntity.status(200).body(users);
    }
}
