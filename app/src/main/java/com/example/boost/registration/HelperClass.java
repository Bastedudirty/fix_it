package com.example.boost.registration;

import androidx.appcompat.app.AppCompatActivity;

public class HelperClass extends AppCompatActivity {

        String  email, username, password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public HelperClass(String username, String email, String password) {
            this.password = password;
            this.email = email;
            this.username = username;
        }
    }


