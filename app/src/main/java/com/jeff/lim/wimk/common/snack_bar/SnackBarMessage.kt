/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.jeff.lim.wimk.common.snack_bar

import android.content.res.Resources
import androidx.annotation.StringRes
import com.jeff.lim.wimk.R.string as AppText


sealed class SnackBarMessage {
  class StringSnackBar(val message: String) : SnackBarMessage()
  class ResourceSnackBar(@StringRes val message: Int) : SnackBarMessage()

  companion object {
    fun SnackBarMessage.toMessage(resources: Resources): String {
      return when (this) {
        is StringSnackBar -> this.message
        is ResourceSnackBar -> resources.getString(this.message)
      }
    }

    fun Throwable.toSnackBarMessage(): SnackBarMessage {
      val message = this.message.orEmpty()
      return if (message.isNotBlank()) StringSnackBar(message)
      else ResourceSnackBar(AppText.generic_error)
    }
  }
}
