package com.shutovna.topfive.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemData {
    @NotNull
    @Column(nullable = false)
    private String filename;

    @NotNull
    @Column(nullable = false)
    private String contentType;

    @Override
    public String toString() {
        return "ItemData{" +
                "contentType=" + contentType +
                ", filename='" + filename + '\'' +
                '}';
    }
}
