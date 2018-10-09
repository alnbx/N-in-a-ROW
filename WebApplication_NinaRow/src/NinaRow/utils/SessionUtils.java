package NinaRow.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getAttribute (HttpServletRequest request, String attrName) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(attrName) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static boolean isSessionValid(HttpServletRequest request) {
        return request.getSession(false) != null;
    }
}
