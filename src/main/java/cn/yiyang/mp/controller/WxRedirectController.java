package cn.yiyang.mp.controller;

import cn.yiyang.common.utils.ResultBean;
import cn.yiyang.mp.config.WxMpConfiguration;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Action;

/**
 * @author Edward
 */
@AllArgsConstructor
@RestController
@RequestMapping("/wx/redirect")
public class WxRedirectController {
    private final WxMpService wxService;

    @RequestMapping("/greet")
    public String greetUser(@PathVariable String appid, @RequestParam String code, ModelMap map) {
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);
            WxMpUser user = wxService.oauth2getUserInfo(accessToken, null);
            map.put("user", user);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return "greet_user";
    }

    @GetMapping("/getUrl")
    public ResultBean getUrl() {
        String url = "https://www.buchang.com/SixPromotionWeb/?fuId=231321321";
        String path = wxService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        return ResultBean.success(path);
    }
}
