/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bubbble.andsplash.util.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * 与Auth提供程序（在本例中为Firebase）交互的表示层中的元素。
 *
 * 从 Activity 或 Fragment 中使用此类。
 */
interface SignInHandler {

    fun makeSignInIntent(context: Activity): LiveData<Intent?>

    fun signOut(context: Context)
}

/**
 * 与Firebase Auth交互的[SignInHandler]的实现。
 */
class GoogleAuthSignInHandler(private val externalScope: CoroutineScope) : SignInHandler {

    /**
     * 请求登录 Intent。
     *
     * 要观察结果，您必须将其传递给startActivityForResult。
     */
    override fun makeSignInIntent(context: Activity): LiveData<Intent?> {

        val result = MutableLiveData<Intent?>()

        // 在后台运行，因为AuthUI会执行I / O操作。
        externalScope.launch {

        }
        return result
    }


    override fun signOut(context: Context) {

        externalScope.launch {

        }
    }
}
