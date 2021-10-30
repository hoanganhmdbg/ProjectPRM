package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Shoes product details (not to be mistaken with inventory item)
 */
public class Product implements Serializable {
    /**
     * Set of product reviews.
     */
    private final Set<ProductReview> productReviews = new TreeSet<>();
    /**
     * Product ID.
     */
    private String id;
    /**
     * Product name.
     */
    private String name = "";
    /**
     * Product description.
     */
    private String description = "";
    /**
     * The time that the product is posted.
     */
    private Date postedTime = new Date();
    /**
     * Product category.
     */
    private Category category;
    /**
     * Product brand.
     */
    private Brand brand;
    /**
     * Product gender.
     */
    private Gender gender;
    /**
     * Product images.
     */
    private List<String> imageUrls = new ArrayList<>();
    /**
     * The size of the product.
     */
    private double size = 1;
    /**
     * Whether the product is available in the inventory.
     */
    private boolean available = false;
    /**
     * The current price of the product. It should be less than or equal to the original price.
     */
    private double currentPrice = 0;
    /**
     * The original price of the product.
     */
    private double originalPrice = 0;
    /**
     * The average ratings of the product, or null if there is no review.
     * This is a redundant field for easier product searching and sorting.
     */
    private Double averageRatings = null;

    public Product() {

    }

    public Product(String id) {
        this.id = id;
    }

    public Product(String id, String name, Category category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;

    }

    public static Comparator<Product> getComparator(SortField sortField, boolean ascending) {
        switch (sortField) {
            case CURRENT_PRICE:
                return (p1, p2) -> {
                    int compareAvailable = -Boolean.compare(p1.isAvailable(), p2.isAvailable());
                    if (compareAvailable != 0) {
                        return compareAvailable;
                    } else {
                        int result = Double.compare(p1.getCurrentPrice(), p2.getCurrentPrice());
                        return ascending ? result : -result;
                    }
                };
            case AVERAGE_RATINGS:
                return (p1, p2) -> {
                    int result = 0;
                    if (p1.getAverageRatings() == null) {
                        result = -1;
                    } else if (p2.getAverageRatings() == null) {
                        result = 1;
                    } else {
                        result = Double.compare(p1.getAverageRatings(), p2.getAverageRatings());
                    }
                    return ascending ? result : -result;
                };
            case DISCOUNT_RATE:
                return (p1, p2) -> {
                    int compareAvailable = -Boolean.compare(p1.isAvailable(), p2.isAvailable());
                    if (compareAvailable != 0) {
                        return compareAvailable;
                    } else {
                        int result = Double.compare(p1.getDiscountRate(), p2.getDiscountRate());
                        return ascending ? result : -result;
                    }
                };
            case POSTED_TIME:
                return (p1, p2) -> {
                    int result = 0;
                    if (p1.getPostedTime() == null) {
                        result = -1;
                    } else if (p2.getPostedTime() == null) {
                        result = 1;
                    } else {
                        result = p1.getPostedTime().compareTo(p2.getPostedTime());
                    }
                    return ascending ? result : -result;
                };
            default:
                return (p1, p2) -> {
                    int result = 0;
                    if (p1.getName() == null) {
                        result = -1;
                    } else if (p2.getName() == null) {
                        result = 1;
                    } else {
                        result = p1.getName().compareToIgnoreCase(p2.getName());
                    }
                    return ascending ? result : -result;
                };
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(Date postedTime) {
        this.postedTime = postedTime;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive number.");
        }
        this.size = size;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        if (currentPrice < 0) {
            throw new IllegalArgumentException("Price must not be negative number.");
        }
        this.currentPrice = currentPrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        if (originalPrice < 0) {
            throw new IllegalArgumentException("Price must not be negative number.");
        }
        this.originalPrice = originalPrice;
    }

    public Double getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(Double averageRatings) {
        if (averageRatings != null && (averageRatings < 1 || averageRatings > 5)) {
            throw new IllegalArgumentException("Average rating must be between 1 and 5.");
        }
        this.averageRatings = averageRatings;
    }

    public double getDiscountRate() {
        return currentPrice < originalPrice && originalPrice > 0 ? 1 - currentPrice / originalPrice : 0;
    }

    /**
     * Get all the product reviews.
     *
     * @return A read-only set of product reviews.
     */
    public Set<ProductReview> getProductReviews() {
        return new TreeSet<>(productReviews);
    }

    /**
     * Get product review of an user.
     *
     * @return The product review of that user, or null if the user has not made a review.
     */
    public ProductReview getProductReview(User user) {
        return productReviews.stream().filter(review -> review.getUser().equals(user)).findFirst().orElse(null);
    }

    /**
     * Get the set of reviewed users.
     *
     * @return The set of reviewed users.
     */
    public Set<User> getReviewedUsers() {
        return productReviews.stream().map(review -> review.getUser()).collect(Collectors.toSet());
    }

    /**
     * Check if the product has review by a specified user.
     *
     * @param user The {@link User} object.
     * @return True if the review by that user exists, otherwise False.
     */
    public boolean hasUserReviewed(User user) {
        return productReviews.stream().anyMatch(review -> review.getUser().equals(user));
    }

    /**
     * Add new review for the product with default rating = 3. If the user has already reviewed, nothing will happen,
     * and the existing product review will be returned.
     *
     * @param user The {@link User} object.
     * @return The newly added or existing product review.
     */
    public ProductReview addProductReview(User user) {
        return addProductReview(user, 3);
    }

    /**
     * Add new review for the product with specified rating. If the user has already reviewed, nothing will happen,
     * and the existing product review will be returned.
     *
     * @param user The {@link User} object.
     * @return The newly added or existing product review.
     */
    public ProductReview addProductReview(User user, int rating) {
        if (hasUserReviewed(user)) {
            return getProductReview(user);
        }
        ProductReview review = new ProductReview(this, user, rating);
        productReviews.add(review);
        updateAverageRatings();
        return review;
    }

    /**
     * Remove an existing product review.
     * If the product does not exist, nothing will happen.
     *
     * @param user The {@link User} object.
     * @return True if it previously exists in the reviews, otherwise False.
     */
    public boolean removeProductReview(User user) {
        boolean removed = productReviews.removeIf(review -> review.getUser().equals(user));
        updateAverageRatings();
        return removed;
    }

    /**
     * Clear the product reviews.
     */
    public void clearProductReviews() {
        productReviews.clear();
        updateAverageRatings();
    }

    /**
     * Compute the average ratings based on the reviews.
     *
     * @return The computed average ratings.
     */
    private Double computeAverageRatings() {
        OptionalDouble average = productReviews.stream().mapToDouble(review -> (double) review.getRating()).average();
        return average.isPresent() ? average.getAsDouble() : null;
    }

    /**
     * Update the average ratings based on the reviews.
     *
     * @return The computed average ratings.
     */
    public Double updateAverageRatings() {
        Double newAverageRatings = computeAverageRatings();
        setAverageRatings(newAverageRatings);
        return newAverageRatings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id != null ? id.equals(product.id) : product.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public enum SortField {
        NAME,
        CURRENT_PRICE,
        AVERAGE_RATINGS,
        DISCOUNT_RATE,
        POSTED_TIME
    }
}
