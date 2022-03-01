package com.example.logindemo.exception.tokenrefresh;

import com.example.logindemo.exception.base.BaseException;
import com.example.logindemo.exception.modules.Modules;
import com.example.logindemo.exception.responsecode.MgrResponseCode;

/**
 * 處理token refresh接口異常類
 *
 * @author chris
 * */
public class TokenRefreshException extends BaseException {

    private static final long serialVersionUID = 1L;

    public TokenRefreshException(MgrResponseCode mgrResponseCode, String token) {
        super(Modules.REFRESH_TOKEN,mgrResponseCode,new Object[]{token},mgrResponseCode.getMessage());
    }
}
