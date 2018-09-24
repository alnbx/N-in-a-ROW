package NinaRow.utils;

import NinaRow.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getAttribute (HttpServletRequest request, String attrName) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(attrName) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void setAttribute (HttpServletRequest request, String attrName, Object val) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(attrName, val);
        }
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
