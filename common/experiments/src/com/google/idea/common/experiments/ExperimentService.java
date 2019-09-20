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
package com.google.idea.common.experiments;

import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.application.ApplicationManager;
import javax.annotation.Nullable;

/** Reads experiments. */
public interface ExperimentService {

  static ExperimentService getInstance() {
    return ApplicationManager.getApplication().getComponent(ExperimentService.class);
  }

  /** Returns an experiment if it exists, else defaultValue */
  boolean getExperiment(Experiment experiment, boolean defaultValue);

  /** Returns a string-valued experiment if it exists, else defaultValue. */
  @Nullable
  String getExperimentString(Experiment experiment, @Nullable String defaultValue);

  /** Returns an int-valued experiment if it exists, else defaultValue. */
  int getExperimentInt(Experiment experiment, int defaultValue);

  /** Starts an experiment scope. During an experiment scope, experiments won't be reloaded. */
  void startExperimentScope();

  /** Ends an experiment scope. */
  void endExperimentScope();

  /** Returns all experiments queried through this service. */
  ImmutableMap<String, Experiment> getAllQueriedExperiments();
}
