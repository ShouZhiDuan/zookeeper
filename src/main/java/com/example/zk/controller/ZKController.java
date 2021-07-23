package com.example.zk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
@RestController
@RequestMapping("/param")
public class ZKController {

    @GetMapping("/test")
    public Object testParams(@RequestParam("name") String name) {
        System.out.println(name);
        return name;
    }

}
