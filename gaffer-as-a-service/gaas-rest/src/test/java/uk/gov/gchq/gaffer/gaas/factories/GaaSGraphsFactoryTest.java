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

package uk.gov.gchq.gaffer.gaas.factories;

import io.kubernetes.client.openapi.models.V1ObjectMeta;
import org.junit.jupiter.api.Test;
import uk.gov.gchq.gaffer.common.model.v1.Gaffer;
import uk.gov.gchq.gaffer.common.model.v1.GafferSpec;
import uk.gov.gchq.gaffer.common.model.v1.GafferStatus;
import uk.gov.gchq.gaffer.common.model.v1.RestApiStatus;
import uk.gov.gchq.gaffer.gaas.model.GaaSGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.gchq.gaffer.gaas.util.Constants.DESCRIPTION_KEY;
import static uk.gov.gchq.gaffer.gaas.util.Constants.GRAPH_ID_KEY;
import static uk.gov.gchq.gaffer.gaas.util.Constants.INGRESS_API_PATH_KEY;
import static uk.gov.gchq.gaffer.gaas.util.Constants.INGRESS_HOST_KEY;

public class GaaSGraphsFactoryTest {

    @Test
    public void gafferHasValidUpStatus_returnsGaaSGraphWithUpStatus() {
        final GafferSpec graphSpec = getFullValuesGafferSpec();
        final GafferStatus gafferStatus = new GafferStatus().restApiStatus(RestApiStatus.UP);
        final Map<String, Object> gafferList = makeGafferList(graphSpec, gafferStatus);

        final Map<String, List<GaaSGraph>> actual = GaaSGraphsFactory.from(gafferList);
        actual.get("graphs");
        assertEquals(1, actual.get("graphs").size());
        assertEquals("full-values-gaffer", actual.get("graphs").get(0).getGraphId());
        assertEquals(RestApiStatus.UP, actual.get("graphs").get(0).getStatus());
    }

    @Test
    public void gafferHasEmptyStatus_returnsGaaSGraphWithDownStatus() {
        final GafferSpec graphSpec = getFullValuesGafferSpec();
        final GafferStatus gafferStatus = new GafferStatus();
        final Map<String, Object> gafferList = makeGafferList(graphSpec, gafferStatus);

        final Map<String, List<GaaSGraph>> actual = GaaSGraphsFactory.from(gafferList);

        assertEquals(1, actual.get("graphs").size());
        assertEquals("full-values-gaffer", actual.get("graphs").get(0).getGraphId());
        assertEquals(RestApiStatus.DOWN, actual.get("graphs").get(0).getStatus());
    }

    @Test
    public void gafferSpecHasAllValues_andNullStatus_returnsOneGaaSGraphWithDownStatus() {
        final GafferSpec graphSpec = getFullValuesGafferSpec();
        final Map<String, Object> gafferList = makeGafferList(graphSpec);

        final Map<String, List<GaaSGraph>> actual = GaaSGraphsFactory.from(gafferList);

        assertEquals(1, actual.get("graphs").size());
        assertEquals("full-values-gaffer", actual.get("graphs").get(0).getGraphId());
        assertEquals("This is a test gaffer", actual.get("graphs").get(0).getDescription());
        assertEquals("http://apps.my.k8s.cluster/rest", actual.get("graphs").get(0).getUrl());
        assertEquals(RestApiStatus.DOWN, actual.get("graphs").get(0).getStatus());
    }

    @Test
    public void gafferWithGraphIdOnly_fillWithDefaultValues_andDownStatus() {
        final GafferSpec graphSpec = new GafferSpec();
        graphSpec.putNestedObject("hello-gaffer", GRAPH_ID_KEY);
        final Map<String, Object> gafferList = makeGafferList(graphSpec);

        final Map<String, List<GaaSGraph>> actual = GaaSGraphsFactory.from(gafferList);

        assertEquals(1, actual.get("graphs").size());
        assertEquals("hello-gaffer", actual.get("graphs").get(0).getGraphId());
        assertEquals("n/a", actual.get("graphs").get(0).getDescription());
        assertEquals("n/a", actual.get("graphs").get(0).getUrl());
        assertEquals(RestApiStatus.DOWN, actual.get("graphs").get(0).getStatus());
    }

    @Test
    public void gafferSpecHasAllValues_andEmptyProblems_returnsOneGaaSGraphWithEmptyList() {
        final GafferSpec graphSpec = getFullValuesGafferSpec();
        final Map<String, Object> gafferList = makeGafferList(graphSpec);

        final Map<String, List<GaaSGraph>> actual = GaaSGraphsFactory.from(gafferList);

        assertEquals(1, actual.get("graphs").size());
        assertEquals("full-values-gaffer", actual.get("graphs").get(0).getGraphId());
        assertEquals("This is a test gaffer", actual.get("graphs").get(0).getDescription());
        assertEquals("http://apps.my.k8s.cluster/rest", actual.get("graphs").get(0).getUrl());
        assertEquals(RestApiStatus.DOWN, actual.get("graphs").get(0).getStatus());
    }

    @Test
    public void gafferHasProblems_returnsGaaSGraphWithProblems() {
        List<String> problems = new ArrayList<>();
        problems.add("There is a problem with this graph");
        final GafferSpec graphSpec = getFullValuesGafferSpec();
        final GafferStatus gafferStatus = new GafferStatus().problems(problems);
        final Map<String, Object> gafferList = makeGafferList(graphSpec, gafferStatus);

        final Map<String, List<GaaSGraph>> actual = GaaSGraphsFactory.from(gafferList);

        assertEquals(1, actual.get("graphs").size());
        assertEquals("full-values-gaffer", actual.get("graphs").get(0).getGraphId());
        assertEquals(problems, actual.get("graphs").get(0).getProblems());
    }

    @Test
    public void gafferWithNullGraphId_treatAsGraphNonExistentAndExcludeFromList() {
        final Map<String, Object> gafferList = makeGafferList(new GafferSpec());

        final Map<String, List<GaaSGraph>> actual = GaaSGraphsFactory.from(gafferList);

        assertEquals(0, actual.get("graphs").size());
    }

    @Test
    public void gafferWithNullGafferSpec_treatAsGraphNonExistentAndExcludeFromList() {
        final Map<String, Object> gafferList = makeGafferList(null);

        final Map<String, List<GaaSGraph>> actual = GaaSGraphsFactory.from(gafferList);

        assertEquals(0, actual.get("graphs").size());
    }

    @Test
    public void emptyGafferList_returnsZeroGaaSGraphs() {
        final Map<String, Object> gafferList = new LinkedHashMap<>();
        final List<Gaffer> gaffers = new ArrayList<>();
        gafferList.put("graphs", gaffers);

        assertEquals(0, GaaSGraphsFactory.from(gafferList).size());
    }

    private GafferSpec getFullValuesGafferSpec() {
        final GafferSpec graphSpec = new GafferSpec();
        graphSpec.putNestedObject("full-values-gaffer", GRAPH_ID_KEY);
        graphSpec.putNestedObject("This is a test gaffer", DESCRIPTION_KEY);
        graphSpec.putNestedObject("apps.my.k8s.cluster", INGRESS_HOST_KEY);
        graphSpec.putNestedObject("/rest", INGRESS_API_PATH_KEY);
        return graphSpec;
    }

    private Map<String, Object> makeGafferList(final GafferSpec graphSpec) {
        final Map<String, Object> gafferList = new LinkedHashMap<>();
        final List<Gaffer> gaffers = new ArrayList<>();
        HashMap<String, String> labels = new HashMap<>();
        labels.put("configName", "mapStore");
        final V1ObjectMeta metadata = new V1ObjectMeta()
                .name("test")
                .labels(labels);
        gaffers.add(new Gaffer().spec(graphSpec).metaData(metadata));
        gafferList.put("items", gaffers);
        return gafferList;
    }

    private Map<String, Object> makeGafferList(final GafferSpec graphSpec, final GafferStatus gafferStatus) {
        final Map<String, Object> gafferList = new LinkedHashMap<>();
        final List<Gaffer> gaffers = new ArrayList<>();
        HashMap<String, String> labels = new HashMap<>();
        labels.put("configName", "mapStore");
        final V1ObjectMeta metadata = new V1ObjectMeta()
                .name("test")
                .labels(labels);
        gaffers.add(new Gaffer().spec(graphSpec).status(gafferStatus).metaData(metadata));
        gafferList.put("items", gaffers);
        return gafferList;
    }
}
