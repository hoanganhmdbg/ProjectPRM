package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Enum.InputValidationResult;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IReviewService;
import com.project.prm391.shoesstore.Utils.InputValidation;
import com.project.prm391.shoesstore.Views.ProductDetailView.ReviewView;

import java.util.Date;

/**
 * Created by lamtu on 2018-03-24.
 */

public class ReviewPresenter {
    private static final String TAG = ReviewPresenter.class.getName();

    private final ReviewView view;
    private final IReviewService reviewService;
    private final Resources resources;
    private ProductReview review;

    public ReviewPresenter(ReviewView view) {
        this.view = view;
        this.reviewService = ServiceManager.getInstance().getReviewService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public void loadReview(ProductReview review) {
        if (review == null) {
            view.finishActivity(false);
            return;
        }
        this.review = review;
        view.setInputRating(review.getRating());
        view.setInputTitle(review.getTitle());
        view.setInputContent(review.getContent());
    }

    public void submitReview() {
        if (validateTitleAndNotifyError() == InputValidationResult.VALID
                && validateContentAndNotifyError() == InputValidationResult.VALID) {
            String title = view.getInputTitle();
            String content = view.getInputContent();
            int rating = view.getInputRating();
            review.setTitle(title);
            review.setContent(content);
            review.setRating(rating);
            review.setPublishedTime(new Date());
            view.showSubmitReviewProgressDialog();
            reviewService.createOrUpdateProductReview(review).addOnCompleteListener(task -> {
                view.hideSubmitReviewProgressDialog();
                if (task.isSuccessful()) {
                    view.notifySubmitReviewSuccess();
                } else {
                    Exception e = task.getException();
                    Log.e(TAG, null, e);
                    view.notifySubmitReviewFailed(resources.getString(R.string.msg_submit_review_failed));
                }
            });
        }
    }

    public InputValidationResult validateTitleAndNotifyError() {
        String title = view.getInputTitle();
        InputValidationResult result = InputValidation.validateNonEmptyString(title);
        switch (result) {
            case VALID:
                view.notifyInputTitleError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputTitleError(resources.getString(R.string.msg_input_title_empty));
                break;
        }
        return result;
    }

    public InputValidationResult validateContentAndNotifyError() {
        String content = view.getInputContent();
        InputValidationResult result = InputValidation.validateNonEmptyString(content);
        switch (result) {
            case VALID:
                view.notifyInputContentError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputContentError(resources.getString(R.string.msg_input_content_empty));
                break;
        }
        return result;
    }
}
