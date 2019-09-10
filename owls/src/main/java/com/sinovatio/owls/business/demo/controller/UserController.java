package com.sinovatio.owls.business.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.sinovatio.owls.base.BaseController;
import com.sinovatio.owls.business.demo.entity.User;
import com.sinovatio.owls.business.demo.service.UserService;

@Controller
public class UserController extends BaseController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired 
	private MessageSource messageSource;

	@RequestMapping(value = "/demo/user/{id}", method = RequestMethod.GET)
	public String findOneUserById(Model model, @PathVariable("id") Long id) {
		User user = userService.findUserById(id);
		model.addAttribute("user", user);
		return "/demo/user";
	}

	@RequestMapping(value = "/demo/test/log", method = RequestMethod.GET)
	public @ResponseBody Object testLog(HttpServletRequest request, HttpServletResponse resp) {
		logger.trace("trace!just test it!");
		logger.debug("debug!just test it!");
		logger.info("info!just test it!");
		logger.warn("warn!just test it!");
		logger.error("error!just test it!");
		return "OK";
	}

	/**
	 * 调用存储过程，并获取返回值
	 * @return
	 */
	@RequestMapping(value = "/demo/procedure", method = RequestMethod.GET)
	public @ResponseBody Object procedure(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("i_sequence", "123");
		userService.procedure(map);
		String data1 = map.get("o_error1");
		String data2 = map.get("o_error2");
		logger.info("o_error1:"+data1);
		logger.info("o_error2:"+data2);
		return "procedure OK";
	}
	
	/**
	 * 调用存储过程，没有返回值
	 * @return
	 */
	@RequestMapping(value = "/demo/procedure1", method = RequestMethod.GET)
	public @ResponseBody Object procedure1(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "457");
		map.put("username", "lidongsong");
		userService.procedure1(map);
		return "procedure1 OK";
	}
	
	@RequestMapping(value = "/demo/test/i18n", method = RequestMethod.GET)
	public String testI18n(HttpServletRequest request, HttpServletResponse resp) {
		Locale locale = RequestContextUtils.getLocale(request);
		String msg = this.messageSource.getMessage("demo.hello", null, locale);
		logger.info("current language:" + locale.getLanguage());
		logger.info("demo.hello:" + msg);
		
		return "/demo/i18n";
	}


	@RequestMapping(value = "/demo/testParam/{name}/{age}", method = RequestMethod.GET)
	public @ResponseBody Object testParam(@PathVariable("name") String name,@PathVariable("age")String age){
		logger.trace("test param:" + name);
		return "OK";
	}


	@RequestMapping(value = "/demo/testParam", method = RequestMethod.POST)
	public @ResponseBody Object testPost(String account, String ip){
		logger.trace("test POST:" + account);
		return "OK";
	}

	@RequestMapping(value = "/demo/testGet", method = RequestMethod.GET)
	public @ResponseBody Object testGet(@RequestParam(value="name") String name,@RequestParam(value="age",required=false,defaultValue="0") String age){
		logger.trace("test param:" + name);
		return "OK";
	}
}
