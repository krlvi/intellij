/*
 * Copyright 2016 The Bazel Authors. All rights reserved.
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
package com.google.idea.blaze.base.experiments;

import com.google.idea.blaze.base.scope.BlazeContext;
import com.google.idea.blaze.base.scope.BlazeScope;
import com.google.idea.common.experiments.ExperimentService;

/** Reloads experiments at the start of the scope. */
public class ExperimentScope implements BlazeScope {
  @Override
  public void onScopeBegin(BlazeContext context) {
    ExperimentService.getInstance().startExperimentScope();
  }

  @Override
  public void onScopeEnd(BlazeContext context) {
    ExperimentService.getInstance().endExperimentScope();
  }
}
