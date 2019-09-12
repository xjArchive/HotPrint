package com;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.JarScanFilter;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import com.print.facade.PrintServiceFacade;
import com.print.task.PrintTaskManager;
import com.print.web.PrintServlet;
 
public class App {
	protected static final Logger log = LogManager.getLogger();
	private static int port = 8088;
	private static String contextPath ="/Print";
	private static List<Class<? extends HttpServlet>> servletlist = new ArrayList<Class<? extends HttpServlet>>();
	public static void main(String[] args) throws Exception {
		beforeInit();
		Tomcat tomcat = new Tomcat();
		afterInit( tomcat );
		tomcat.start();
		tomcat.getServer().await();
	}
	 


	private static void beforeInit() {
		Map<String,String> param = loadInitConfig();//加载配置。
		setInitParameter(param);//这是初始化参数。
		PrintServiceFacade.setInitParameter(param);//
		PrintServlet.json_body_charset = param.get("request.body.charset"); 
		System.out.println( param );
		PrintTaskManager.init();
	}


	private static void afterInit(Tomcat tomcat) throws Exception {
		tomcat.setPort(port);//端口。可修改
		new File("temp").mkdir();
		StandardContext ctx = (StandardContext) 
				tomcat.addWebapp(contextPath,//Web应用名。
						 new File("temp")  .getAbsolutePath()
						);
		initCosFilter(ctx);
		registerServlet();
		initServletConfig(tomcat,ctx);
		ctx.setAddWebinfClassesResources(true);
	}

	private static Map<String, String> loadInitConfig() {
		Map<String,String> param = new HashMap<String,String>();
		try (BufferedReader bufr = new BufferedReader( new InputStreamReader(new FileInputStream("conf/conf.ini"), "UTF-8"));){
			while(true) {
				String line = bufr.readLine();
				if(line == null )break;
				if(line.startsWith("#"))continue;//跳过注释。
				if(line.trim().equals(""))continue;
				StringTokenizer stk = new StringTokenizer(line,"=");
				param.put(stk.nextToken().trim(), stk.nextToken().trim());
			}
		}catch (Exception e) {
			log.error("conf.ini 参数初始化失败。先检查文件是否存在。再检查文件目录位置是否正确。最后检查参数是否正确。");
		}
		return param;
		
	}
	
	public static void setInitParameter(Map<String,String> param) {
		 if(param!= null && !param.isEmpty()) {
			String port_str = param.get("server.port");
			if(port_str != null) {
				port = Integer.parseInt(port_str);
			}
			String context = param.get("server.contextPath");
			if(context != null) {
				 contextPath = context;
			}
		 }
	}
	
	/**
	 * 用于注册Servlet
	 */
	private static void registerServlet() {
		 servletlist.add(PrintServlet.class);
		 //servletlist.add(UserServlet.class);
		 //servletlist.add(Record.class);
		 
	}
 
	/**
	 * 未调用。如果需求可以在此处使用。
	 * 过滤器初始化  
	 * @param ctx
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static void initCosFilter( StandardContext ctx  ) throws InstantiationException, IllegalAccessException {
		FilterDef filterde = new FilterDef();
		Filter ft = new Filter() {
			@Override
			public void init(FilterConfig arg0) throws ServletException { }
			
			@Override
			public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
					throws IOException, ServletException {
				 HttpServletResponse rp = (HttpServletResponse) arg1;
				 rp.setHeader("Access-Control-Allow-Origin", "*");  
				 rp.setHeader("Access-Control-Allow-Methods", "*");  
				 rp.setHeader("Access-Control-Allow-Headers", "*");
				arg2.doFilter(arg0, arg1);
			}
			
			@Override
			public void destroy() { }
		};
		filterde.setFilter(ft);
		filterde.setFilterName(ft.getClass().getName());
		ctx.addFilterDef(filterde);
		FilterMap filter1mapping = new FilterMap();
		filter1mapping.setFilterName(ft.getClass().getName());
		filter1mapping.addURLPattern("/*");
		ctx.addFilterMap(filter1mapping);
		 
	}
	
	
	private static void initServletConfig(Tomcat tomcat,StandardContext ctx  ) throws InstantiationException, IllegalAccessException {
	   	 for(Class<? extends HttpServlet> cls :servletlist) {
	   		 String name = cls.toString();
	   		Wrapper rp = tomcat.addServlet(ctx, name, cls.newInstance());
	   		String []vs = cls.getAnnotation(WebServlet.class).value();
	   		for(String mapping : vs) {
	   			rp.addMapping(mapping );
	   		}
	   		
	   	 }
	}
}
