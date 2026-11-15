package org.rc.otherKnowledge.class_usage.enum_usage;

public class Season {
    private final String seasonName;
    private final String seasonDesc;

    // 私有构造器：外界无法 new，保证实例数量可控
    private Season(String seasonName, String seasonDesc) {
        this.seasonName = seasonName;
        this.seasonDesc = seasonDesc;
    }

    // 在类内部创建唯一的几个实例，并用 public static final 暴露
    public static final Season SPRING = new Season("春天", "春暖花开");
    public static final Season SUMMER = new Season("夏天", "夏日炎炎");
    public static final Season AUTUMN = new Season("秋天", "秋高气爽");
    public static final Season WINTER = new Season("冬天", "冰天雪地");

    public String getSeasonName() {
        return seasonName;
    }

    public String getSeasonDesc() {
        return seasonDesc;
    }

    @Override
    public String toString() {
        return "Season{" + seasonName + ", " + seasonDesc + "}";
    }
}
