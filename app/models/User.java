package models;

import com.avaje.ebean.*;
import play.data.validation.Constraints;
import play.*;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.Constraint;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@Entity
public class User extends Model{
    @Id
    @GeneratedValue
    public Long id;

    @Constraints.Required
    public String first_name;

    @Constraints.Required
    public String last_name;

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = User.getSha256(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    // Methods
    //public static Finder<Long,User> find = new Finder<>(Long.class, User.class);

    public static User findByEmail(String email)
    {
        return Ebean.find(User.class)
                .where()
                .like("email", email)
                .findUnique();
    }

    /**
     * Return a page of computer
     *
     * @param page     Page to display
     * @param pageSize Number of computers per page
     * @param sortBy   Computer property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     */
    public static PagedList<User> page(int page, int pageSize, String sortBy,
                                  String order, String filter) {
        return Ebean.find(User.class)
                .where()
                .ilike("first_name", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .fetch("company")
                .findPagedList(pageSize*(page-1)+1, pageSize*page);
    }

    // Transient field
    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = User.getSha256(confirm_password);
    }

    // Transient field
    @Transient
    String confirm_password;

    // Get SHA password
    public static String getSha256(String input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
