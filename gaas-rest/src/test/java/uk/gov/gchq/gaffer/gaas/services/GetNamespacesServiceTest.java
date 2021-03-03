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

package uk.gov.gchq.gaffer.gaas.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.gchq.gaffer.gaas.client.CRDClient;
import uk.gov.gchq.gaffer.gaas.exception.GaaSRestApiException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GetNamespacesServiceTest {

    @Autowired
    private GetNamespacesService getNamespacesService;

    @MockBean
    CRDClient crdClient;

    @Test
    public void shouldReturnNamespacesAsAListOfString_whenCrdClientIsSuccessful() throws GaaSRestApiException {
        final List<String> list = Arrays.asList("my-production-env-1", "my-test-env-3");
        when(crdClient.getAllNameSpaces()).thenReturn(list);

        final List<String> actual = getNamespacesService.getNamespaces();

        assertTrue(actual.contains("my-production-env-1"));
        assertTrue(actual.contains("my-test-env-3"));
    }
}