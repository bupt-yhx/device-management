package cn.edu.bupt.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.edu.bupt.utils.HttpUtil;

/**
 * Created by liyou on 2018/1/15.
 */

@RestController
@RequestMapping("/api/data")
public class DataController extends DefaultThingsboardAwaredController{

    @RequestMapping(value="/getKeyData/{deviceId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public String getKeyData(@PathVariable("deviceId") String deviceId) {
        String requestKeyAddr = "/api/plugins/telemetry/DEVICE/" + deviceId + "/keys/TIMESERIES";

        String responseKeyContent = null;
        try {
            responseKeyContent = HttpUtil.sendGetToThingsboard("http://" + getServer() + requestKeyAddr,
                    null,
                    request.getSession());
        } catch (Exception e) {
            return retFail(e.toString());
        }
        return retSuccess(responseKeyContent);
    }
    @RequestMapping(value="/getlatestData/{deviceId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public String getlatestData(@PathVariable("deviceId") String deviceId)
    {
        String responseKeyContent=getKeyData(deviceId);
        responseKeyContent=responseKeyContent.replaceAll("[\\[\\]]","");
        responseKeyContent=responseKeyContent.replaceAll("\"","");

        String requestHistoricalDataAddr = "/api/plugins/telemetry/DEVICE/"+ deviceId
                + "/values/TIMESERIES?keys="+responseKeyContent;

        String responseHistoricalDataContent = null ;
        try {
            responseHistoricalDataContent = HttpUtil.sendGetToThingsboard("http://" + getServer() + requestHistoricalDataAddr,
                    null,
                    request.getSession());
        } catch (Exception e) {
            return retFail(e.toString()) ;
        }

        return retSuccess(responseHistoricalDataContent);
    }
    @RequestMapping(value="/getHistoricalData/{deviceId}/{startTime}/{endTime}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public String getHistoricalData(@PathVariable("deviceId") String deviceId,@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime)
    {
        String responseKeyContent=getKeyData(deviceId);
        responseKeyContent=responseKeyContent.replaceAll("[\\[\\]]","");
        responseKeyContent=responseKeyContent.replaceAll("\"","");

        String requestHistoricalDataAddr = "/api/plugins/telemetry/DEVICE/"+ deviceId
                + "/values/TIMESERIES?keys="
                + "&startTs="+startTime
                + "&endTs="+endTime
                + "&interval=0&limit=100";

        String responseHistoricalDataContent = null ;
        try {
            responseHistoricalDataContent = HttpUtil.sendGetToThingsboard("http://" + getServer() + requestHistoricalDataAddr,
                    null,
                    request.getSession());
        } catch (Exception e) {
            return retFail(e.toString()) ;
        }

        return retSuccess(responseHistoricalDataContent);
    }

    @RequestMapping(value="/getKeyAttribute/{deviceId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public String getKeyAttributes(@PathVariable("deviceId") String deviceId) {
        String requestKeyAddr = "/api/plugins/telemetry/DEVICE/" + deviceId + "/keys/ATTRIBUTES";

        String responseKeyAttributeContent = null;
        try {
            responseKeyAttributeContent = HttpUtil.sendGetToThingsboard("http://" + getServer() + requestKeyAddr,
                    null,
                    request.getSession());
        } catch (Exception e) {
            return retFail(e.toString());
        }
        return retSuccess(responseKeyAttributeContent);
    }

    @ApiOperation(value="test - tjl", notes="根据device的id来指定操作")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value="/getAttribute/{deviceId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public String getValueAttribute(@PathVariable("deviceId") String deviceId)
    {
        String responseKeyAttributeContent=getKeyAttributes(deviceId);

        responseKeyAttributeContent=responseKeyAttributeContent.replaceAll("[\\[\\]]","");
        responseKeyAttributeContent=responseKeyAttributeContent.replaceAll("\"","");

        String requestHistoricalDataAddr = "/api/plugins/telemetry/DEVICE/"+ deviceId
                + "/values/ATTRIBUTES?keys=" +responseKeyAttributeContent;

        String responseAttributeDataContent = null ;
        try {
            responseAttributeDataContent = HttpUtil.sendGetToThingsboard("http://" + getServer() + requestHistoricalDataAddr,
                    null,
                    request.getSession());
        } catch (Exception e) {
            return retFail(e.toString()) ;
        }

        return retSuccess(responseAttributeDataContent);
    }
}
