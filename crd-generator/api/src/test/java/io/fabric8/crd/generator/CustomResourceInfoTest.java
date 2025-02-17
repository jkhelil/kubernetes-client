/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.crd.generator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.Scope;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;
import org.junit.jupiter.api.Test;

public class CustomResourceInfoTest {

  public static class Spec {

  }

  public static class Status {

  }

  private static final String GROUP = "sample.fabric8.io";
  private static final String VERSION = "v1";

  @Group(GROUP)
  @Version(VERSION)
  @ShortNames("s")
  public static class ClusteredCR extends CustomResource<Spec, Status> {

  }

  @Group(GROUP)
  @Version(VERSION)
  public static class NamespacedCR extends CustomResource<Spec, Status> implements Namespaced {

  }

  @Test
  void shouldBeProperlyScoped() {
    CustomResourceInfo info = CustomResourceInfo.fromClass(ClusteredCR.class);
    assertEquals(GROUP, info.group());
    assertEquals(VERSION, info.version());
    assertEquals(Scope.CLUSTER, info.scope());

    info = CustomResourceInfo.fromClass(NamespacedCR.class);
    assertEquals(GROUP, info.group());
    assertEquals(VERSION, info.version());
    assertEquals(Scope.NAMESPACED, info.scope());
  }

  @Test
  void shouldProperlyCreateCustomResourceInfo() {
    CustomResourceInfo info = CustomResourceInfo.fromClass(ClusteredCR.class);
    assertEquals(GROUP, info.group());
    assertEquals(VERSION, info.version());
    assertEquals(Scope.CLUSTER, info.scope());
    assertEquals(ClusteredCR.class.getCanonicalName(), info.crClassName()); // todo: should we actually use the type name here?
    assertEquals(Spec.class.getTypeName(), info.specClassName().get());
    assertEquals(Status.class.getTypeName(), info.statusClassName().get());
    assertEquals(CustomResource.getSingular(ClusteredCR.class), info.singular());
    assertEquals(CustomResource.getPlural(ClusteredCR.class), info.plural());
    assertEquals(CustomResource.getCRDName(ClusteredCR.class), info.crdName());
    assertArrayEquals(CustomResource.getShortNames(ClusteredCR.class), info.shortNames());
    assertEquals(true, info.served());
    assertEquals(true, info.storage());
    assertEquals(HasMetadata.getKind(ClusteredCR.class), info.kind());
  }
}
