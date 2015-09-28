/*
 * See COPYING for license information.
 */ 

package com.stacksync.android.api;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;

public class FilesResponse
{
    private HttpResponse response = null;
    private HttpEntity entity = null;

    /**
     * @param method The HttpMethod that generated this response
     */
    public FilesResponse (HttpResponse response)
    {
    	this.response = response;
    	entity = response.getEntity();
    	
    }

    /**
     * Checks to see if the user managed to login with their credentials.
     *
     * @return true is login succeeded false otherwise
     */
    public boolean loginSuccess ()
    {
	int statusCode = getStatusCode();

    	if (statusCode >= 200 && statusCode < 300)
    		return true;

    	return false;     
    }

    /**
     * This method makes no assumptions about the user having been logged in.  It simply looks for the Storage Token header
     * as defined by FilesConstants.X_STORAGE_TOKEN and if this exists it returns its value otherwise the value returned will be null.
     *
     * @return null if the user is not logged into Cloud FS or the Storage token
     */
    public String getAuthToken ()
    {
        return getResponseHeader(FilesUtil.getProperty("auth_token_name", FilesConstants.X_AUTH_TOKEN)).getValue();
    }

    /**
     * This method makes no assumptions about the user having been logged in.  It simply looks for the Storage URL header
     * as defined by FilesConstants.X_STORAGE_URL and if this exists it returns its value otherwise the value returned will be null.
     *
     * @return null if the user is not logged into Cloud FS or the Storage URL
     */
    public String getStorageURL ()
    {
       return getResponseHeader(FilesConstants.X_STORAGE_URL).getValue();
    }

    /**
     * This method makes no assumptions about the user having been logged in.  It simply looks for the CDN Management URL header
     * as defined by FilesConstants.X_CDN_MANAGEMENT_URL and if this exists it returns its value otherwise the value returned will be null.
     *
     * @return null if the user is not logged into Cloud FS or the Storage URL
     */
    public String getCDNManagementURL ()
    {
    	Header header = getResponseHeader(FilesConstants.X_CDN_MANAGEMENT_URL);
    	return header == null ? null : header.getValue();
    }

    /**
     * Get the content type
     * 
     * @return The content type (e.g., MIME type) of the response
     */
    public String getContentType ()
    {
       return getResponseHeader("Content-Type").getValue();
    }

    /**
     * Get the content length of the response (as reported in the header)
     * 
     * @return the length of the content
     */
    public String getContentLength ()
    {
    	Header hdr = getResponseHeader("Content-Length");
    	if (hdr == null) return "0";
    	return hdr.getValue();
    }

    /**
     * The Etag is the same as the objects MD5SUM
     * 
     * @return The ETAG
     */
    public String getETag ()
    {
    	Header hdr = getResponseHeader(FilesConstants.E_TAG);
    	if (hdr == null) return null;
    	return hdr.getValue(); 
    }

    /**
     * The last modified header
     * 
     * @return The last modified header
     */
    public String getLastModified ()
    {
       return getResponseHeader("Last-Modified").getValue(); 
    }

    /**
     * The HTTP headers from the response
     * 
     * @return The headers
     */
    public Header[] getResponseHeaders()
    {
        return response.getAllHeaders();
    }

    /**
     * The HTTP Status line (both the status code and the status message).
     * 
     * @return The status line
     */
    public StatusLine getStatusLine()
    {
        return response.getStatusLine();
    }

    /**
     * Get the HTTP status code
     * 
     * @return The status code
     */
    public int getStatusCode ()
    {
        return response.getStatusLine().getStatusCode();
    }

    /**
     * Get the HTTP status message
     * 
     * @return The message portion of the status line
     */
    public String getStatusMessage ()
    {
        return response.getStatusLine().getReasonPhrase();
    }

    /**
     * The HTTP Method (put, get, etc) of the request that generated this response
     * 
     * @return The method name
     */
    /*public String getMethodName ()
    {
        return response.getName();
    }
    */

    /**
     * Returns the response body as text
     * 
     * @return The response body
     * @throws IOException
     */
    public String getResponseBodyAsString () throws IOException
    {
        return EntityUtils.toString(entity);
    }

    /**
     * Get the response body as a Stream
     * 
     * @return An input stream that will return the response body when read
     * @throws IOException
     */
    public InputStream getResponseBodyAsStream () throws IOException
    {
        return entity.getContent();
    }

    /**
     * Get the body of the response as a byte array
     *
     * @return The body of the response.
     * @throws IOException
     */
    public byte[] getResponseBody () throws IOException
    {
        return EntityUtils.toByteArray(entity);
    }

    /**
     * Returns the specified response header. Note that header-name matching is case insensitive. 
     *
     * @param headerName  - The name of the header to be returned. 
     * @return  The specified response header. If the response contained multiple instances of the header, its values will be combined using the ',' separator as specified by RFC2616.
     */
    public Header getResponseHeader(String headerName)
    {
        return response.getFirstHeader(headerName);
    }

    /**
     * Get the number of objects in the header
     * 
     * @return -1 if the header is not present or the correct value as defined by the header
     */
    public int getContainerObjectCount ()
    {
        Header contCountHeader = getResponseHeader (FilesConstants.X_CONTAINER_OBJECT_COUNT);
        if (contCountHeader != null )
          return Integer.parseInt(contCountHeader.getValue());
        return -1;
    }

    /**
     * Get the number of bytes used by the container
     * 
     * @return -1 if the header is not present or the correct value as defined by the header
     */
    public long getContainerBytesUsed ()
    {
        Header contBytesUsedHeader = getResponseHeader (FilesConstants.X_CONTAINER_BYTES_USED);
        if (contBytesUsedHeader != null )
          return Long.parseLong(contBytesUsedHeader.getValue());
        return -1;
    }

    /**
     * Get the number of objects in the header
     * 
     * @return -1 if the header is not present or the correct value as defined by the header
     */
    public int getAccountContainerCount ()
    {
        Header contCountHeader = getResponseHeader (FilesConstants.X_ACCOUNT_CONTAINER_COUNT);
        if (contCountHeader != null )
          return Integer.parseInt(contCountHeader.getValue());
        return -1;
    }

    /**
     * Get the number of bytes used by the container
     * 
     * @return -1 if the header is not present or the correct value as defined by the header
     */
    public long getAccountBytesUsed ()
    {
        Header accountBytesUsedHeader = getResponseHeader (FilesConstants.X_ACCOUNT_BYTES_USED);
        if (accountBytesUsedHeader != null )
          return Long.parseLong(accountBytesUsedHeader.getValue());
        return -1;
    }

    /**
     * Get the URL For a shared container
     * 
     * @return null if the header is not present or the correct value as defined by the header
     */
    public String getCdnUrl ()
    {
        Header cdnHeader = getResponseHeader (FilesConstants.X_CDN_URI);
        if (cdnHeader != null )
          return cdnHeader.getValue();
        return null;
    }

    /**
     * Get the SSL URL For a shared container
     * 
     * @return null if the header is not present or the correct value as defined by the header
     */
    public String getCdnSslUrl ()
    {
        Header cdnHeader = getResponseHeader (FilesConstants.X_CDN_SSL_URI);
        if (cdnHeader != null )
          return cdnHeader.getValue();
        return null;
    }
    
    /**
     * Get the SSL URL For a shared container
     * 
     * @return null if the header is not present or the correct value as defined by the header
     */
    public String getCdnStreamingUrl ()
    {
        Header cdnHeader = getResponseHeader (FilesConstants.X_CDN_Streaming_URI);
        if (cdnHeader != null )
          return cdnHeader.getValue();
        return null;
    }

    /**
     * Returns the response headers with the given name. Note that header-name matching is case insensitive.
     *
     * @param headerName - the name of the headers to be returned.
     * @return An array of zero or more headers
     */
    public Header[] getResponseHeaders(String headerName)
    {
        return response.getHeaders(headerName);
    }
    
    public String getContentEncoding() {
    	return entity.getContentEncoding().getValue();
    }
}
