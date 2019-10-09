package com.sinovatio.middle.demo;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/person")
public class PersonController {

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    /**请求地址: localhost:8080/person/show?name=zhang&age=23 请求方式是get*/
    private String show(@RequestParam("name") String name, @RequestParam("age") int age, ModelMap map) {   /**@RequestParam("name")绑定请求地址中的name到参数name中 ModelMap map 存放返回内容*/
        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        map.addAttribute("person", person);
        return "demo/person";    /**返回的是显示数据的html的文件名*/
    }

}