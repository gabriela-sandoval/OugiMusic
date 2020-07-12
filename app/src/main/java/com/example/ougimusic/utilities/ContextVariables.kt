package com.example.ougimusic.utilities

import android.app.Application
import com.example.ougimusic.Classes.Queue


class ContextVariables : Application() {
    val rootDirection:String ?= "http://192.168.1.73/"
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

    private var queue:Queue? = null

    fun getQueue():Queue?{
        return queue
    }

    fun setQueue(queue: Queue?){
        this.queue = queue
    }


    private var someVariable: String? = null

    fun getSomeVariable(): String? {
        return someVariable
    }

    fun setSomeVariable(someVariable: String?) {
        this.someVariable = someVariable
    }
}