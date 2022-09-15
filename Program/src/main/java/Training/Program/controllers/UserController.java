package Training.Program.controllers;

import static Training.Program.mongodb.Mongodb.isUsernameAvailable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Training.Program.models.Users;
import Training.Program.mongodb.Mongodb;



@Controller
@RequestMapping(path = "")
public class UserController {

    @GetMapping(path = "register")
    public String viewRegistrationPage(Model model){
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping(path = "register")
    public String registerUser(@ModelAttribute Users user, Model model){
        try{
            validateUsername(user.getUserName());
            isUsernameAvailable(user.getUserName());
            validatePassword(user.getPassword());
            if(!user.confirmPassword())
                throw new Exception("Passwords don't match!");
            Mongodb.addUser(user.getUserName(), user.getPassword());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }
        model.addAttribute("success", "Registration Successful!");
        model.addAttribute("user", new Users());
        return "register";
    }

    private static boolean validateUsername(String username) throws Exception {
        if(username.length() < 4)
            throw new Exception("Username should have at least 4 characters!");
        else if(!Character.isAlphabetic(username.charAt(0)))
            throw new Exception("Username should start with an alphabetic character!");
        else{
            for(char c : username.toCharArray()){
                if(!(Character.isLetterOrDigit(c) || c == '_' || c == '-'))
                    throw new Exception("Username should have alphanumeric characters\n or underscore or hyphen only!");
            }
            return true;
        }
    }

    private static boolean validatePassword(String password) throws Exception{
        if(password.length() < 8)
            throw new Exception("Password should have at least 8 characters!");
        else{
            int upper = 0, lower = 0, num = 0, special = 0;
            for(char c : password.toCharArray()){
                if(Character.isSpaceChar(c) || c == '>' || c == '<' || c == '&' || c == ';')
                    throw new Exception("Password has invalid characters: " + c);
                else if(Character.isUpperCase(c))
                    upper++;
                else if(Character.isLowerCase(c))
                    lower++;
                else if(Character.isDigit(c))
                    num++;
                else
                    special++;

            }
            if(upper < 2)
                throw new Exception("Password should have at least 2 upper-case characters");
            if(lower < 2)
                throw new Exception("Password should have at least 2 lower-case characters");
            if(num < 2)
                throw new Exception("Password should have at least 2 numeric characters");
            if(special < 2)
                throw new Exception("Password should have at least 2 special characters");
            return true;
        }
    }

}

