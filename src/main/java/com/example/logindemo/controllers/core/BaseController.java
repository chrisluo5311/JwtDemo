package com.example.logindemo.controllers.core;

import com.example.logindemo.Utils.ServletUtils;
import com.example.logindemo.Utils.TimeUtil;
import com.example.logindemo.common.constant.SessionConstants;
import com.example.logindemo.common.session.SessionEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 基礎 Controller 類
 * 獲取SessionEntity與用戶等等資訊
 *
 * @author chris
 * @date 2022/01/30
 * */
public class BaseController {

    /**
     * 將前台傳遞過來的日期格式的字符串，自動轉換為Date類型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 類型轉換
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(TimeUtil.parseDate(text));
            }
        });
    }

    /**
     * 獲取request
     */
    public HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 獲取response
     */
    public HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 獲取SessionEntity
     */
    public SessionEntity getSession() {
        return (SessionEntity) getRequest().getAttribute(SessionConstants.SESSION_ATTRIBUTE);
    }

    public HttpSession getHttpSession(){
        return getRequest().getSession();
    }

    /**
     * 獲取用戶名
     * */
    public String getUserName(){
        return getSession().getUserName();
    }

    /**
     * 獲取用戶id
     * */
    public Long getUserId(){
        return getSession().getUserId();
    }

}
