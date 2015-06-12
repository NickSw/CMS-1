package ua.demo.util;

import ua.demo.entity.User;

/**
 * singleton that defines security policy.
 * It defines which url can be access by certain user with definite role.
 *
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
            if (uri.equals("/admin/userupdate")) return false;

            if (user.getRole().equals("author")){
                if (uri.equals("/admin/postdelete")) return false;
                if (uri.equals("/admin/tag")) return false;
                if (uri.equals("/admin/tags")) return false;
                if (uri.equals("/admin/tagupdate")) return false;
                if (uri.equals("/admin/tagdelete")) return false;
            }

            if (user.getRole().equals("corrector")){
                if (uri.equals("/admin/postdelete")) return false;
                if (uri.equals("/admin/tag")) return false;
                if (uri.equals("/admin/tags")) return false;
                if (uri.equals("/admin/tagupdate")) return false;
                if (uri.equals("/admin/tagdelete")) return false;
            }
        }


        return true;
    }
}
