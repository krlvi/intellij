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
package com.google.idea.blaze.base.sync;

import com.google.auto.value.AutoValue;
import com.google.idea.blaze.base.command.info.BlazeInfo;
import com.google.idea.blaze.base.model.BlazeVersionData;
import com.google.idea.blaze.base.projectview.ProjectViewSet;
import com.google.idea.blaze.base.sync.projectview.WorkspaceLanguageSettings;
import com.google.idea.blaze.base.sync.workspace.WorkingSet;
import com.google.idea.blaze.base.sync.workspace.WorkspacePathResolver;
import javax.annotation.Nullable;

/** Project and sync state shared between multiple phases of sync. */
@AutoValue
public abstract class SyncProjectState {

  /**
   * Merges this {@link SyncProjectState} with the state associated with a more recently started
   * build phase.
   */
  public SyncProjectState updateState(SyncProjectState nextResult) {
    // take the most recent version of the project data
    return nextResult.toBuilder()
        .setSyncParams(BlazeSyncParams.combine(getSyncParams(), nextResult.getSyncParams()))
        .build();
  }

  public abstract BlazeSyncParams getSyncParams();

  public abstract ProjectViewSet getProjectViewSet();

  public abstract WorkspaceLanguageSettings getLanguageSettings();

  public abstract BlazeInfo getBlazeInfo();

  public abstract BlazeVersionData getBlazeVersionData();

  @Nullable
  public abstract WorkingSet getWorkingSet();

  public abstract WorkspacePathResolver getWorkspacePathResolver();

  public static Builder builder() {
    return new AutoValue_SyncProjectState.Builder();
  }

  private Builder toBuilder() {
    return builder()
        .setSyncParams(getSyncParams())
        .setProjectViewSet(getProjectViewSet())
        .setLanguageSettings(getLanguageSettings())
        .setBlazeInfo(getBlazeInfo())
        .setBlazeVersionData(getBlazeVersionData())
        .setWorkingSet(getWorkingSet())
        .setWorkspacePathResolver(getWorkspacePathResolver());
  }

  /** A builder for {@link SyncProjectState} objects. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setSyncParams(BlazeSyncParams params);

    public abstract Builder setProjectViewSet(ProjectViewSet projectViewSet);

    public abstract Builder setLanguageSettings(WorkspaceLanguageSettings languageSettings);

    public abstract Builder setBlazeInfo(BlazeInfo blazeInfo);

    public abstract Builder setBlazeVersionData(BlazeVersionData blazeVersionData);

    public abstract Builder setWorkingSet(@Nullable WorkingSet workingSet);

    public abstract Builder setWorkspacePathResolver(WorkspacePathResolver pathResolver);

    public abstract SyncProjectState build();
  }
}
