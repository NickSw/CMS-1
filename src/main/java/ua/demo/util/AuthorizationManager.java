package ua.demo.util;

import ua.demo.entity.User;

/**
 * Created by Sergey on 07.06.2015.
 */
public class AuthorizationManager {
    private static AuthorizationManager ourInstance = new AuthorizationManager();

    public static AuthorizationManager getInstance() {
        return ourInstance;
    }

    private AuthorizationManager() {
    }

    public boolean isUserAuthorized(User user,String uri){
        if (!user.getRole().equals("admin")) {
            //only admin can control users
            if (uri.equals("/admin/users")) return false;
            if (uri.equals("/admin/user")) return false;
            if (uri.equals("/admin/userdelete")) return false;

            if (user.getRole().equals("author")){
                if (uri.equals("/admin/postdelete")) return false;
            }

            if (user.getRole().equals("corrector")){
                if (uri.equals("/admin/postdelete")) return false;
            }
        }


        return true;
    }
}
