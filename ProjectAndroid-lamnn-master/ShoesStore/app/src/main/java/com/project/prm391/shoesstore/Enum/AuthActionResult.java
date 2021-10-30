package com.project.prm391.shoesstore.Enum;

import com.project.prm391.shoesstore.R;

/**
 * Created by nguyen on 3/21/2018.
 */

public enum AuthActionResult {
    SUCCESSFUL(R.string.msg_AuthActionResult_SUCCESSFUL),
    UNKNOWN_ERROR(R.string.msg_AuthActionResult_UNKNOWN_ERROR),
    SERVER_ERROR(R.string.msg_AuthActionResult_SERVER_ERROR),
    INVALID_PASSWORD(R.string.msg_AuthActionResult_INVALID_PASSWORD),
    INVALID_EMAIL(R.string.msg_AuthActionResult_INVALID_EMAIL),
    INCORRECT_PASSWORD(R.string.msg_AuthActionResult_INCORRECT_PASSWORD),
    INVALID_OR_EXPIRED_TOKEN(R.string.msg_AuthActionResult_INVALID_OR_EXPIRED_TOKEN),
    SIGN_IN_EMAIL_NOT_EXIST(R.string.msg_AuthActionResult_SIGN_IN_EMAIL_NOT_EXIST),
    REGISTER_EMAIL_ALREADY_EXIST(R.string.msg_AuthActionResult_REGISTER_EMAIL_ALREADY_EXIST),
    SIGN_IN_METHOD_NOT_LINKED(R.string.msg_AuthActionResult_SIGN_IN_METHOD_NOT_LINKED),
    SIGN_IN_METHOD_ALREADY_LINKED(R.string.msg_AuthActionResult_SIGN_IN_METHOD_ALREADY_LINKED),
    ACTION_REQUIRE_SIGN_IN(R.string.msg_AuthActionResult_ACTION_REQUIRE_SIGN_IN);

    private final int resourceId;

    AuthActionResult(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }
}
