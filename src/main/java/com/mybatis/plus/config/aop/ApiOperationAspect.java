

package com.mybatis.plus.config.aop;

import com.google.gson.Gson;
import com.mybatis.plus.sys.entity.Log;
import com.mybatis.plus.sys.entity.User;
import com.mybatis.plus.sys.service.ILogService;
import com.mybatis.plus.sys.service.IUserService;
import com.mybatis.plus.utils.IPUtils;
import com.mybatis.plus.utils.oauth.HttpContextUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 *
 * 描述: 针对swagger注解的日志处理，切面处理类
 *
 * @author 官萧何
 * @date 2020/8/17 16:15
 * @version V1.0
 */
@Slf4j
@Aspect
@Component
public class ApiOperationAspect {
	@Autowired
	private ILogService logService;

	@Autowired
	IUserService userService;

	@Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		String params = "";
		//请求的参数
		Object[] args = point.getArgs();
		try{
			params = new Gson().toJson(args);
		}catch (Exception e){
			log.info("获取参数信息失败");
		}
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveLog(point, time, params);

		return result;
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time, String params) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		Log sysLog = new Log();
		ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
		if(apiOperation != null){
			//注解上的描述
			sysLog.setOperation(apiOperation.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		//用户名
		String username = "";
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		if (null != principal) {
			// 处理管理系统用户日志信息
			username = principal.getUsername();
		}
		sysLog.setParams(params);
		sysLog.setUsername(username);
		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		//保存系统日志
		logService.save(sysLog);

		log.info("====接口 {} 执行时间：{} 毫秒", sysLog.getMethod(), time);
	}
}
