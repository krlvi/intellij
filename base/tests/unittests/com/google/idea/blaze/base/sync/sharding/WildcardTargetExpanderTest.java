/*
 * Copyright 2017 The Bazel Authors. All rights reserved.
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
package com.google.idea.blaze.base.sync.sharding;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.idea.blaze.base.BlazeTestCase;
import com.google.idea.blaze.base.model.primitives.TargetExpression;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Test that targets are correctly partitioned in {@link WildcardTargetExpander#shardTargets}. */
@RunWith(JUnit4.class)
public class WildcardTargetExpanderTest extends BlazeTestCase {

  private static TargetExpression target(String expression) {
    return Preconditions.checkNotNull(TargetExpression.fromStringSafe(expression));
  }

  @Test
  public void testShardSizeRespected() {
    List<TargetExpression> targets =
        ImmutableList.of(
            target("//java/com/google:one"),
            target("//java/com/google:two"),
            target("//java/com/google:three"),
            target("//java/com/google:four"),
            target("//java/com/google:five"));
    List<ImmutableList<TargetExpression>> shards = WildcardTargetExpander.shardTargets(targets, 2);
    assertThat(shards).hasSize(3);
    assertThat(shards.get(0)).hasSize(2);
    assertThat(shards.get(1)).hasSize(2);
    assertThat(shards.get(2)).hasSize(1);

    shards = WildcardTargetExpander.shardTargets(targets, 4);
    assertThat(shards).hasSize(2);
    assertThat(shards.get(0)).hasSize(4);
    assertThat(shards.get(1)).hasSize(1);

    shards = WildcardTargetExpander.shardTargets(targets, 100);
    assertThat(shards).hasSize(1);
    assertThat(shards.get(0)).hasSize(5);
  }

  @Test
  public void testAllSubsequentExcludedTargetsAppendedToShards() {
    List<TargetExpression> targets =
        ImmutableList.of(
            target("//java/com/google:one"),
            target("-//java/com/google:two"),
            target("//java/com/google:three"),
            target("-//java/com/google:four"),
            target("//java/com/google:five"),
            target("-//java/com/google:six"));
    List<ImmutableList<TargetExpression>> shards = WildcardTargetExpander.shardTargets(targets, 3);
    assertThat(shards).hasSize(2);
    assertThat(shards.get(0)).hasSize(5);
    assertThat(shards.get(0))
        .isEqualTo(
            ImmutableList.of(
                target("//java/com/google:one"),
                target("-//java/com/google:two"),
                target("//java/com/google:three"),
                target("-//java/com/google:four"),
                target("-//java/com/google:six")));
    assertThat(shards.get(1)).hasSize(3);
    assertThat(shards.get(1))
        .containsExactly(
            target("-//java/com/google:four"),
            target("//java/com/google:five"),
            target("-//java/com/google:six"))
        .inOrder();

    shards = WildcardTargetExpander.shardTargets(targets, 1);
    assertThat(shards).hasSize(3);
    assertThat(shards.get(0))
        .containsExactly(
            target("//java/com/google:one"),
            target("-//java/com/google:two"),
            target("-//java/com/google:four"),
            target("-//java/com/google:six"))
        .inOrder();
  }

  @Test
  public void testShardWithOnlyExcludedTargetsIsDropped() {
    List<TargetExpression> targets =
        ImmutableList.of(
            target("//java/com/google:one"),
            target("//java/com/google:two"),
            target("//java/com/google:three"),
            target("-//java/com/google:four"),
            target("-//java/com/google:five"),
            target("-//java/com/google:six"));

    List<ImmutableList<TargetExpression>> shards = WildcardTargetExpander.shardTargets(targets, 3);

    assertThat(shards).hasSize(1);
    assertThat(shards.get(0)).hasSize(6);
  }
}
