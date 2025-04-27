package com.phamhuong.library.model;

public enum NotificationType {
    NEW_BOOK("📚"),
    DUE_REMINDER("🔁"),
    PROMOTION("💥"),
    RENEWAL_SUCCESS("✅"),
    RECOMMENDATION("❤️"),
    ORDER_STATUS("📦"),
    SYSTEM("🕐");

    private final String emoji;

    NotificationType(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }
}
