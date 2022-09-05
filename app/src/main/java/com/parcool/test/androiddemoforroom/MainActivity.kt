package com.parcool.test.androiddemoforroom

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parcool.test.androiddemoforroom.ui.theme.AndroidDemoForRoomTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private val tag = this.javaClass::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidDemoForRoomTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column {
                        Greeting("Android")
                        Button(onClick = {
                            MainScope().launch {
                                withContext(Dispatchers.IO) {
                                    //Query
                                    val users = DbUtil.getUserDao(this@MainActivity).getAll()
                                    users.forEach {
//                                        Log.d(tag, "id=${it.uid},firstName=${it.firstName},lastName=${it.lastName}")
                                        Log.d(tag, "id=${it.uid},firstName=${it.firstName},lastName=${it.lastName},nickName=${it.nickName}")
                                    }
                                }
                            }
                        }) {
                            Text("查询")
                        }

                        Button(onClick = {
                            MainScope().launch {
                                withContext(Dispatchers.IO) {
//                                    val user = User(firstName = "tan", lastName = "parcool-${Random(System.currentTimeMillis()).nextInt()}")
                                    val user = User(firstName = "tan", lastName = "parcool", nickName = Random(System.currentTimeMillis()).nextInt().toString())
                                    //Insert
//                                    DbUtil.getUserDao(this@MainActivity).insertUsers(user)
                                    DbUtil.getUserDao(this@MainActivity).insertUsers(user)
                                }
                                Toast.makeText(this@MainActivity,"插入完成！",Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("插入")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidDemoForRoomTheme {
        Greeting("Android")
    }
}