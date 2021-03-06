package cn.edu.bupt.controller;

import cn.edu.bupt.data.CachForDeviceService;
import cn.edu.bupt.utils.HttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Administrator on 2017/12/23.
 *
 *  -- 该类的所有接口返回采用统一json
 */
@RestController
@RequestMapping("/api/shadow")
@Slf4j
public class ShadowController extends DefaultThingsboardAwaredController {
    
    @RequestMapping(value = "/{deviceId}",method = RequestMethod.GET)
    public String getDeviceShadow(@PathVariable("deviceId") String deviceId){
        String url = "http://"+getServer()+"/api/shadow/"+deviceId;
        JsonObject body = new JsonObject();
        body.addProperty("requestName","get");
//        JsonObject res = new JsonObject();
        try{
            String s = HttpUtil.sendPostToThingsboard(url,null,body,request.getSession());
//            res.addProperty("responce_code",0);
            JsonObject obj = new JsonParser().parse(s).getAsJsonObject();
            CachForDeviceService.put(deviceId,obj);
//            res.add("responce_msg",obj);
            return responceUtil.onSuccess(obj) ;
        }catch(Exception e){
            return retFail(e.toString());
        }
    }
    @RequestMapping(value = "/task/list/{deviceId}",method = RequestMethod.GET)
    public String taskLists(@PathVariable("deviceId") String deviceId){
        String url = "http://"+getServer()+"/api/shadow/list/"+deviceId;
//        JsonObject res = new JsonObject();
        try{
            String s = HttpUtil.sendGetToThingsboard(url,null,request.getSession());
    //        JsonObject obj = new JsonParser().parse(s).getAsJsonObject();
            return  retSuccess(s);
        }catch(Exception e){
            return retFail(e.toString());
        }
    }

    @RequestMapping(value = "/task/cancel/{deviceId}/{taskId}",method = RequestMethod.GET)
    public String cancelTask(@PathVariable("deviceId") String deviceId,@PathVariable String taskId){
        String url = "http://"+getServer()+"/api/shadow/cancel/"+deviceId+"/"+taskId;
        try{
            String s = HttpUtil.sendGetToThingsboard(url,null,request.getSession());
           // JsonObject obj = new JsonParser().parse(s).getAsJsonObject();
         // return responceUtil.onSuccess(obj) ;
           return  retSuccess(s);
        }catch(Exception e){
            return retFail(e.toString());
        }
    }




    @RequestMapping(value = "/control/{deviceId}",method = RequestMethod.POST)
    public String controlDevice(@RequestBody String bd,@PathVariable("deviceId") String deviceId){
        String url ;
        JsonObject body = new JsonObject();
        body.addProperty("requestName","serviceCall");
        JsonObject paramsAndServiceName = new JsonParser().parse(bd).getAsJsonObject();

        if(paramsAndServiceName.has("startTime")){
            if(paramsAndServiceName.has("period")){
                url =  "http://"+getServer()+"/api/shadow/"+deviceId+"/period/"+
                        paramsAndServiceName.get("startTime").getAsString()+"/"+paramsAndServiceName.get("period").getAsString();
                paramsAndServiceName.remove("startTime");
                paramsAndServiceName.remove("period");
            }else{
                url =  "http://"+getServer()+"/api/shadow/"+deviceId+"/delay/"+
                        paramsAndServiceName.get("startTime").getAsString();
                paramsAndServiceName.remove("startTime");
            }
        }else{
            url = "http://"+getServer()+"/api/shadow/"+deviceId;
        }

//        String serviceNmae = paramsAndServiceName.get("serviceName").getAsString();
//        JsonObject service = CachForDeviceService.get(deviceId,serviceNmae);
//        paramsAndServiceName.remove("serviceName");
//        service.get("serviceBody").getAsJsonObject().add("params",paramsAndServiceName);
//        body.add("requestBody",service);
        body.add("requestBody",paramsAndServiceName);
//        JsonObject res = new JsonObject();
        try{
            String s = HttpUtil.sendPostToThingsboard(url,null,body,request.getSession());
//            res.addProperty("responce_code",0);
//            res.addProperty("responce_msg",s);
            return retSuccess(s);
        }catch(Exception e){
            return retFail(e.toString());
        }
    }
}
