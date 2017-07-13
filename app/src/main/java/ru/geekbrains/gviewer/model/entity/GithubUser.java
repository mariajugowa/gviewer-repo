package ru.geekbrains.gviewer.model.entity;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import android.support.annotation.Nullable;

public class GithubUser extends RealmObject {

  @PrimaryKey
  private String login;
  private String name;
  private String bio;
  private String location;
  private String company;
  private String email;
  @SerializedName("avatar_url")
  private String avatarUrl;
  @SerializedName("blog")
  private String blogUrl;

  public void setName(String name) {
    this.name = name;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public void setBlogUrl(String blogUrl) {
    this.blogUrl = blogUrl;
  }

  @Nullable
  public String getName() {
    return name;
  }

  @Nullable
  public String getBio() {
    return bio;
  }

  @Nullable
  public String getLocation() {
    return location;
  }

  @Nullable
  public String getCompany() {
    return company;
  }

  @Nullable
  public String getEmail() {
    return email;
  }

  @Nullable
  public String getLogin() {
    return login;
  }

  @Nullable
  public String getAvatarUrl() {
    return avatarUrl;
  }

  @Nullable
  public String getBlogUrl() {
    return blogUrl;
  }

}