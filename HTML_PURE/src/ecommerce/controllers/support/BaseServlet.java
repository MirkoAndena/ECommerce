package ecommerce.controllers.support;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import ecommerce.SessionKeys;
import ecommerce.database.ConnectionBuilder;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected ServletContext context;
	protected Connection connection;
	protected TemplateEngine templateEngine;
	private Thymeleaf thymeleaf;
    
    public BaseServlet() {
        super();
    }

    @Override
	public void init() throws ServletException {
    	context = getServletContext();
		connection = ConnectionBuilder.create(context);
		templateEngine = initTemplateEngine(context);
		OnInit();
	}
    
    private static TemplateEngine initTemplateEngine(ServletContext servletContext) {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		return engine;
	}
    
    protected abstract void OnInit();
    
    @Override
    public void destroy() {
    	ConnectionBuilder.close(connection);
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try { get(request, response); }
		catch (FatalException e) {
			System.err.println(e.toString());
			redirectToLogin(request, response);
		}
	}
    
    public abstract void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FatalException;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try { post(request, response); }
		catch (FatalException e) {
			System.err.println(e.toString());
			redirectToLogin(request, response);
		}
	}
	
    public abstract void post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FatalException;

	protected void redirectToLogin(HttpServletRequest request, HttpServletResponse response) {
    	try {
			response.sendRedirect(request.getContextPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public Thymeleaf getThymeleaf() {
    	this.thymeleaf = new Thymeleaf(context, templateEngine);
    	return this.thymeleaf;
    }
    
    @SuppressWarnings("unchecked")
	public <T> T getAttribute(HttpServletRequest request, SessionKeys key) {	
    	try {
	    	HttpSession session = request.getSession(false);
	    	if (session == null) return null;    
	    	return (T) session.getAttribute(key.toString());
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public Integer getUserId(HttpServletRequest request) {
    	return getAttribute(request, SessionKeys.User);
    }
}
