package com.topica.checking.service.util;

import com.topica.checking.service.AnswerService;
import com.topica.checking.service.FileStorageServices;
import com.topica.checking.service.QuestionService;
import com.topica.checking.service.dto.AnswerDTO;
import com.topica.checking.service.dto.QuestionDTO;
import com.topica.checking.service.dto.SourceDTO;
import com.topica.checking.web.rest.util.FetchingAPI;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class CallApiRunner implements Runnable {

    List<Object[]> resultSet;
    Object[] str;
    String url;
    AnswerService answerService;
    QuestionService questionService;
    Integer res;
    String apiURL;

    public CallApiRunner(List<Object[]> resultSet, Object[] str, String url, AnswerService answerService, QuestionService questionService, Integer res, String apiURL) {
        this.resultSet = resultSet;
        this.str = str;
        this.url = url;
        this.answerService = answerService;
        this.questionService = questionService;
        this.res = res;
        this.apiURL = apiURL;
    }

    @Override
    public void run() {
        FetchingAPI fetchingAPI = new FetchingAPI();
        byte[] imageBytes = new byte[0];
        try {
            System.out.println(this.str[2].toString());
            String restAPIUrl = "";
            if(!this.str[2].toString().contains("http")){
                restAPIUrl = "http://"+this.str[2].toString();
            }else{
                restAPIUrl  = this.str[2].toString();
            }
            imageBytes = IOUtils.toByteArray(new URL(restAPIUrl));
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            JSONObject response = fetchingAPI.callApi(base64,apiURL );
            System.out.println(response + "\t RESPONSE");
            if (response != null) {
                JSONArray array = response.getJSONArray("result");
                if (array.length() > 0) {
                    this.str[4] = 1;
                    for (int i = 0; i < array.length(); i++) {
                                JSONObject entry = array.getJSONObject(i);
                                QuestionDTO questionDTO = new QuestionDTO();
                                questionDTO.setQuestion_text((String) entry.get("question"));
                                questionDTO.setSourceId(res.longValue());
                                this.str[3]= url+res;
                                questionDTO.setVisible(1);
                                QuestionDTO quesRes = questionService.save(questionDTO);

                                AnswerDTO answerDTO = new AnswerDTO();
                                answerDTO.setAnswer_text(entry.get("answer").toString().replace("format1//",""));
                                answerDTO.setQuestionId(quesRes.getId());
                                answerDTO.setReviewer_id(1);
                                answerDTO.setStatus(1);
                                answerDTO.setUser_id(1);
                                answerService.save(answerDTO);


                    }
                } else {
                    this.str[4] = 0;
                }
            } else {
                this.str[4] = -1;
            }
            resultSet.add(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
