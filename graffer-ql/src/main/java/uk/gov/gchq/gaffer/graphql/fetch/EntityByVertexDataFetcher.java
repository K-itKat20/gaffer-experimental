/*
 * Copyright 2016 Crown Copyright
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
package uk.gov.gchq.gaffer.graphql.fetch;

import graphql.schema.DataFetchingEnvironment;
import uk.gov.gchq.gaffer.graphql.definitions.Constants;
import java.util.Map;

/**
 * Fetch entities based on the source object, which we expect to be a vertex.
 */
public class EntityByVertexDataFetcher extends EntityDataFetcher {

    public EntityByVertexDataFetcher(final String group) {
        super(group);
    }

    @Override
    protected String getVertex(final DataFetchingEnvironment environment) {
        final Map<String, Object> source = (Map<String, Object>) environment.getSource();
        return source.get(Constants.VALUE).toString();
    }
}
