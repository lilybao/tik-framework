package com.jeerigger.module.sys.controller;

import com.jeerigger.common.annotation.Log;
import com.jeerigger.common.enums.LogTypeEnum;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.frame.support.util.UserAgentUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.vo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(value = "用户登录登出", tags = "用户登录登出")
public class SysLoginController extends BaseController {
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @Log(logType = LogTypeEnum.LOGIN, logTitle = "用户登录")
    public ResultData userLogin(HttpServletRequest request, @RequestBody LoginUser loginUser) {
        if (StringUtil.isEmpty(loginUser.getLoginName())) {
            return this.failed(ResultCodeEnum.ERROR_LOGIN_EXCEPTION, "登录名不能为空!");
        }
        if (StringUtil.isEmpty(loginUser.getLoginPwd())) {
            return this.failed(ResultCodeEnum.ERROR_LOGIN_EXCEPTION, "登录密码不能为空!");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(loginUser.getLoginName(), loginUser.getLoginPwd(), UserAgentUtil.getUserIp(request));
        token.setRememberMe(loginUser.isRememberMe());
        ShiroUtil.getSubject().login(token);
        if (ShiroUtil.getSubject().isAuthenticated()) {
            return this.success(ShiroUtil.getUserData());
        } else {
            return this.failed(ResultCodeEnum.ERROR_LOGIN_EXCEPTION);
        }
    }

    @ResponseBody
    @RequiresPermissions("user")
    @ApiOperation(value = "用户退出")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Log(logType = LogTypeEnum.EXIT, logTitle = "用户退出")
    public ResultData userLogout() {
        ShiroUtil.logout();
        return this.success();
    }
}
