package com.example.flascash.controller;

import com.example.flascash.entities.User;
import com.example.flascash.service.FriendshipService;
import com.example.flascash.service.SessionService;
import com.example.flascash.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FriendController {
    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @GetMapping(path = "/friends/add")
    public String showAddFriends(Model model) {
        User user = sessionService.sessionUser();

        List<User> usersFound = userService.findFriends(user.getId());

        model.addAttribute("friends", usersFound);
        return "add-friends";
    }

    @PostMapping(path = "/friends/addKnown")
    public String addKnownFriends(@RequestParam("friendUsername") String friendUsername, Model model) {
        try {
            User user = sessionService.sessionUser();
            User userFriend = userService.findUserByUsername(friendUsername);

            friendshipService.createFriendship(user.getId(), userFriend.getId());
            model.addAttribute("success", true);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "You can not add this user");
            return "add-friends";
        }
    }

    @PostMapping(path = "/friends/add/{friendId}")
    public String addFriend(@PathVariable("friendId") Long friendId, Model model) {
        try {
            User user = sessionService.sessionUser();
            friendshipService.createFriendship(user.getId(), friendId);

            model.addAttribute("success", true);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "redirect:/add-friends";
        }
    }
}
