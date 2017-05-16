package humaxdigital.joycast.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpRequestHelper 
{
    public HttpRequestHelper() 
    {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5 * 1000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        localContext = new BasicHttpContext();
    }

    public void abort() 
    {
        try 
        {
            if (httpClient != null) 
            {
                CastLogger.i(TAG, "Abort.");
//                httpPost.abort();
//                httpDelete.abort();
//                httpGet.abort();
            }
        } 
        catch (Throwable e) 
        {
            CastLogger.e(TAG, "Failed to abort");
        }
    }

    public boolean sendDelete(String url, Map<String, String> params) 
    {
        CastLogger.e("send DELETE to target application : " + url);
        return sendDelete(url, params, null);        
    }
    
    public boolean sendPost(String url, Map<String, String> params) 
    {
        CastLogger.e("send POST to target application : " + url);
        return sendPost(url, params, null);
    }

    public boolean sendPost(String url, Map<String, String> params, String contentType) 
    {
        try 
        {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", 
                    "text/html,application/xml,application/xhtml+xml," +
                    "text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            if (contentType != null) 
            {
                httpPost.setHeader("Content-Type", contentType);
            } 
            else 
            {
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            }
            httpPost.setEntity(buildData(params));
            HttpResponse response = httpClient.execute(httpPost, localContext);
            
            if (response != null) 
            {
                CastLogger.i("DialServer return : " + response.getStatusLine().getStatusCode());
                /**
                 * @note HTTP_UNAVAILABLE is workaround since dial server is sometimes returning 50x \n
                 * event though the server could run the target application on there 
                 */
                if ( response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK || 
                        response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_CREATED || 
                        response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_UNAVAILABLE ) 
                    return true; 
            }
        } 
        catch (Throwable e) 
        {
            e.printStackTrace();
            CastLogger.e(TAG, "Failed to post to url: " + url);
        }
        return false;
    }

    public boolean sendPostStream(String url, Map<String, String> params, String contentType, OutputStream outstream) 
    {
        try 
        {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", 
                    "text/html,application/xml,application/xhtml+xml," +
                    "text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");

            if (contentType != null) 
            {
                httpPost.setHeader("Content-Type", contentType);
            } 
            else 
            {
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            }

            httpPost.setEntity(buildData(params));
            HttpResponse response = httpClient.execute(httpPost, localContext);
            if (response != null) 
            {
                response.getEntity().writeTo(outstream);
                return true;
            }
        } 
        catch (Throwable e) 
        {
            CastLogger.e(TAG, "Failed to post to url: " + url);
        }
        return false;
    }

    public boolean sendDelete(String url, Map<String, String> params, String contentType) 
    {
        try 
        {
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader("Accept", 
                    "text/html,application/xml,application/xhtml+xml," +
                    "text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            if (contentType != null) 
            {
                httpDelete.setHeader("Content-Type", contentType);
            } 
            else 
            {
                httpDelete.setHeader("Content-Type", "application/x-www-form-urlencoded");
            }
            HttpResponse response = httpClient.execute(httpDelete, localContext);
            
            if (response != null) 
            {
                /**
                 * @note HTTP_UNAVAILABLE is workaround since dial server is sometimes returning 50x \n
                 * event though the server could run the target application on there 
                 */
                if ( response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK || 
                        response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_UNAVAILABLE ) 
                    return true; 
            }
        }
        catch (Throwable e) 
        {
            CastLogger.e(TAG, "Failed to post to url: " + url);
        }
        return false;
    }
    
    public String sendGet(String url) 
    {
        HttpGet httpGet = new HttpGet(url);

        try 
        {
            HttpResponse response = httpClient.execute(httpGet);
            if (null != response)
                return EntityUtils.toString(response.getEntity());
        } 
        catch (Throwable e) 
        {
            e.printStackTrace();
        }

        return "";
    }

    public boolean sendGetStream(String url, OutputStream outstream) 
    {
        HttpGet httpGet = new HttpGet(url);

        try 
        {
            HttpResponse response = httpClient.execute(httpGet);
            if (null != response) 
            {
                response.getEntity().writeTo(outstream);
                return true;
            }
        } 
        catch (Throwable e) 
        {
            e.printStackTrace();
        }

        return false;
    }

    public HttpResponse sendHttpGet(String url) 
    {
        HttpGet httpGet = new HttpGet(url);

        try 
        {
            return httpClient.execute(httpGet);
        } 
        catch (Throwable e) 
        {
            e.printStackTrace();
        }
        return null;

    }

    public HttpResponse sendHttpGet(String url, String... query) 
    {
        String queryParams = buildgetData(query);
        return sendHttpGet(url + "?" + queryParams);
    }

    public String sendGet(String scheme, String host, int port, String path, String... query) throws URISyntaxException 
    {
        String queryParams = buildgetData(query);
        URI uri = URIUtils.createURI(scheme, host, port, path, queryParams, null);
        HttpGet httpGet = new HttpGet(uri);

        try 
        {
            HttpResponse response = httpClient.execute(httpGet);
            if ( response != null )
                return EntityUtils.toString(response.getEntity());
        } 
        catch (Throwable e) 
        {
            e.printStackTrace();
        }

        return "";
    }

    public String sendGet(String url, String... query) 
    {
        String queryParams = buildgetData(query);
        return sendGet(url + "?" + queryParams);
    }

    public boolean sendGetStream(String url, OutputStream outstream, String... query) 
    {
        String queryParams = buildgetData(query);
        return sendGetStream(url + "?" + queryParams, outstream);
    }

    public InputStream getHttpStream(String urlString) throws IOException 
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try 
        {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) 
            {
                in = httpConn.getInputStream();
            }
        } 
        catch (Exception e) 
        {
            throw new IOException("Error connecting");
        } // end try-catch

        return in;
    }

    public static String buildgetData(String... list) 
    {
        if (null == list || list.length == 0)
            return null;
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        for (int i = 0; i < list.length - 1; i = i + 2) 
        {
            qparams.add(new BasicNameValuePair(list[i], list[i + 1]));
        }
        return URLEncodedUtils.format(qparams, "UTF-8");

    }

    public HttpEntity buildData(Map<String, String> map) throws UnsupportedEncodingException 
    {
        if (null != map && !map.isEmpty()) 
        {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (String name : map.keySet()) 
            {
                parameters.add(new BasicNameValuePair(name, map.get(name)));
            }
            return new UrlEncodedFormEntity(parameters, "UTF-8");
        }
        return null;
    }
    
    HttpClient httpClient;
    HttpContext localContext;
    private String TAG = "HttpRequestHelper";
}
