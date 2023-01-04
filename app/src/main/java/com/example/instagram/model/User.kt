package com.example.instagram.model

class User {
    var uid: String? = null
    var fullname: String
    var emailAddress: String
    var password: String? = null
    var userImage: String? = null

    constructor(fullname: String, emailAddress: String) {
        this.fullname = fullname
        this.emailAddress = emailAddress
    }

    constructor(fullname: String, emailAddress: String, password: String?) {
        this.fullname = fullname
        this.emailAddress = emailAddress
        this.password = password
    }

    constructor(fullname: String, emailAddress: String, password: String?, userImage: String?) {
        this.fullname = fullname
        this.emailAddress = emailAddress
        this.password = password
        this.userImage = userImage
    }
}