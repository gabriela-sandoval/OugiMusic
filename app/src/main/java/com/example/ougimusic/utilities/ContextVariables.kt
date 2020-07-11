package com.example.ougimusic.utilities

import android.app.Application

class ContextVariables : Application() {
    val rootDirection:String ?= "http://192.168.0.7/"
    var userName:String
        get() {
            return userName
        }
        set(newUserName:String){
            userName = newUserName
        }

    var password:String
        get() {
            return password
        }
        set(newPassword:String){
            password = newPassword
        }
    var token:String
        get() {
            return token
        }
        set(newToken:String){
            token = newToken
        }
}