package test.webblog.entity;

import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.yooyo.zjk.BaseEntity;


/*****
 * 
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="wp_users")
@SequenceGenerator(name="UsersEntityGenerator", sequenceName="SEQ_wp_users")
public class UsersEntity extends BaseEntity<Long> {
	
	protected Long id;
  
	protected String userLogin;
  
	protected String userPass;
  
	protected String userNicename;
  
	protected String userEmail;
  
	protected String userUrl;
  
	protected Date userRegistered;
  
	protected String userActivationKey;
  
	protected Integer userStatus;
  
	protected String displayName;
  
	
	public UsersEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UsersEntityGenerator")
	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="user_login")
	public String getUserLogin() {
		return this.userLogin;
	}
	
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="user_pass")
	public String getUserPass() {
		return this.userPass;
	}
	
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="user_nicename")
	public String getUserNicename() {
		return this.userNicename;
	}
	
	public void setUserNicename(String userNicename) {
		this.userNicename = userNicename;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="user_email")
	public String getUserEmail() {
		return this.userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="user_url")
	public String getUserUrl() {
		return this.userUrl;
	}
	
	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="user_registered")
	public Date getUserRegistered() {
		return this.userRegistered;
	}
	
	public void setUserRegistered(Date userRegistered) {
		this.userRegistered = userRegistered;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="user_activation_key")
	public String getUserActivationKey() {
		return this.userActivationKey;
	}
	
	public void setUserActivationKey(String userActivationKey) {
		this.userActivationKey = userActivationKey;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="user_status")
	public Integer getUserStatus() {
		return this.userStatus;
	}
	
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="display_name")
	public String getDisplayName() {
		return this.displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
