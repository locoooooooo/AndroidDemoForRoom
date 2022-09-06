package com.parcool.test.androiddemoforroom

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.parcool.test.androiddemoforroom.ui.theme.AndroidDemoForRoomTheme
import com.parcool.test.androiddemoforroom.ui.theme.Purple200
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private val tag = this.javaClass::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidDemoForRoomTheme {
                val state = remember {
                    mutableStateOf(false)
                }
                val userStateList = remember {
                    mutableStateOf(listOf<User>())
                }
                val selectedIndex = remember {
                    mutableStateOf(-1)
                }
                val insertAction: (User) -> Unit = {
                    MainScope().launch {
                        withContext(Dispatchers.IO) {
                            DbUtil.getUserDao(this@MainActivity).insertUsers(it)
                        }
                        queryAllData(userStateList)
                        Toast.makeText(this@MainActivity, "插入完成！", Toast.LENGTH_SHORT).show()
                        selectedIndex.value = -1
                    }
                }


                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column {
                        Greeting()
                        Row {
                            //插入按钮
                            Button(onClick = {
                                state.value = !state.value
                            }) {
                                Text("插入")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            //查询按钮
                            Button(onClick = {
                                queryAllData(userStateList)
                                selectedIndex.value = -1
                            }) {
                                Text("查询")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            //删除选中按钮
                            Button(onClick = {
                                MainScope().launch {
                                    withContext(Dispatchers.IO) {
                                        if (selectedIndex.value != -1) {
                                            val user = userStateList.value[selectedIndex.value]
                                            DbUtil.getUserDao(this@MainActivity).delete(user)
                                            queryAllData(userStateList)
                                            selectedIndex.value = -1
                                        } else {
                                            Toast.makeText(this@MainActivity, "请选择一条数据", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }) {
                                Text("删除选中")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            //删除全部按钮
                            Button(onClick = {
                                MainScope().launch {
                                    withContext(Dispatchers.IO) {
                                        //Query
                                        DbUtil.getUserDao(this@MainActivity).deleteAll()
                                        queryAllData(userStateList)
                                        selectedIndex.value = -1
                                    }
                                }
                            }) {
                                Text("删除全部")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        //查询的结果列表
                        UserList(userStateList, selectedIndex)
                    }
                    if (state.value) {
                        DialogInsert(state, insertAction)
                    }
                }
                //首次进入就查询一次
                queryAllData(userStateList)
            }
        }
    }

    private fun queryAllData(userStateList: MutableState<List<User>>) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                //Query
                val users = DbUtil.getUserDao(this@MainActivity).getAll()
                userStateList.value = users
            }
        }
    }
}

@Composable
fun Greeting() {
    Text(text = "Android Room Demo")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidDemoForRoomTheme {
        Greeting()
    }
}


@Preview
@Composable
fun DialogInsert(state: MutableState<Boolean> = mutableStateOf(false), action: ((User) -> Unit)? = null) {
    var textFirstName by remember { mutableStateOf("") }
    var textLastName by remember { mutableStateOf("") }
    var textNickname by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {
            Log.e("ccm", "====onDismiss=====")
            state.value = false
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, securePolicy = SecureFlagPolicy.SecureOff)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 15.dp)
                .background(
                    Color.White, shape = RoundedCornerShape(8.dp)
                )
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "请输入信息", color = Color.Black, fontSize = 16.sp, modifier = Modifier.padding(start = 10.dp), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                value = textFirstName,
                onValueChange = {
                    textFirstName = it
                },
                label = { Text("Firstname") }
            )
            TextField(
                value = textLastName,
                onValueChange = {
                    textLastName = it
                },
                label = { Text("Lastname") }
            )
            TextField(
                value = textNickname,
                onValueChange = {
                    textNickname = it
                },
                label = { Text("Nickname") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(modifier = Modifier.height(0.5.dp))
            Row() {
                Button(
                    onClick = {
                        state.value = false
                    },
                    modifier = Modifier.weight(1f, true),
//                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                ) {
                    Text(text = "取消")
                }
                Button(
                    onClick = {
                        state.value = false
                        val user = User(firstName = textFirstName, lastName = textLastName, nickName = textNickname)
                        action?.invoke(user)
                    },
                    modifier = Modifier.weight(1f, true),
//                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                ) {
                    Text(text = "确定")
                }
            }
        }
    }
}

@Composable
fun UserList(stateWithData: MutableState<List<User>>, selectedIndex: MutableState<Int>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (stateWithData.value.isNotEmpty()) {
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .requiredHeightIn(min = 32.dp)
                        .background(Color.LightGray),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "firstName", modifier = Modifier.weight(1F))
                    Text(text = "lastName", modifier = Modifier.weight(2F))
                    Text(text = "nickName", modifier = Modifier.weight(2F))
                }
            }
        } else {
            item {
                Text(text = "暂无数据")
            }
        }
        items(stateWithData.value.size) { userIndex ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(min = 32.dp)
                    .background(color = if (userIndex == selectedIndex.value) Purple200 else Color.Transparent)
                    .clickable {
                        if (selectedIndex.value == userIndex) {
                            selectedIndex.value = -1
                        } else {
                            selectedIndex.value = userIndex
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stateWithData.value[userIndex].firstName ?: "", modifier = Modifier.weight(1F))
                Text(text = stateWithData.value[userIndex].lastName ?: "", modifier = Modifier.weight(2F))
                Text(text = stateWithData.value[userIndex].nickName ?: "", modifier = Modifier.weight(2F))
            }
        }
    }
}
