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

package uk.gov.gchq.gaffer.gaas.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * <b>GaaS: Create Gaffer Request Body</b>
 */
public class GaaSCreateRequestBody implements Serializable {

    @NotNull(message = "Graph id should not be null")
    @NotBlank(message = "Graph id should not be null")
    @Pattern(regexp = "[a-z0-9]*$", message = "Graph ID can contain only digits or lowercase letters")
    private String graphId;
    @NotBlank(message = "Description should not be empty")
    private String description;
    @NotNull(message = "\"configName\" must be defined. Valid config names can be found at /storetypes endpoint")
    private String configName;
    private Map<String, Object> schema;

    public GaaSCreateRequestBody() {
    }

    public GaaSCreateRequestBody(final String graphId, final String description, final Map<String, Object> schema, final String configName) {
        this.graphId = graphId;
        this.description = description;
        this.configName = configName;
        this.schema = schema;
    }

    public String getGraphId() {
        return graphId;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getSchema() {
        return schema;
    }

    public String getConfigName() {
        return configName;
    }

}
