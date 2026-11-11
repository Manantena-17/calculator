package com.example.app.view

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.app.viewmodel.UserViewmodel

@Composable
fun UserScreen(modifier: Modifier = Modifier,viewmodel: UserViewmodel) {
    val user by viewmodel.user.observeAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick={}
        ){
            Text(text = "Get user")
        }

    }

}