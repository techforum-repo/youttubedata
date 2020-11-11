package com.aspectj.core.aspects;


import java.util.Arrays;
import java.util.Enumeration;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@Aspect
public class LoggerAspect {
	
	@Pointcut("execution(* com.aspectj.core.servlets.*.doGet(..))")	
    public void doGet() {
    } 
	
	
	 @Around("doGet()")
    public void logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	 Logger logger = LoggerFactory.getLogger(this.getClass());
         
        long start = System.currentTimeMillis();
        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Method " + className + "." + methodName + " ()" + " execution time : "+ elapsedTime + " ms");        
           
        } catch (IllegalArgumentException e) {            
            throw e;
        }
    }
		 
	

    @Before("doGet() && args(req,..)")	
    public void logRequestHeader(JoinPoint joinPoint, SlingHttpServletRequest req) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("Method Started...."+joinPoint.getSignature().getName()+"()");
        
        logger.info("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        
        if (null != req) {
           Enumeration<String> headerNames = req.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = req.getHeader(headerName);
                logger.info("Header Name: " + headerName + " Header Value : " + headerValue);
            }           
        }
    }
 

    @After("doGet() && args(..,resp)")
    public void addCustomHeader(JoinPoint joinPoint, SlingHttpServletResponse resp) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        resp.addHeader("customHeader", "customValue");
        logger.info(" Method Exited..."+joinPoint.getSignature().getName()+"()");
    }
    
	
   
    

}