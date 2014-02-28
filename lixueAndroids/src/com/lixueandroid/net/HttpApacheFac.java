package com.lixueandroid.net;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Future;

import me.xiaopan.easy.network.android.http.Post;
import me.xiaopan.easy.network.android.http.Request;
import me.xiaopan.easy.network.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import android.content.Context;

import com.lixueandroid.util.HttpUtils;
import com.lixueandroid.util.StringUtils;

public class HttpApacheFac {

	public static int DEFAULT_MAX_CONNECTIONS = 10;	//最大连接数
	public static int DEFAULT_SOCKET_TIMEOUT = 20 * 1000;	//连接超时时间
	public static int DEFAULT_MAX_RETRIES = 5;	//最大重试次数
	public static int DEFAULT_SOCKET_BUFFER_SIZE = 8192;	//Socket缓存大小
	private static String CharSet = HTTP.UTF_8;
	
    private DefaultHttpClient client = null;
    private static Map<String, String> clientHeaderMap;	//请求头Map
    private HttpContext httpContext;	//Http上下文
    private Map<Context, List<WeakReference<Future<?>>>>requestMap;
    
    public boolean debugMode;//是否输出Log日志
    private String logTag="HttpApacheFac";
    
    public HttpApacheFac(){
    	httpContext=new SyncBasicHttpContext(new BasicHttpContext());
    	clientHeaderMap = new HashMap<String, String>();
    	requestMap= new WeakHashMap<Context, List<WeakReference<Future<?>>>>();
    	 
    	 /* 初始化HttpClient */
     	if(client==null){
     		HttpParams params = new BasicHttpParams();
     		ConnManagerParams.setTimeout(params,10000);//设置超时时间
     		ConnManagerParams.setMaxConnectionsPerRoute(params,new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));//设置每个route上的最大连接数
     		ConnManagerParams.setMaxTotalConnections(params, DEFAULT_MAX_CONNECTIONS);//设置最大连接数
     		
     		HttpConnectionParams.setSoTimeout(params,DEFAULT_SOCKET_TIMEOUT);//设置socket超时时间
     		HttpConnectionParams.setConnectionTimeout(params,DEFAULT_SOCKET_TIMEOUT);//设置连接超时时间
     		HttpConnectionParams.setTcpNoDelay(params, true);
     		HttpConnectionParams.setSocketBufferSize(params, DEFAULT_SOCKET_BUFFER_SIZE);//设置socket的缓存大小
     		HttpProtocolParams.setVersion(params,HttpVersion.HTTP_1_1);//版本
     		HttpProtocolParams.setContentCharset(params,CharSet);//编码
     		HttpProtocolParams.setUseExpectContinue(params,true);//如果发现异常也继续进行
     		SchemeRegistry shm = new SchemeRegistry();
     		shm.register(new Scheme("http",PlainSocketFactory.getSocketFactory(),80));
            shm.register(new Scheme("https",SSLSocketFactory.getSocketFactory(),443));
             // 为请求头使用Interceptor
            client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,shm),params);
            client.addRequestInterceptor(new GzipProcessRequestInterceptor());
            client.addRequestInterceptor(new AddRequestHeaderRequestInterceptor(clientHeaderMap));
            client.addResponseInterceptor(new GzipProcessResponseInterceptor());
            client.setHttpRequestRetryHandler(new RetryHandler(DEFAULT_MAX_RETRIES));
     	}
    }
    /**
	 * 实例持有器
	 */
	private static class EasyHttpClientInstanceHolder{
		private static HttpApacheFac instance = new HttpApacheFac();
	}
	
    /**
     * 得到实例
     */
    public static final HttpApacheFac getHttpInstance(){
    	return EasyHttpClientInstanceHolder.instance;
    }
    
    /**
     * 设置Cookie仓库，将在发送请求时使用此Cookie仓库
     */
    public void setCookieStore(CookieStore cookieStore){
    	httpContext.setAttribute(ClientContext.COOKIE_STORE,cookieStore);
    }
    
    /**
     * 设置代理，在之后的每一次请求都将使用此代理
     * @param userAgent 用户代理的信息将会添加在“User-Agent”请求头中
     */
    public void setAgent(String userAgent){
    	HttpProtocolParams.setUserAgent(this.client.getParams(), userAgent);
    }
    
    /**
     *  设置请求超时时间，默认是20秒
     * @param timeout 请求超时时间，单位毫秒
     */
    public void setRequestTimeOut(int timeout){
    	HttpParams params=this.client.getParams();
    	ConnManagerParams.setTimeout(params, timeout);
    	HttpConnectionParams.setConnectionTimeout(params, timeout);
    	HttpConnectionParams.setSoTimeout(params, timeout);
    }
    /**
     * Sets the SSLSocketFactory to user when making requests. By default,
     * a new, default SSLSocketFactory is used.
     * @param sslSocketFactory the socket factory to use for https requests.
     */
    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sslSocketFactory, 443));
    }
    
    /**
     * 添加一个请求头参数，这些参数都会在发送请求之前添加到请求体中
     * @param header 参数名
     * @param value 参数值
     */
    public void setHeader(String header,String value){
    	clientHeaderMap.put(header, value);
    }
    
    /**
     * 设置Http Auth认证
     * @param username 用户名
     * @param password 密码
     */
    public void setBasicAuth(String username,String password){
    	setBasicAuth(username,password,AuthScope.ANY);
    }
    /**
     * 设置Http Auth认证
     * @param username 用户名
     * @param password 密码
     * @param scope 
     */
    public void setBasicAuth( String user, String pass, AuthScope scope){
        this.client.getCredentialsProvider().setCredentials(scope, new UsernamePasswordCredentials(user,pass));
    }
    
   /****************************************************************************************************HTTP Get请求**************************************************************************************************************** */
    
    /**
     * 执行一个HTTP GET请求
     * @param context Android上下文，稍后你可以通过此上下文来取消此次请求
     * @param url 请求地址
     * @param headers 请求头信息
     * @param params 请求参数
     * @param httpResponseHandler Http响应处理器
     */
    public void get(Context context, String url, Header[] headers, RequestParams params, HttpResponseHandler httpResponseHandler) {
        sendRequest(context, HttpUtils.setHeaders(new HttpGet(HttpUtils.getUrlWithQueryString(url, params)), headers), httpResponseHandler);
    }
    
    /**
     * 执行一个HTTP GET请求
     * @param context
     * @param url
     * @param headers
     * @param request
     * @param httpResponseHandler
     */
    private void get(Context context, String url, Header[] headers,Request request, HttpResponseHandler httpResponseHandler) {
		sendRequest(context, url, headers, request, null, httpResponseHandler);
	}
    /****************************************************************************************************Post 请求**************************************************************************************************************** */
    private void post(Context context, String url, Header[] headers,Request request, String contentType,HttpResponseHandler httpResponseHandler) {
    	 try {
 	        sendRequest(context, HttpUtils.setEntity(new HttpPost(HttpUtils.getUrlFromRequestObject(url, request)), HttpUtils.paramsToEntity(this, HttpUtils.requestToRequestParams(request)), headers), contentType, httpResponseHandler);
 	    } catch (Exception e) {
 	    	if(httpResponseHandler != null){
 				httpResponseHandler.exception(e);
 			}
 	    }
	}
    
    /****************************************************************************************************HTTP 发送请求**************************************************************************************************************** */
    /**
     * 发送请求
     * @param context 上下文
     * @param httpUriRequest uri请求
     * @param contentType 上下文类型
     * @param httpResponseHandler 响应处理
     */
    public void sendRequest(Context context,HttpUriRequest httpUriRequest,String contentType,HttpResponseHandler httpResponseHandler){
    	if(StringUtils.isNotEmpty(contentType)){
    		httpUriRequest.addHeader("Content-Type", contentType);
    	}
    	Future<?> futureRequest=EasyNetwork.getThreadPool().submit(new HttpRequestRunnable(this, this.client, httpContext, httpResponseHandler, httpUriRequest));
    	if(context!=null){
    		List<WeakReference<Future<?>>> requestList= requestMap.get(context);
    		if(requestList==null){
    			requestList=new LinkedList<WeakReference<Future<?>>>();
    			requestMap.put(context, requestList);
    		}
    		WeakReference<Future<?>> futures=new WeakReference<Future<?>>(futureRequest);
    		requestList.add(futures);
    	}
    }
    /**
     * 发送请求
     * @param context 上下文
     * @param httpUriRequest uri请求
     * @param httpResponseHandler 响应处理
     */
    public void sendRequest(Context context,HttpUriRequest httpUriRequest,HttpResponseHandler httpResponseHandler){
    	sendRequest(context, httpUriRequest, null,httpResponseHandler);
    }
    
    /**
     * 发送请求
     * @param httpUriRequest 上下文
     * @param contentType 头类型
     * @param httpResponseHandler 响应处理
     */
    public void sendRequest(HttpUriRequest httpUriRequest,String contentType,HttpResponseHandler httpResponseHandler){
    	sendRequest(null, httpUriRequest, contentType,httpResponseHandler);
    }
    
    /**
     * 发送请求
     * @param httpUriRequest uri请求
     * @param httpResponseHandler 响应处理
     */
    public void sendRequest(HttpUriRequest httpUriRequest,HttpResponseHandler httpResponseHandler){
    	sendRequest(null, httpUriRequest, null, httpResponseHandler);
    }
    /**
     * 执行一个HTTP 请求
     * @param context Android上下文，稍后你可以通过此上下文来取消此次请求
     * @param url 请求地址
     * @param headers 请求头信息
     * @param request 请求对象，EasyHttpClient会采用反射的方式将请求对象里所有加了Expose注解的字段封装成一个RequestParams，如果请求对象有Post注解就会以Post的方式来发送请求，否则一律采用Get的方式来发送请求
     * @param contentType 内容类型
     * @param httpResponseHandler Http响应处理器
     */
    public void sendRequest(Context context, String url, Header[] headers, Request request, String contentType, HttpResponseHandler httpResponseHandler){
    	if(request.getClass().getAnnotation(Post.class) != null){
    		post(context, url, headers, request, contentType, httpResponseHandler);
    	}else{
    		get(context, url, headers, request, httpResponseHandler);
    	}
    }
    
//    public static HttpClient getHttps(String sslKeyPath,String host,String username,String password){
//    	HttpClient client = null;
//		try{
//			client = new DefaultHttpClient();
//    		SSLSocketFactory socket = null;
//    		Scheme shm = null;
//    		if(sslKeyPath!=null&&!sslKeyPath.equals("")){
//    			File keyFile = new File(sslKeyPath);
//    			if(keyFile.exists()&&keyFile.isFile()){
//    				KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
//    				FileInputStream fis = new FileInputStream(keyFile);
//    				key.load(fis,"password".toCharArray());
//    	    		fis.close();
//    	    		socket = new SSLSocketFactory(key);
//    	    		shm = new Scheme("https",socket,443);
//    			}
//    		}
//    		if(socket==null){
//    			shm = new Scheme("https",SSLSocketFactory.getSocketFactory(),443);
//    		}
//    		client.getConnectionManager().getSchemeRegistry().register(shm);
//    		if(host!=null&&username!=null&&password!=null){
//    			((AbstractHttpClient)client).getCredentialsProvider().setCredentials(new AuthScope(host,443),new UsernamePasswordCredentials(username,password));
//    		}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//    	return client;
//    }
    /**
     * 得到debug模式是否开启
     */
    public boolean getDebugMode(){
    	return this.debugMode;
    }
    
    /**
     * 是否设置debug模式
     * @param ifDebugMode
     */
    public void setDebugMode(boolean ifDebugMode){
    	this.debugMode=ifDebugMode;
    }
    
    /**
     * 打印日志
     */
    public void Log(String logContent){
    	if(debugMode){
    		android.util.Log.d(logTag, logContent);
    	}
    }
}