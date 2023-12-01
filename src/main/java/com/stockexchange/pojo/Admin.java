package com.stockexchange.pojo;

/**
 * Package: com.stockexchange.pojo
 * ClassName: Admin
 * Description 管理员类
 *
 * @author
 * @Create 2023/11/18/ 19:42
 * @Version 1.0
 */
public class Admin {
    private Integer adminId;
    private String userName;
    private String password;


    // get和set方法
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
