package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;

/**
 * Status of the order.
 */
public class OrderStatus implements Serializable {
    /**
     * The order was accepted/cancelled by the seller.
     */
    private boolean accepted = true;
    /**
     * The order was paid/unpaid by the user.
     */
    private boolean paid = false;
    /**
     * The current delivery status of the order.
     */
    private DeliveryStatus deliveryStatus = DeliveryStatus.NOT_YET_DELIVERED;

    public OrderStatus() {
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    /**
     * Check whether the order is completed.
     * An order is considered to be completed if it was accepted, paid and delivered; or if it was cancelled.
     *
     * @return True if the order is completed, otherwise False.
     */
    public boolean isCompleted() {
        return !accepted || (paid && deliveryStatus == DeliveryStatus.DELIVERED);
    }
}
