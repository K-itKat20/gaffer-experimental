/*
 * Copyright 2020 Crown Copyright
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
package uk.gov.gchq.gaffer.gaas.util;

import io.kubernetes.client.openapi.models.V1ObjectMeta;
import uk.gov.gchq.gaffer.common.model.v1.Gaffer;
import uk.gov.gchq.gaffer.common.model.v1.GafferSpec;
import uk.gov.gchq.gaffer.gaas.model.GaaSCreateRequestBody;

public final class GafferKubernetesObjectFactory {

    public static Gaffer from(final GaaSCreateRequestBody graph) {
        final V1ObjectMeta metadata = new V1ObjectMeta().name(graph.getGraphId());

        final GafferSpec gafferSpec = new GafferSpec();
        gafferSpec.putNestedObject(graph.getGraphId(), "graph", "config", "graphId");

        return new Gaffer()
                .apiVersion("gchq.gov.uk" + "/" + "v1")
                .kind("Gaffer")
                .metaData(metadata)
                .spec(gafferSpec);
    }

    private GafferKubernetesObjectFactory() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }
}