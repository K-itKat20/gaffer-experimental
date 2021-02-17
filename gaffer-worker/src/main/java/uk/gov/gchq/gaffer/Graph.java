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
package uk.gov.gchq.gaffer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Graph {

    @NotBlank(message = "Graph id should not be null")
    @NotNull(message = "Graph id should not be null")
    @Pattern(regexp = "([0-9|a-z|_|])*", message = "Graph can contain only digits,lowercase letters or _ ")
    private String graphId;
    @NotBlank(message = "Description should not be empty")
    private String description;

    public Graph() {
    }

    public Graph(final String graphId, final String description) {
        this.graphId = graphId;
        this.description = description;

    }


    public void setGraphId(final String graphId) {
        this.graphId = graphId;
    }

    public String getGraphId() {
        return graphId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}