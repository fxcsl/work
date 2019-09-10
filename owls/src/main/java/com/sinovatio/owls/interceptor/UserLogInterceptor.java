package com.sinovatio.owls.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Title: UserLogFilter.java
 * @Package net.bwda.fish.web.listener
 * @Description: TODO(用于记录用户操作日志)
 * @version V1.0
 */
@Aspect
@Component
@Order(1)
public class UserLogInterceptor {

	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Properties properties = null;
	
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	// 申明一个切点 里面是 execution表达式
	@Pointcut("execution(public * com.sinovatio.owls.business.demo.controller.*.*(..))")
	private void controllerAspect() {
	}

	// 请求method前
	@Before(value = "controllerAspect()")
	public void methodBefore(JoinPoint joinPoint) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		// 打印请求内容
		log.info("请求地址:" + request.getRequestURL().toString());
		log.info("请求方式:" + request.getMethod());
		log.info("请求类方法:" + joinPoint.getSignature());
		
        Enumeration<String> enu = request.getParameterNames();

        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
    		log.info(paraName + ": " + request.getParameter(paraName));
        }
	}
	

	// 在方法执行完结后打印返回内容
	@AfterReturning(returning = "o", pointcut = "controllerAspect()")
	public void methodAfterReturing(Object o) throws IOException {
		
		String resulrName = getResultName();
		log.info("返回内容");
	}
	
	private Properties getProperites(HttpServletRequest request) {
		if (this.properties == null) {
			Properties pro = new Properties();

			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource("classpath:/base-logKeyword.properties");
				is = resource.getInputStream();
				pro.load(is);
			} catch (IOException ex) {
			} finally {
				IOUtils.closeQuietly(is);
			}
			this.properties = pro;
		}
		return this.properties;
	}
	
	private String getResultName() throws IOException {
		
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		
        Properties pro = getProperites(request);
		String url = request.getRequestURL().toString().substring(request.getRequestURL().toString().indexOf("/demo"), request.getRequestURL().toString().length());
		String result = "";
		String resultName = "";
		String[] list = url.split("/");
		for (String s : list) {
			if (s.length() > 20) {
				s = "NUM";
			}
			try {
				Integer.parseInt(s);
				s = "NUM";
			} catch (Exception e) {

			} finally {
				if (s.length() != 0) {
					result = result + "/" + s;
				}
			}
		}
		String str = pro.getProperty(result + "[" + request.getMethod() + "]");
		if (str != null && str.length() > 0 && !"".equals(str)) {
			resultName = new String(str.getBytes("ISO-8859-1"), "utf-8");
			log.info("读取配置文件内容：" + resultName);
		}
		return resultName;
		
	}
}
