package org.rc.otherKnowledge.class_usage.enum_usage;

public enum Week {
    MONDAY("星期一"),
    TUESDAY("星期二"),
    WEDNESDAY("星期三"),
    THURSDAY("星期四"),
    FRIDAY("星期五"),
    SATURDAY("星期六"),
    SUNDAY("星期日");
    private final String description;

    private Week(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return super.toString() + ":" + description;
    }

}
