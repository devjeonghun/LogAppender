package com.lj.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {

    private final String listViewName;

    @Autowired
    public LogController(@Value("${viewname.chatroom.list}") String listViewName) {
        this.listViewName = listViewName;
    }

    @GetMapping("/")
    public String main(Model model) {
        return listViewName;
    }

}
