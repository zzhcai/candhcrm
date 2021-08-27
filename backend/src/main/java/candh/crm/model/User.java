package candh.crm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString

@Document(collection = "user")
public class User implements UserDetails
{
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    /** false by default, visiting the confirmation link will set this to true */
    private boolean enabled;
    /** random path of random length for the confirmation link */
    private String signupConfirmPath;

    // for random string generator
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static SecureRandom random = new SecureRandom();   // alphanumeric string of length 20-30

    public User(String email, String password, String first_name, String last_name) {
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.enabled = false;
        this.signupConfirmPath = generateRandomString(random.nextInt(11)+20);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSignupConfirmPath() {
        return signupConfirmPath;
    }

    public void setSignupConfirmPath(String signupConfirmPath) {
        this.signupConfirmPath = signupConfirmPath;
    }

    private static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            sb.append(DATA_FOR_RANDOM_STRING.charAt(rndCharAt));
        }
        return sb.toString();
    }

    /**
     * Get a user's full name.
     */
    public String getName() {
        return this.first_name + " " + this.last_name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> listAuthorities = new ArrayList<GrantedAuthority>() {{
            add(new SimpleGrantedAuthority("ROLE_USER"));
        }};
        return listAuthorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
}
