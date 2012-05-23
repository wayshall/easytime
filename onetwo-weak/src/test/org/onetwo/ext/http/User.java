package org.onetwo.ext.http;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author mcb
 */
public class User
{
  /** unique id */
  private Integer id;

  /** twitter username */
  private String name;

  /** twitter screen name */
  private String screenName;

  /** location */
  private String location;

  /** description */
  private String description;

  /** profile image url */
  private String profileImageUrl;

  /** user url */
  private String url;

  /** user protection status */
  private boolean isProtected;

  /** statuses count */
  private Integer statusesCount;

  /** followers count */
  private Integer followersCount;

  /** friends (following) count */
  private Integer friendsCount;

  /** favorites count */
  private Integer favouritesCount;

  /** latest status */
  private Status status;

  /** creation date */
  private Date createdAt;

  /** utc offset */
  private int utcOffset;

  /** time zone */
  private TimeZone timeZone;

  /** profile bg color */
  private String profileBackgroundColor;

  private String profileTextColor;

  private String profileLinkColor;

  private String profileSidebarFillColor;

  private String profileSidebarBorderColor;

  private String profileBackgroundImageUrl;

  private String profileBackgroundTile;

  public enum Device {
    IM, SMS, NONE
  }

  /**
   * @return the id
   */
  public Integer getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Integer id)
  {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * @return the screenName
   */
  public String getScreenName()
  {
    return screenName;
  }

  /**
   * @param screenName the screenName to set
   */
  public void setScreenName(String screenName)
  {
    this.screenName = screenName;
  }

  /**
   * @return the location
   */
  public String getLocation()
  {
    return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(String location)
  {
    this.location = location;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * @return the profileImageUrl
   */
  public String getProfileImageUrl()
  {
    return profileImageUrl;
  }

  /**
   * @param profileImageUrl the profileImageUrl to set
   */
  public void setProfileImageUrl(String profileImageUrl)
  {
    this.profileImageUrl = profileImageUrl;
  }

  /**
   * @return the url
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url)
  {
    this.url = url;
  }

  /**
   * @return the isProtected
   */
  public boolean isProtected()
  {
    return isProtected;
  }

  /**
   * @param isProtected the isProtected to set
   */
  public void setProtected(boolean isProtected)
  {
    this.isProtected = isProtected;
  }

  /**
   * @return the statusesCount
   */
  public Integer getStatusesCount()
  {
    return statusesCount;
  }

  /**
   * @param statusesCount the statusesCount to set
   */
  public void setStatusesCount(Integer statusesCount)
  {
    this.statusesCount = statusesCount;
  }

  /**
   * @return the followersCount
   */
  public Integer getFollowersCount()
  {
    return followersCount;
  }

  /**
   * @param followersCount the followersCount to set
   */
  public void setFollowersCount(Integer followersCount)
  {
    this.followersCount = followersCount;
  }

  /**
   * @return the friendsCount
   */
  public Integer getFriendsCount()
  {
    return friendsCount;
  }

  /**
   * @param friendsCount the friendsCount to set
   */
  public void setFriendsCount(Integer friendsCount)
  {
    this.friendsCount = friendsCount;
  }

  /**
   * @return the favouritesCount
   */
  public Integer getFavouritesCount()
  {
    return favouritesCount;
  }

  /**
   * @param favouritesCount the favouritesCount to set
   */
  public void setFavouritesCount(Integer favouritesCount)
  {
    this.favouritesCount = favouritesCount;
  }

  /**
   * @return the status
   */
  public Status getStatus()
  {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Status status)
  {
    this.status = status;
  }

  /**
   * @return the createdAt
   */
  public Date getCreatedAt()
  {
    return createdAt;
  }

  /**
   * @param createdAt the createdAt to set
   */
  public void setCreatedAt(Date createdAt)
  {
    this.createdAt = createdAt;
  }

  /**
   * @return the utcOffset
   */
  public Integer getUtcOffset()
  {
    return utcOffset;
  }

  /**
   * @param utcOffset the utcOffset to set
   */
  public void setUtcOffset(Integer utcOffset)
  {
    this.utcOffset = utcOffset;
  }

  /**
   * @return the timeZone
   */
  public TimeZone getTimeZone()
  {
    return timeZone;
  }

  /**
   * @param timeZone the timeZone to set
   */
  public void setTimeZone(TimeZone timeZone)
  {
    this.timeZone = timeZone;
  }

  /**
   * @return the profileBackgroundColor
   */
  public String getProfileBackgroundColor()
  {
    return profileBackgroundColor;
  }

  /**
   * @param profileBackgroundColor the profileBackgroundColor to set
   */
  public void setProfileBackgroundColor(String profileBackgroundColor)
  {
    this.profileBackgroundColor = profileBackgroundColor;
  }

  /**
   * @return the profileTextColor
   */
  public String getProfileTextColor()
  {
    return profileTextColor;
  }

  /**
   * @param profileTextColor the profileTextColor to set
   */
  public void setProfileTextColor(String profileTextColor)
  {
    this.profileTextColor = profileTextColor;
  }

  /**
   * @return the profileLinkColor
   */
  public String getProfileLinkColor()
  {
    return profileLinkColor;
  }

  /**
   * @param profileLinkColor the profileLinkColor to set
   */
  public void setProfileLinkColor(String profileLinkColor)
  {
    this.profileLinkColor = profileLinkColor;
  }

  /**
   * @return the profileSidebarFillColor
   */
  public String getProfileSidebarFillColor()
  {
    return profileSidebarFillColor;
  }

  /**
   * @param profileSidebarFillColor the profileSidebarFillColor to set
   */
  public void setProfileSidebarFillColor(String profileSidebarFillColor)
  {
    this.profileSidebarFillColor = profileSidebarFillColor;
  }

  /**
   * @return the profileSidebarBorderColor
   */
  public String getProfileSidebarBorderColor()
  {
    return profileSidebarBorderColor;
  }

  /**
   * @param profileSidebarBorderColor the profileSidebarBorderColor to set
   */
  public void setProfileSidebarBorderColor(String profileSidebarBorderColor)
  {
    this.profileSidebarBorderColor = profileSidebarBorderColor;
  }

  /**
   * @return the profileBackgroundImageUrl
   */
  public String getProfileBackgroundImageUrl()
  {
    return profileBackgroundImageUrl;
  }

  /**
   * @param profileBackgroundImageUrl the profileBackgroundImageUrl to set
   */
  public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl)
  {
    this.profileBackgroundImageUrl = profileBackgroundImageUrl;
  }

  /**
   * @return the profileBackgroundTile
   */
  public String getProfileBackgroundTile()
  {
    return profileBackgroundTile;
  }

  /**
   * @param profileBackgroundTile the profileBackgroundTile to set
   */
  public void setProfileBackgroundTile(String profileBackgroundTile)
  {
    this.profileBackgroundTile = profileBackgroundTile;
  }

  public String toString()
  {
    return "User(id=" + id + ",name=" + name + ",screenName=" + screenName + ")";
  }
}
