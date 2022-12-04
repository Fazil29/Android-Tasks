package com.example.todocompose.TASK_3.util.helpers

import android.content.Context
import com.example.todocompose.TASK_3.util.USER_DATA
import com.example.todocompose.TASK_3.util.UserModel
import com.example.todocompose.TASK_3.util.readString
import com.example.todocompose.TASK_3.util.writeString
import com.google.android.gms.auth.api.identity.SignInCredential
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first

object CredentialHelper {

    suspend fun getCredentialsFromLocal(context: Context): UserModel? {
        context.apply {
            val userJson = readString(USER_DATA).first()
            if(userJson.isEmpty())
                return null
            val moshi: Moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<UserModel> = moshi.adapter(UserModel::class.java)
            return adapter.fromJson(userJson)
        }
    }

    suspend fun saveCredentials(context: Context, credentials: SignInCredential){
        context.apply {
            credentials.apply {
                val user = UserModel(this.givenName.toString(), this.familyName.toString(), this.id)
                val moshi: Moshi = Moshi.Builder().build()
                val adapter: JsonAdapter<UserModel> = moshi.adapter(UserModel::class.java)
                val serializedUser = adapter.toJson(user)
                writeString(USER_DATA, serializedUser)
            }
        }
    }
}