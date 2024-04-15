package com.shutovna.topfive.entities;

public enum TopType {
    SONG("Музыка"), VIDEO("Видео"), PHOTO("Фото");
    private String name;

    TopType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
