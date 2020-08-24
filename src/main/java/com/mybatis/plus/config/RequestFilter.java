package com.mybatis.plus.config;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.mybatis.plus.config.exception.RRException;
import com.mybatis.plus.config.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * 描述: 请求过滤器 很多请求预处理都可以在这里做
 *
 * @author 官萧何
 * @date 2020/8/17 11:22
 * @version V1.0
 */
@Component
@WebFilter()
public class RequestFilter implements Filter {

	private static Log logger = LogFactory.getLog(RequestFilter.class);


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response2 = (HttpServletResponse) response;
		response2.setHeader("Access-Control-Allow-Origin","*");
		response2.setHeader("Access-Control-Allow-Credentials", "true");
		response2.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response2.setHeader("Access-Control-Max-Age", "5000");
		response2.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since," +
				" Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,Authorization,Token,tokenId,tokenid");
		HttpServletRequest request2 = (HttpServletRequest) request;
		chain.doFilter(request, response2);
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
