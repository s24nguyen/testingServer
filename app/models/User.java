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
    private Long id;

    @Constraints.Required
    private String firstName;

    @Constraints.Required
    private String lastName;

    @Constraints.Required
    @Constraints.Email
    private String email;

    @Constraints.Required
    private String password;

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
        this.password = User.getSha512(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return firstName;
    }

    public void setFirst_name(String first_name) {
        this.firstName = first_name;
    }

    public String getLast_name() {
        return lastName;
    }

    public void setLast_name(String last_name) {
        this.lastName = last_name;
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
                .ilike("firstName", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .fetch("company")
                .findPagedList(pageSize*(page-1)+1, pageSize*page);
    }

    // Transient field
    public String getConfirm_password() {
        return cPassword;
    }

    public void setConfirm_password(String confirm_password) {
        this.cPassword = User.getSha512(confirm_password);
    }

    // Transient field
    @Transient
    String cPassword;

    // Get SHA password
    public static String getSha512(String input) {
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
