package candh.crm.service;

import candh.crm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService implements UserDetailsService
{
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService EmailService;

    @Autowired
    private UserDataService userDataService;

    public void signupUser(User user) throws MessagingException {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDataService.saveUser(user);
        EmailService.sendConfirmMail(user.getEmail(), user.getFirst_name(), user.getSignupConfirmPath());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDataService.findUserByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("email not found");
        }
    }

    /**
     * check email entered follows the following rules
     * @param email    email address user entered
     *
     * domain name must include at least one dot
     * the part of the domain name after the last dot can only consist of letters
     * contain ‘@’ symbol
     * two dots can appear right next to each other in domain name
     * first and last characters in the local part and in the domain name must not be dots
     * The top-level domain (.com in these examples) must consist of two to six letters only
     */
    public boolean vaildEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * check password entered follows the following rules
     * @param password    password address user entered
     *
     * length 5 to 10
     * only contain upper and lower case letter or numbers
     */
    public boolean vaildPassword(String password) {
        String regex = "[A-Za-z0-9]{5,10}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}