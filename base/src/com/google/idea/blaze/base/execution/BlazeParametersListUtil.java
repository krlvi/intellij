/*
 * Copyright 2019 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.idea.blaze.base.execution;

import com.google.common.collect.ImmutableList;
import com.intellij.util.execution.ParametersListUtil;
import java.util.List;
import javax.annotation.Nullable;

/** Workaround issues in ParametersListUtil */
public class BlazeParametersListUtil {

  /**
   * Like ParametersListUtil.encode(String) but will quote a param if it contains a single quote.
   *
   * @param param raw parameter to shell-encode for a commandline.
   * @return the encoded param.
   */
  public static String encodeParam(String param) {
    // If single quotes are not quoted, it breaks round trip of parse/join if
    // ParametersListUtil.parse() is called with supportSingleQuotes=true. By forcing a space into
    // the parameter, it will force it to be quoted.
    param = param.replace("'", " '");
    // This will effectively return ParametersListUtil.encodeParam(param), which we can't access
    // directly because it is private
    String output = ParametersListUtil.join(param);
    output = output.replace(" '", "'");
    return output;
  }

  public static List<String> splitParameters(@Nullable String params) {
    if (params == null) {
      return ImmutableList.of();
    }
    return ParametersListUtil.parse(params, true, true);
  }
}
