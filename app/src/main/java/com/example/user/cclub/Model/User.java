package com.example.user.cclub.Model;

/**
 * Created by user on 12/28/2017.
 */

public class User {

        private String name;
        private String password;
        private String mail;
        private String phone;
        private String address;

        public User() {
        }

        public User(String phone, String mail, String password, String name,String address) {
            this.name = name;
            this.mail = mail;
            this.address = address;
            this.password = password;
            this.phone = phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

    }



