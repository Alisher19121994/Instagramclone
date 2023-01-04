package com.example.instagram.model

class Posts {
    var id: String = ""
    var image: String = ""
    var caption: String = ""
    var currentDate: String = ""

    var uid: String = ""
    var fullname: String = ""
    var userImage: String = ""

    constructor(image: String) {
        this.image = image
    }

    constructor(image: String, caption: String) {
        this.image = image
        this.caption = caption
    }

    constructor(id: String, image: String, caption: String) {
        this.id = id
        this.image = image
        this.caption = caption
    }


}



