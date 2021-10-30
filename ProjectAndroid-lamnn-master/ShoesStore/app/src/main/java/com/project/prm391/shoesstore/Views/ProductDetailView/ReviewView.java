package com.project.prm391.shoesstore.Views.ProductDetailView;

/**
 * Created by lamtu on 2018-03-11.
 */

public interface ReviewView {
    String getInputTitle();

    void setInputTitle(String title);

    String getInputContent();

    void setInputContent(String content);

    int getInputRating();

    void setInputRating(int rating);

    void notifyInputTitleError(String error);

    void notifyInputContentError(String error);

    void notifySubmitReviewSuccess();

    void notifySubmitReviewFailed(String error);

    void notifyException(Exception ex);

    void showSubmitReviewProgressDialog();

    void hideSubmitReviewProgressDialog();

    void finishActivity(boolean success);
}
