package com.credibanco.tarjetas.dto.card;

import jakarta.validation.constraints.NotBlank;

public class CreateCardRequest {
    @NotBlank(message = "El productId no puede estar vacío")
    private String productId;

    @NotBlank(message = "El holderName no puede estar vacío")
    private String holderName;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }
}
