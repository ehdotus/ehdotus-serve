package ehdotus.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class DifficultyData {

    private Integer estimatedDifficulty;
    private Integer realDifficulty;
    private String userId;
    private String course;
    private String exercise;

    private Map<String, String> content;

    public DifficultyData() {
        this.content = new TreeMap<>();
    }

    public Integer getEstimatedDifficulty() {
        return estimatedDifficulty;
    }

    public void setEstimatedDifficulty(Integer estimatedDifficulty) {
        this.estimatedDifficulty = estimatedDifficulty;
    }

    public Integer getRealDifficulty() {
        return realDifficulty;
    }

    public void setRealDifficulty(Integer realDifficulty) {
        this.realDifficulty = realDifficulty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public void addEntry(String key, String value) {
        if (this.content == null) {
            this.content = new TreeMap<>();
        }

        this.content.put(key, value);
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

}
