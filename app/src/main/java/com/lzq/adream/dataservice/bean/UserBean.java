package com.lzq.adream.dataservice.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by ${廖昭启} on 2017/11/28.
 */
@Entity(nameInDb = "USER_TABLE")
public class UserBean {
    @Id
    private long id;

    @Property(nameInDb = "username")
    private String userName;
    @Property(nameInDb = "password")
    private String passWord;
    @Property(nameInDb = "sex")
    private  String  sex;
    @Property(nameInDb = "age")
    private int  age;
    @Generated(hash = 1223862457)
    public UserBean(long id, String userName, String passWord, String sex, int age) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.sex = sex;
        this.age = age;
    }
    @Generated(hash = 1203313951)
    public UserBean() {
    }
    public String getPassWord() {
        return this.passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

}
