package utils;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ognl.OgnlValueStack;

public class RequestObserver {
	private HttpServletRequest request;

	public RequestObserver(HttpServletRequest request) {
		this.request = request;
	}

	public void observe() {
		String name, pvalue;
		Object avalue;
		Enumeration enum1;

		System.out.println("/***************** Request Observer (Author: Alex Nie)**************/");

		// observe Request Header
		enum1 = request.getHeaderNames();
		System.out.println("Request Header:");
		while (enum1.hasMoreElements()) {
			name = (String) enum1.nextElement();
			pvalue = request.getHeader(name);
			System.out.println("  " + name + " ---- " + pvalue);
		}
		enum1 = request.getParameterNames();

		// observe Request Parameters
		System.out.println("Request Parameters:");
		while (enum1.hasMoreElements()) {
			name = (String) enum1.nextElement();
			pvalue = request.getParameter(name);
			System.out.println("  " + name + " ---- " + pvalue);
		}
		enum1 = request.getAttributeNames();

		// observe Request Attributes
		System.out.println("Request Attributes:");
		while (enum1.hasMoreElements()) {
			name = (String) enum1.nextElement();
			avalue = request.getAttribute(name);
			System.out.println("  " + name + " ---- " + avalue);

			// observe OgnlValueStack bind by Struts 2
			if (avalue instanceof OgnlValueStack) {
				avalue = (OgnlValueStack) avalue;
				Map<String, Object> m = ((OgnlValueStack) avalue).getContext();
				System.out.println("  >> OgnlValueStack:");

                for (Map.Entry<String, Object> entry : m.entrySet())
                {
                    System.out.println("    " + entry.getKey() + "----" + entry.getValue());
                }
/*                Iterator it = m.keySet().iterator();
				Object key;
				while (it.hasNext()) {
					key = it.next();
					System.out.println("        " + key + " ---- " + m.get(key));
				}*/
			}
		}

		// observe Request Session
		HttpSession session = request.getSession(false);
		System.out.println("session: " + session);
		if (session != null) {
			System.out.println("  sessionId: " + session.getId());
			enum1 = session.getAttributeNames();
			System.out.println("Session Attributes:");
			while (enum1.hasMoreElements()) {
				name = (String) enum1.nextElement();
				avalue = session.getAttribute(name);
				System.out.println("  " + name + " ---- " + avalue);
			}
		}
		System.out.println("/***************** End of Request Observer (Author: Alex Nie)**************/");

	}

}