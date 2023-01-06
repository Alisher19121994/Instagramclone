package com.example.instagram.model

class User {
    var uid: String = ""
    var fullname: String = ""
    var emailAddress: String = ""
    var password: String = ""
    var userImage: String = ""

    var deviceId = ""
    var deviceType = "A" // A -> "A"Android
    var deviceToken = ""

    var isFollowed: Boolean = false

    constructor(fullname: String, emailAddress: String) {
        this.fullname = fullname
        this.emailAddress = emailAddress
    }

    constructor(fullname: String, emailAddress: String, userImage: String) {
        this.fullname = fullname
        this.emailAddress = emailAddress
        this.userImage = userImage
    }

    constructor(fullname: String, emailAddress: String, password: String, userImage: String) {
        this.fullname = fullname
        this.emailAddress = emailAddress
        this.password = password
        this.userImage = userImage
    }


}