/*
 * Copyright 2017 Crown Copyright
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
package uk.gov.gchq.gaffer.flink;

import uk.gov.gchq.gaffer.commonutil.StreamUtil;
import uk.gov.gchq.gaffer.flink.operation.AddElementsFromSocket;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.user.User;

/**
 * To run the demo:
 * <pre>
 * Use netcat to start a local server. In a terminal run the command: nc -l 9000
 * Run this main method via your IDE
 * In the terminal running netcat, add some elements, e.g:
 * 1,2
 * 1,3
 * 3,2
 * They must be in the format [source],[destination].
 * </pre>
 * You should see the elements have been added to Gaffer and logged in the IDE terminal
 */
public class AddElementsFromSocketDemo {
    public static void main(String[] args) throws Exception {
        new AddElementsFromSocketDemo().run();
    }

    private void run() throws OperationException {
        final Graph graph = new Graph.Builder()
                .storeProperties(StreamUtil.openStream(getClass(), "mockaccumulostore.properties"))
                .addSchemas(StreamUtil.schemas(getClass()))
                .build();

        graph.execute(new AddElementsFromSocket.Builder()
                .generator(new CsvToElement())
                .hostname("localhost")
                .port(9000)
                .build(), new User());
    }
}