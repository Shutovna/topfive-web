package com.shutovna.topfive.entities.payload;

public abstract class UpdateItemPayload extends ItemPayload{
    public UpdateItemPayload(String title, String description) {
        super(title, description);
    }
}
