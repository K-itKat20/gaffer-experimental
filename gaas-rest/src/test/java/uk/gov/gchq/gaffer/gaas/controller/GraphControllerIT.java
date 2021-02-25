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
package uk.gov.gchq.gaffer.gaas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.gchq.gaffer.gaas.AbstractTest;
import uk.gov.gchq.gaffer.gaas.model.Graph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
public class GraphControllerIT extends AbstractTest {

    @Test
    public void authEndpointShouldReturn200StatusAndTokenWhenValidUsernameAndPassword() throws Exception {
        final String authRequest = "{\"username\":\"javainuse\",\"password\":\"password\"}";

        final MvcResult result = mvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(authRequest)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(179, result.getResponse().getContentAsString().length());
    }

    @Test
    public void testAddGraphReturns201OnSuccess() throws Exception {
        final Graph graph = new Graph(TEST_GRAPH_ID, TEST_GRAPH_DESCRIPTION);
        final String inputJson = mapToJson(graph);
        final MvcResult mvcResult = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(inputJson)).andReturn();
        final int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @Test
    public void testAddGraphNotNullShouldReturn400() throws Exception {
        final String graphRequest = "{\"description\":\"password\"}";
        final MvcResult mvcResult = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(graphRequest)).andReturn();
        final int status = mvcResult.getResponse().getStatus();
        assertEquals("{\"message\":\"Validation failed\",\"details\":\"Graph id should not be null\"}", mvcResult.getResponse().getContentAsString());
        assertEquals(400, status);
    }

    @Test
    public void testAddGraphWithSameGraphIdShouldReturn409() throws Exception {
        final String graphRequest = "{\"graphId\":\"" + TEST_GRAPH_ID + "\",\"description\":\"" + TEST_GRAPH_DESCRIPTION + "\"}";
        mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(graphRequest)).andReturn();

        final MvcResult mvcResult = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(graphRequest)).andReturn();

        assertEquals("{\"message\":\"gaffers.gchq.gov.uk \\\"testgraphid\\\" already exists\",\"details\":\"AlreadyExists\"}", mvcResult.getResponse().getContentAsString());
        assertEquals(409, mvcResult.getResponse().getStatus());
    }

    @Test
    public void getGraphEndpointReturnsGraph() throws Exception {
        final String graphRequest = "{\"graphId\":\"" + TEST_GRAPH_ID + "\",\"description\":\"" + TEST_GRAPH_DESCRIPTION + "\"}";
        final MvcResult addGraphResponse = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(graphRequest)).andReturn();
        assertEquals(201, addGraphResponse.getResponse().getStatus());

        final MvcResult getGraphsResponse = mvc.perform(get("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token))
                .andReturn();

        assertEquals(200, getGraphsResponse.getResponse().getStatus());
        //assertTrue(getGraphsResponse.getResponse().getContentAsString().contains("testgraphid"));
        //assertEquals("[{\"graphId\":\"\\\"testgraphid\\\"\",\"description\":\"\\\"Test Graph Description\\\"\"}]", getGraphsResponse.getResponse().getContentAsString());
    }

    @Test
    public void testGraphIdWithSpacesShouldReturn400() throws Exception {
        final String graphRequest = "{\"graphId\":\"some graph \",\"description\":\"" + TEST_GRAPH_DESCRIPTION + "\"}";
        final MvcResult mvcResult = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(graphRequest)).andReturn();
        final int status = mvcResult.getResponse().getStatus();
        assertEquals("{\"message\":\"Validation failed\",\"details\":\"Graph can contain only digits,lowercase letters or _ \"}", mvcResult.getResponse().getContentAsString());
        assertEquals(400, status);
    }

    @Test
    public void testGraphIdWithSpecialCharactersShouldReturn400() throws Exception {
        final String graphRequest = "{\"graphId\":\"some!!!!graph@@\",\"description\":\"" + TEST_GRAPH_DESCRIPTION + "\"}";

        final MvcResult mvcResult = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(graphRequest)).andReturn();

        assertEquals("{\"message\":\"Validation failed\",\"details\":\"Graph can contain only digits,lowercase letters or _ \"}", mvcResult.getResponse().getContentAsString());
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGraphIdWitCapitalLettersShouldReturn400() throws Exception {
        final String graphRequest = "{\"graphId\":\"SomeGraph\",\"description\":\"" + TEST_GRAPH_DESCRIPTION + "\"}";

        final MvcResult mvcResult = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(graphRequest)).andReturn();

        assertEquals("{\"message\":\"Validation failed\",\"details\":\"Graph can contain only digits,lowercase letters or _ \"}", mvcResult.getResponse().getContentAsString());
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDescriptionEmptyShouldReturn400() throws Exception {
        final String graphRequest = "{\"graphId\":\"" + TEST_GRAPH_ID + "\",\"description\":\"\"}";
        final MvcResult mvcResult = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(graphRequest)).andReturn();
        final int status = mvcResult.getResponse().getStatus();
        assertEquals("{\"message\":\"Validation failed\",\"details\":\"Description should not be empty\"}", mvcResult.getResponse().getContentAsString());
        assertEquals(400, status);
    }

    @Test
    public void testDeleteShouldReturn200AndRemoveCRD() throws Exception {
        //given we have a graph
        final Graph graph = new Graph(TEST_GRAPH_ID, TEST_GRAPH_DESCRIPTION);
        final String inputJson = mapToJson(graph);
        final MvcResult mvcResult = mvc.perform(post("/graphs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .content(inputJson)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());

        //when delete graph
        final MvcResult mvcResult2 = mvc.perform(delete("/graphs/" + TEST_GRAPH_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token))
                .andReturn();

        //then have no graphs / 200 return
        assertEquals(204, mvcResult2.getResponse().getStatus());
    }

    @Test
    public void testDeleteShouldReturn404WhenGraphNotExisting() throws Exception {
        //when delete graph
        final MvcResult mvcResult2 = mvc.perform(delete("/graphs/nonexistentgraphfortestingpurposes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token))
                .andReturn();

        //then have no graphs / 200 return
        assertEquals(404, mvcResult2.getResponse().getStatus());
    }
}
