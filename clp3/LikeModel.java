package com.example.clp3;

public class LikeModel {
    private int likeCount;
    private boolean isLiked;

    public LikeModel() {

    }
    public LikeModel(int likeCount, boolean isLiked) {
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }
}
