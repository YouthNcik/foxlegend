package com.zhph.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zhph.commons.Constant;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * httpClient助手类，使用的是httpclient-4.4.1版本，各版本有差异
 * @author Roilat-D
 */
public class HttpClientHelper {

	private static Logger log =  LoggerFactory.getLogger(HttpClientHelper.class);
	public static final String CHARSET = Constant.UTF_ENCODING;
	private static final CloseableHttpClient HTTP_CLIENT;
	private static RequestConfig requestConfig;

	static {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(100000).setSocketTimeout(35000).build();
		HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	/**
	 * http post请求 key-value 传参
	 * @param apiUrl
	 * @param params
	 * @return
	 */
	public static String doPost(String apiUrl, Map<String, Object> params) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;

		try {
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				httpStr = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}

	/**
	 * http post请求  json传参调用
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostJSON(String url, String json) {
		CloseableHttpResponse response=null;
		HttpPost post = new HttpPost(url);

		try {
			StringEntity s = new StringEntity(json);
			s.setContentEncoding(CHARSET);
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			response=HTTP_CLIENT.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());// 返回json格式：
				return result;
			}else{
				post.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
		} catch (Exception e) {
			log.error("调用URL连接异常");
			throw new RuntimeException(e);
		}finally {
			if (null!=response) {
				try{
					response.close();
				}catch (Exception ex){
					log.error("json调用接口 关闭response异常"+ex.getMessage());
				}
			}
		}
	}

	/**
	 * postJson返回json或普通字符串
	 * @param postUrl 请求的地址
	 * @param jsonStr 请求的json格式的报文
	 * @return String 返回接口响应的json字符串报文，失败返回null
	 */
	@Deprecated
	public static Json postJson(String postUrl,String jsonStr) throws Exception{
		Json json = new Json();
		if(StringUtil.isEmpty(postUrl)){
			json.setSuccess(false);
			json.setMsg("请求地址为空！!");
			return json;
		}
		
		//TODO 需要进一步重构，目前暂时不处理
		String rs = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(postUrl);
		try {
			StringEntity reqEntity= new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
			post.setEntity(reqEntity);
			log.debug("executing request：{} ",post.getURI());
			CloseableHttpResponse resp = null;
			try {
				resp = httpclient.execute(post);
				StatusLine status = resp.getStatusLine();
				log.debug("respose status：{}",status);
				if(status.getStatusCode()==200){
					HttpEntity entity = resp.getEntity();
					if(entity!=null){
						BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
						rs = EntityUtils.toString(bufferedEntity, "UTF-8");
						json.setObj(rs);
					}
				}
			} catch (ClientProtocolException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			} finally {
				if(resp!=null){
					resp.close();
				}
			}
		} catch (UnsupportedCharsetException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (ParseException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				if(httpclient!=null)
					httpclient.close();
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return json;
	}
	
	/**
	 * post提交表单数据
	 * @param postUrl 提交的地址
	 * @param paramMap 表单参数map
	 * @return 返回响应的内容，失败返回null
	 */
	public static Json postForm(String postUrl ,Map<String,String> paramMap){
		Json json = new Json();
		if(StringUtil.isEmpty(postUrl)){
			json.setSuccess(false);
			json.setMsg("请求地址为空！!");
			return json;
		}
		String rs = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post =new HttpPost(postUrl);
		CloseableHttpResponse resp = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//参数组装
		if(paramMap != null){
			for(Map.Entry<String, String> para : paramMap.entrySet()){
				params.add(new BasicNameValuePair(para.getKey(), para.getValue()));
			}
		}
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
			post.setEntity(entity);
			log.debug("executing request：{} ",post.getURI());
			resp = httpClient.execute(post);
			StatusLine status = resp.getStatusLine();
			log.debug("respose status：{}",status);
			if(status.getStatusCode()==200){
				HttpEntity hEntity = resp.getEntity();
				if(hEntity!=null){
					BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(hEntity);
					rs = EntityUtils.toString(bufferedEntity, "UTF-8");
					json.setObj(rs);
					json.setSuccess(true);
				}
			}else{//other httpStatus
				json.setSuccess(false);
				HttpEntity hEntity = resp.getEntity();
				if(hEntity!=null){
					try {
						BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(hEntity);
						rs = EntityUtils.toString(bufferedEntity, "UTF-8");
					} catch (Exception e) {
						log.error("http请求异常时对响应体处理失败：{}",e.getMessage());
					}
					json.setObj(rs);
					json.setMsg("请求结果错误，http返回状态是：" + status.getStatusCode());
					json.setSuccess(false);
				}
			}
		} catch (Exception e){
			log.error("post请求失败：{}",e.getMessage());
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("请求异常！异常原因："+e.getMessage());
		}finally{
			try {
				if(resp!=null)
					resp.close();
				if(httpClient!=null)
					httpClient.close();
			} catch (IOException e) {
				log.error("资源关闭失败：{}",e.getMessage());
				e.printStackTrace();
			}
		}
		return json;
	}
	
	/**
	 * 发送get请求
	 * @param getUrl get请求地址
	 * @param paramMap 参数map
	 * @return 响应内容
	 */
	public static String requestMethodByGet(String getUrl,Map<String,String> paramMap){
		// TODO
		return null;
	}
	
	/**
	 * 文件上传
	 * @param getUrl 上传地址
	 * @param paramMap 参数
	 * @param
	 * @return 上传结果
	 */
	public static Object uploadFile(String getUrl,Map<String,String> paramMap,FileBody fileBody){
		// TODO
		return null;
	}
	
	/**
	 * 文件下载
	 * @param getUrl 下载url
	 * @param paramMap 参数map
	 * @return 返回下载的文件流
	 */
	public static OutputStream downLoadFile(String getUrl,Map<String,String> paramMap){
		// TODO
		return null;
	}

	
}
