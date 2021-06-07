/*
 * Copyright 2021 Crown Copyright
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import uk.gov.gchq.gaffer.gaas.exception.GaaSRestApiException;
import uk.gov.gchq.gaffer.gaas.model.GaaSCreateRequestBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PropertiesLoader {

  @Autowired
  private ResourceLoader resourceLoader;

  public void loadStoreProperties(final GaaSCreateRequestBody graph) throws GaaSRestApiException {

    try {
      String storeType = graph.getStoreType();
      Yaml yaml = new Yaml();

      Map<String, Object> storeProperties = new HashMap<>();
      Resource[] resources = loadResources("classpath*:yaml/" + storeType + ".yaml");

      if(resources.length == 0){
        unSupportedStoreType();
      }
      
      for (final Resource resource : resources) {
        storeProperties = yaml.load(resource.getInputStream());
        graph.setStoreProperties(storeProperties);
      }

    } catch (IOException e) {
      throw new GaaSRestApiException(e.getLocalizedMessage(), e.getMessage(), 500);
    }

  }

  private void unSupportedStoreType()throws  IOException {
    ArrayList<String> storeTypes = new ArrayList<>();
    Resource[] resources = loadResources("classpath*:yaml/*.yaml");
    for (final Resource resource : resources) {
      String filename = resource.getFilename().split("\\.", 2)[0];
      storeTypes.add(filename);
    }
    throw new RuntimeException("StoreType is Invalid must be defined Valid Store Types supported are: " + storeTypes.toString().replaceAll("\\[|\\]", ""));

  }


  private  Resource[]  loadResources(final String pattern) throws  IOException {
      Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
     return resources;
  }


  public List<String> getStoreTypesAsStringList()throws  IOException {
    List<String> storeTypes = new ArrayList<>();
    Resource[] resources = loadResources("classpath*:yaml/*.yaml");
    for (final Resource resource : resources) {
      String filename = resource.getFilename().split("\\.", 2)[0];
      storeTypes.add(filename);
    }
    return storeTypes;
  }

}