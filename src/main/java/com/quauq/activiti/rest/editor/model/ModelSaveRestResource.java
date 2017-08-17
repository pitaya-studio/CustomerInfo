/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quauq.activiti.rest.editor.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.quauq.review.core.engine.ProcessTagService;
import com.quauq.review.core.engine.entity.ProcessTag;

/**
 * @author Tijs Rademakers
 */
@Controller
public class ModelSaveRestResource implements ModelDataJsonConstants {
  
  protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);

  @Autowired
  private RepositoryService repositoryService;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private ProcessTagService iProcessService;
  
  @RequestMapping(value="/model/{modelId}/save1", method = RequestMethod.PUT)
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public void saveModel(@PathVariable String modelId, @RequestBody MultiValueMap<String, String> values) {
    try {
    	
    	//解析多层json串
		JSONObject jsonObject = new JSONObject(values.getFirst("json_xml"));
		JSONArray jsonArray = jsonObject.getJSONArray("childShapes");
		String jsonStr = jsonObject.toString();
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Object>> maps = objectMapper.readValue(jsonStr, Map.class);
		Map<String, Object> propertiesStr = maps.get("properties");
		JSONObject object = jsonObject.getJSONObject("properties");
		String process_id = propertiesStr.get("process_id").toString();
		
		if (null != jsonArray && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject childJson = (JSONObject)jsonArray.get(i);
				Object proJson = childJson.get("properties");
				if (null != proJson && !"".equals(proJson)) {
					JSONObject idTagJson = (JSONObject)proJson;
					if(idTagJson !=null && idTagJson.length()>0){
						//判断是否是人工任务 (包含ID和收据标签)
						boolean a = idTagJson.toString().contains("overrideid");
						boolean b = idTagJson.toString().contains("receipttag");
						if(a&&b){
							String id = idTagJson.get("overrideid").toString();
							String receipttag = idTagJson.get("receipttag").toString();
							ProcessTag processTag = new ProcessTag();
							processTag.setProcessKey(process_id);
							processTag.setActivityId(id);
							processTag.setTagId(receipttag);
//							iProcessService.save(processTag);
						}
					}
				}
			}
		}
		
      Model model = repositoryService.getModel(modelId);
      
      ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
      
      modelJson.put(MODEL_NAME, values.getFirst("name"));
      modelJson.put(MODEL_DESCRIPTION, values.getFirst("description"));
      model.setMetaInfo(modelJson.toString());
      model.setName(values.getFirst("name"));
      
      repositoryService.saveModel(model);
      
      repositoryService.addModelEditorSource(model.getId(), values.getFirst("json_xml").getBytes("utf-8"));
      
      InputStream svgStream = new ByteArrayInputStream(values.getFirst("svg_xml").getBytes("utf-8"));
      TranscoderInput input = new TranscoderInput(svgStream);
      
      PNGTranscoder transcoder = new PNGTranscoder();
      // Setup output
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      TranscoderOutput output = new TranscoderOutput(outStream);
      
      // Do the transformation
      transcoder.transcode(input, output);
      final byte[] result = outStream.toByteArray();
      repositoryService.addModelEditorSourceExtra(model.getId(), result);
      outStream.close();
      
    } catch (Exception e) {
      LOGGER.error("Error saving model", e);
      throw new ActivitiException("Error saving model", e);
    }
  }
}
