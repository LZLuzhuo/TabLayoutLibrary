/* Copyright 2022 Luzhuo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.luzhuo.lib_tablayout.callback;

public interface OnTabSelectedListener {
    /**
     * 选中标签
     */
    public void onTabSelected(int position);

    /**
     * 取消标签
     */
    public void onTabUnselected(int position);

    /**
     * 重复选中标签
     */
    public void onTabReselected(int position);
}
