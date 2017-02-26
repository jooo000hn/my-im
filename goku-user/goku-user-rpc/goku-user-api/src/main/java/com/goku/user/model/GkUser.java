package com.goku.user.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 注：任何表实体必须继承BaseEntity,任何表结构必须包含
 * createTime、updateTime、createBy和updateBy字段
 * User: user
 * Date: 15/10/23
 * Version: 1.0
 */
public class GkUser implements Serializable {
    /**
     * 用户id
     */
    private Integer id;

    /**

     * 平台userid 唯一
     */
    private Integer puId;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 姓名
     */
    private String username;

    /**
     * 性别：0-男性 1-女性 2-保密
     */
    private String sex;

    /**
     * 用户等级
     */
    private String level;

    /**
     * 年龄
     */
    private Integer age;

    /**

     * 用户昵称
     */
    private String nick;

    /**
     * 照片
     */
    private String photo;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 个人签名
     */
    private String underwrite;

    /**
     * 用户角色
     */
    private BigDecimal userroleId;

    /**
     * 是否为管理员：0-非管理员 1-管理员
     */
    private Byte isAdmin;

    /**
     * qq号码
     */
    private String qqNumber;

    /**
     * msn号码
     */
    private String msnNumber;

    /**
     *生日
     */
    private String birthday;

    /**
     * 性取向
     */
    private Integer corientation;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 上次活动时间
     */
    private Date lastactivetime;

    /**
     * 上次登录时间
     */
    private Date lastlogintime;

    /**
     * 上次登录ip地址
     */
    private String lastloginip;

    /**
     * 有效截止日期
     */
    private Date effectdateend;

    /**
     * 是否可用
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

    /**
     * @return 用户id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            用户id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return 姓名
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            姓名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return 性别：0-男性 1-女性 2-保密
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     *            性别：0-男性 1-女性 2-保密
     */
    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    /**
     * @return 照片
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo
     *            照片
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     *            手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return 个人签名
     */
    public String getUnderwrite() {
        return underwrite;
    }

    /**
     * @param underwrite
     *            个人签名
     */
    public void setUnderwrite(String underwrite) {
        this.underwrite = underwrite;
    }

    /**
     * @return 用户角色
     */
    public BigDecimal getUserroleId() {
        return userroleId;
    }

    /**
     * @param userroleId
     *            用户角色
     */
    public void setUserroleId(BigDecimal userroleId) {
        this.userroleId = userroleId;
    }

    /**
     * @return 是否为管理员：0-非管理员 1-管理员
     */
    public Byte getIsAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin
     *            是否为管理员：0-非管理员 1-管理员
     */
    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return qq号码
     */
    public String getQqNumber() {
        return qqNumber;
    }

    /**
     * @param qqNumber
     *            qq号码
     */
    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber;
    }

    /**
     * @return msn号码
     */
    public String getMsnNumber() {
        return msnNumber;
    }

    /**
     * @param msnNumber
     *            msn号码
     */
    public void setMsnNumber(String msnNumber) {
        this.msnNumber = msnNumber;
    }

    /**
     * @return 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     *            创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * @return 上次活动时间
     */
    public Date getLastactivetime() {
        return lastactivetime;
    }

    /**
     * @param lastactivetime
     *            上次活动时间
     */
    public void setLastactivetime(Date lastactivetime) {
        this.lastactivetime = lastactivetime;
    }

    /**
     * @return 上次登录时间
     */
    public Date getLastlogintime() {
        return lastlogintime;
    }

    /**
     * @param lastlogintime
     *            上次登录时间
     */
    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    /**
     * @return 上次登录ip地址
     */
    public String getLastloginip() {
        return lastloginip;
    }

    /**
     * @param lastloginip
     *            上次登录ip地址
     */
    public void setLastloginip(String lastloginip) {
        this.lastloginip = lastloginip;
    }

    /**
     * @return 有效截止日期
     */
    public Date getEffectdateend() {
        return effectdateend;
    }

    /**
     * @param effectdateend
     *            有效截止日期
     */
    public void setEffectdateend(Date effectdateend) {
        this.effectdateend = effectdateend;
    }

    /**
     * @return 是否可用
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * @param status
     *            是否可用
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     *            备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPuId() {
        return puId;
    }

    public void setPuId(Integer puId) {
        this.puId = puId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getCorientation() {
        return corientation;
    }
    
    public void setCorientation(Integer corientation) {
        this.corientation = corientation;
    }

}
