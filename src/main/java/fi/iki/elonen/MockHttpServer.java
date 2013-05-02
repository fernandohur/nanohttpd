package main.java.fi.iki.elonen;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import main.java.fi.iki.elonen.NanoHTTPD.Response.Status;

public class MockHttpServer extends NanoHTTPD {

	private Queue<Request> requestQueue;
	private Queue<Response> responseQueue;
	private int port;
	
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
	
	public void enqueueResponse(Status status, String mimeType, String txt){
		Response resp = new Response(status,mimeType,txt);
		responseQueue.add(resp);
	}
	
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

	public String getUrl() {
		return "http://0.0.0.0:"+port;
	}

}
