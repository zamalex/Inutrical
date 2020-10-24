package inutrical.com.inutrical.api

import android.content.Context
import android.content.SharedPreferences


object LocalData{


        fun saveUser(name:String?,pass:String?,mail:String?,context: Context){
            val pref: SharedPreferences =
                context
                    .getSharedPreferences("user", 0) // 0 - for private mode

            val editor: SharedPreferences.Editor = pref.edit()
            editor.putString("name", name); // Storing string
            editor.putString("pass", pass); // Storing string
            editor.putString("mail", mail); // Storing string


            editor.apply();

        }

    fun getUser(context: Context):User{
        val pref: SharedPreferences =
            context
                .getSharedPreferences("user", 0) // 0 - for private mode

        return User(pref.getString("name", null),pref.getString("pass", null),pref.getString("mail", null))



    }



}