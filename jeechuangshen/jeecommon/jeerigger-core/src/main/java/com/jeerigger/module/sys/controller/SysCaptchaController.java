package com.jeerigger.module.sys.controller;

import com.google.code.kaptcha.Producer;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.support.resolver.annotation.SingleRequestBody;
import com.jeerigger.frame.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@Api(value = "验证码", tags = "验证码")
public class SysCaptchaController extends BaseController {
    @Resource
    private Producer captchaProducer;

    @RequestMapping(value = "/validCode.jpg", method = RequestMethod.GET)
    @ApiOperation(value = "验证码生成", notes = "验证码获取")
    public void getCaptchaCode(HttpServletResponse response) throws IOException {
        ServletOutputStream servletOutputStream = null;
        try {
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            //生成验证码文本
            String kaptchaCode = captchaProducer.createText();
            ShiroUtil.setKaptchaCode(kaptchaCode);
            //利用生成的字符串构建图片
            BufferedImage bufferedImage = captchaProducer.createImage(kaptchaCode);
            servletOutputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", servletOutputStream);
            servletOutputStream.flush();
        } catch (Exception ex) {
            throw new FrameException("获取验证码发生异常！");
        } finally {
            servletOutputStream.close();
        }
    }

    @RequestMapping(value = "/validCode", method = RequestMethod.POST)
    @ApiOperation(value = "效验验证码", notes = "效验验证码")
    public ResultData ValidatorCaptchaCode(@SingleRequestBody(value = "validCode") String validCode) throws IOException {
        if (StringUtil.isNotEmpty(validCode)) {
            if (validCode.equals(ShiroUtil.getKaptchaCode())) {
                return this.success();
            }
        }
        return this.failed(ResultCodeEnum.ERROR_KAPTCHA_WRONG);
    }
}
