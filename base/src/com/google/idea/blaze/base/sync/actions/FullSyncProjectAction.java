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
package com.google.idea.blaze.base.sync.actions;

import com.google.idea.blaze.base.actions.BlazeProjectAction;
import com.google.idea.blaze.base.sync.BlazeSyncManager;
import com.google.idea.blaze.base.sync.status.BlazeSyncStatus;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;

/**
 * Performs a full sync. Unlike an incremental sync, this completely wipes all previous project
 * data, starting from scratch.
 *
 * <p>It should only be needed to manually work around bugs in incremental sync.
 */
public class FullSyncProjectAction extends BlazeProjectAction {

  @Override
  protected void actionPerformedInBlazeProject(Project project, AnActionEvent e) {
    BlazeSyncManager.getInstance(project).fullProjectSync();
    updateStatus(project, e);
  }

  @Override
  protected void updateForBlazeProject(Project project, AnActionEvent e) {
    updateStatus(project, e);
  }

  private static void updateStatus(Project project, AnActionEvent e) {
    Presentation presentation = e.getPresentation();
    presentation.setEnabled(!BlazeSyncStatus.getInstance(project).syncInProgress());
  }
}
