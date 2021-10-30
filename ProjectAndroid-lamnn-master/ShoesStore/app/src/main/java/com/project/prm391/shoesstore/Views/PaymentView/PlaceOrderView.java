package com.project.prm391.shoesstore.Views.PaymentView;

import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.PaymentMethod;

/**
 * Created by duytq on 3/24/2018.
 */

public interface PlaceOrderView {
    String getInputName();

    void setInputName(String name);

    String getInputEmail();

    void setInputEmail(String email);

    String getInputPhoneNumber();

    void setInputPhoneNumber(String phoneNumber);

    String getInputAddress();

    void setInputAddress(String address);

    String getInputMessage();

    void setInputMessage(String message);

    PaymentMethod getInputPaymentMethod();

    void setInputPaymentMethod(PaymentMethod paymentMethod);

    void notifyInputNameError(String error);

    void notifyInputEmailError(String error);

    void notifyInputPhoneNumberError(String error);

    void notifyInputAddressError(String error);

    void notifyInputMessageError(String error);

    void notifyPlaceOrderSuccessful(Order order);

    void notifyPlaceOrderFailed(String error);

    void notifyException(Exception e);

    void showProgressDialog();

    void hideProgressDialog();

    void finishActivity(boolean success);
}
