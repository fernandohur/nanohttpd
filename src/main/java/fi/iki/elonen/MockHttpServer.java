package main.java.fi.iki.elonen;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import main.java.fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * The MockHttpServer can be used as a mock http server to test webservices
 * The simplest way to use MockHttpServer is as follows:
 * <code>
 * //first start the server on your favourite port
 * MockHttpServer server = new MockHttpServer(portNum);
 * server.start();
 * 
 * //We will add a mock response for every request we will do, so in this case, just one mock response
 * server.enqueueResponse(Status.OK, "application/json,"{\"user_id\":15,\"name\":\"paul\"}");
 * 
 * //now we will use curl
 * curl http://0.0.0.0:3000/users.json?name=paul
 * 
 * // we get the request object
 * Request req = server.getRequest();
 * assertEquals(req.getMethod(),"GET");
 * assertEquals(req.getParams().get("name"),"paul")
 * 
 * </code>
 */
public class MockHttpServer extends NanoHTTPD {

	/**
	 * Holds a queue for all the requests
	 */ 
	private Queue<Request> requestQueue;
	/**
	 * Holds a queue for all the responses that will be sent when a request arrives
	 */ 
	private Queue<Response> responseQueue;
	/**
	 * The port in which the server will run
	 */
	private int port;
	
	/**
	 *  Initializes a new server on the specified port. Note that this method does not start
	 * the server, you must use <code>start()</code> for this
	 */ 
	public MockHttpServer(int port) {
		super(port);

		requestQueue = new LinkedList<Request>();
		responseQueue = new LinkedList<Response>();
		this.port = port;
	}
	
	@Override
	public void start() throws IOException {
		super.start();
		requestQueue.clear();
		responseQueue.clear();
	}
	
	@Override
	public void stop() {
		super.stop();
		requestQueue.clear();
		responseQueue.clear();
	}
	
	/**
	 *  Adds a response to the response queue
	 */ 
	public void enqueueResponse(Status status, String mimeType, String txt){
		Response resp = new Response(status,mimeType,txt);
		responseQueue.add(resp);
	}
	
	/**
	 *  returns the last request
	 */ 
	public Request getRequest(){
		return requestQueue.poll();
	}

	@Override
	public Response serve(String uri, Method method,
			Map<String, String> header, Map<String, String> parms,
			Map<String, String> files) {
		Request r = new Request();
		r.setFiles(files);
		r.setHeader(header);
		r.setMethod(method);
		r.setParams(parms);
		r.setUri(uri);
		requestQueue.add(r);
		return responseQueue.poll();
	}
	
	public class Request
	{
		String uri;
		Method method;
		Map<String, String> header;
		Map<String, String> params;
		Map<String, String> files;
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		public Method getMethod() {
			return method;
		}
		public void setMethod(Method method) {
			this.method = method;
		}
		public Map<String, String> getHeader() {
			return header;
		}
		public void setHeader(Map<String, String> header) {
			this.header = header;
		}
		public Map<String, String> getParams() {
			return params;
		}
		public void setParams(Map<String, String> params) {
			this.params = params;
		}
		public Map<String, String> getFiles() {
			return files;
		}
		public void setFiles(Map<String, String> files) {
			this.files = files;
		}
		
		
	}

	/**
	 *  this method should be overriden to fit your needs. http://0.0.0.0:port is the default url of the
	 * server
	 */
	public String getUrl() {
		return "http://0.0.0.0:"+port;
	}

}
