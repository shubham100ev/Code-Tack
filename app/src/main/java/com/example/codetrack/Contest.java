package com.example.codetrack;

public class Contest {
    private String name;
    private String endTime;
    private String platform;
    private String link;
    private String duration;
    private String startTime;

    public Contest(String name, String endTime, String platform, String link, String duration, String startTime) {
        this.name = name;
        this.endTime = endTime;
        this.platform = platform;
        this.link = link;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPlatform() {
        return platform;
    }

    public String getLink() {
        return link;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }
}
