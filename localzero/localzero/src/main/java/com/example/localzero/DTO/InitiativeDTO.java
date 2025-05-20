package com.example.localzero.DTO;

//It is not suitable to implement Singleton here. Because DTO class 
// holds data and should have multiple instances.
public class InitiativeDTO {
    private String id;
    private String title;
    private String description;
    private String location;
    private String category;
    private String visibility;
    private int createdByUserID;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public String getId() { return id; }
    public void setID(String id) { this.id = id; }

    public int getCreatedByUserID() { return createdByUserID; }
    public void setCreatedByUserID(int createdByUserID) { this.createdByUserID = createdByUserID; }
}
