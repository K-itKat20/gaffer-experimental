/*
 * Copyright 2017-2020 Crown Copyright
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
package uk.gov.gchq.gaffer.gafferpop.generator;

import org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality;
import org.junit.Test;
import uk.gov.gchq.gaffer.commonutil.TestGroups;
import uk.gov.gchq.gaffer.commonutil.TestPropertyNames;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.gafferpop.GafferPopGraph;
import uk.gov.gchq.gaffer.gafferpop.GafferPopVertex;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GafferEntityGeneratorTest {
    @Test
    public void shouldConvertGafferPopVertexToGafferEntity() {
        // Given
        final GafferPopGraph graph = mock(GafferPopGraph.class);

        final String vertex = "vertex";
        final String propValue = "property value";
        final GafferPopVertex gafferPopVertex = new GafferPopVertex(TestGroups.ENTITY, vertex, graph);
        gafferPopVertex.property(Cardinality.list, TestPropertyNames.STRING, propValue);

        final GafferEntityGenerator generator = new GafferEntityGenerator();

        // When
        final Entity entity = generator._apply(gafferPopVertex);

        // Then
        assertEquals(TestGroups.ENTITY, entity.getGroup());
        assertEquals(vertex, entity.getVertex());
        assertEquals(1, entity.getProperties().size());
        assertEquals(propValue, entity.getProperty(TestPropertyNames.STRING));
    }

}
