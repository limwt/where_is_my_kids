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

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SnackBarManager {
  private val messages: MutableStateFlow<SnackBarMessage?> = MutableStateFlow(null)
  val snackBarMessages: StateFlow<SnackBarMessage?>
    get() = messages.asStateFlow()

  fun showMessage(@StringRes message: Int) {
    messages.value = SnackBarMessage.ResourceSnackBar(message)
  }

  fun showMessage(message: SnackBarMessage) {
    messages.value = message
  }
}
