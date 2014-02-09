package de.minestar.AdminStuff.webserver.pagehandler.main;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import de.minestar.AdminStuff.webserver.exceptions.LoginInvalidException;
import de.minestar.AdminStuff.webserver.pagehandler.AbstractHTMLHandler;
import de.minestar.AdminStuff.webserver.template.Template;
import de.minestar.AdminStuff.webserver.template.TemplateHandler;
import de.minestar.AdminStuff.webserver.template.TemplateReplacement;
import de.minestar.AdminStuff.webserver.units.AuthHandler;
import de.minestar.AdminStuff.webserver.units.UserData;

public abstract class CustomPageHandler extends AbstractHTMLHandler {

    protected final Template template;
    protected final TemplateReplacement rpl_navigation, rpl_user, rpl_token;

    protected CustomPageHandler(boolean needsLogin, Template template) {
        super(needsLogin);

        // set template
        this.template = template;

        // create replacements
        this.rpl_user = new TemplateReplacement("USERNAME");
        this.rpl_token = new TemplateReplacement("TOKEN");
        if (needsLogin) {
            this.rpl_navigation = new TemplateReplacement("NAVIGATION", TemplateHandler.getTemplate("tpl_navi_on").getString());
        } else {
            this.rpl_navigation = new TemplateReplacement("NAVIGATION", TemplateHandler.getTemplate("tpl_navi_off").getString());
        }
    }

    @SuppressWarnings("unchecked")
    protected final void updateReplacements(HttpExchange http) throws LoginInvalidException {
        if (this.needsLogin()) {
            Map<String, String> params = (Map<String, String>) http.getAttribute("parameters");
            String userName = params.get("username");
            String token = params.get("token");

            if (userName != null && token != null && AuthHandler.isUserLoginValid(userName, token)) {
                // get userdata
                UserData user = AuthHandler.getUser(userName);

                // update replacements...
                this.rpl_user.setValue(userName);
                this.rpl_token.setValue(user.getToken());
            } else {
                throw new LoginInvalidException();
            }
        }
    }

}