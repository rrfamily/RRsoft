package Model;

public class user {
    private String name;
    private String Password;
    private String Phone;

    public user() {
    }

    public user(String name, String password,String phone) {
        this.name = name;
        Password = password;
        this.Phone = phone;


    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {

        this.Phone = phone;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
