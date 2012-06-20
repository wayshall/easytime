/**
 * 
 */
package org.onetwo.ext.http;

import java.util.Date;

/**
 * @author mcb
 */
public class Status
{
  /** unique id */
  private Long id;

  /** text */
  private String text;

  /** user */
  private User user;

  /** source */
  private String source;

  /** truncated */
  private boolean isTruncated;

  /** in reply to status */
  private Integer inReplyToStatusId;

  /** in reply to user */
  private Integer inReplyToUserId;

  /** in reply to screen name */
  private String inReplyToScreenName;

  /** is favorited */
  private boolean isFavorited;

  /** creation date */
  private Date createdAt;

  /**
   * @return the source
   */
  public String getSource()
  {
    return source;
  }

  /**
   * @param source the source to set
   */
  public void setSource(String source)
  {
    this.source = source;
  }

  /**
   * @return the isTruncated
   */
  public boolean isTruncated()
  {
    return isTruncated;
  }

  /**
   * @param isTruncated the isTruncated to set
   */
  public void setTruncated(boolean isTruncated)
  {
    this.isTruncated = isTruncated;
  }

  /**
   * @return the inReplyToStatusId
   */
  public Integer getInReplyToStatusId()
  {
    return inReplyToStatusId;
  }

  /**
   * @param inReplyToStatusId the inReplyToStatusId to set
   */
  public void setInReplyToStatusId(Integer inReplyToStatusId)
  {
    this.inReplyToStatusId = inReplyToStatusId;
  }

  /**
   * @return the inReplyToUserId
   */
  public Integer getInReplyToUserId()
  {
    return inReplyToUserId;
  }

  /**
   * @param inReplyToUserId the inReplyToUserId to set
   */
  public void setInReplyToUserId(Integer inReplyToUserId)
  {
    this.inReplyToUserId = inReplyToUserId;
  }

  /**
   * @return the inReplyToScreenName
   */
  public String getInReplyToScreenName()
  {
    return inReplyToScreenName;
  }

  /**
   * @param inReplyToScreenName the inReplyToScreenName to set
   */
  public void setInReplyToScreenName(String inReplyToScreenName)
  {
    this.inReplyToScreenName = inReplyToScreenName;
  }

  /**
   * @return the isFavorited
   */
  public boolean isFavorited()
  {
    return isFavorited;
  }

  /**
   * @param isFavorited the isFavorited to set
   */
  public void setFavorited(boolean isFavorited)
  {
    this.isFavorited = isFavorited;
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
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id)
  {
    this.id = id;
  }

  /**
   * @return the text
   */
  public String getText()
  {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text)
  {
    this.text = text;
  }

  /**
   * @return the user
   */
  public User getUser()
  {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(User user)
  {
    this.user = user;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "Status(id=" + id + ",text=" + text + ",user=" + user + ",source=" + source + ")";
  }
}
