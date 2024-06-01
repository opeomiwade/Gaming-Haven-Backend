package org.gaminghaven.entities;
import javax.persistence.*;

@Entity
@Table(name = "productimage")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int imageId;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product getProduct() {
        return product;
    }

    public int getImageId() {
        return imageId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
