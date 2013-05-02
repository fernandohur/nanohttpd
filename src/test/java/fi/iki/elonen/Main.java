package test.java.fi.iki.elonen;

import java.io.IOException;
import java.util.Map;

import main.java.fi.iki.elonen.MockHttpServer;
import main.java.fi.iki.elonen.NanoHTTPD;
import main.java.fi.iki.elonen.NanoHTTPD.Response.Status;

public class Main extends MockHttpServer{

	public Main() throws IOException {
		super(1234);
		
		
		
		start();
		enqueueResponse(Status.OK, "application/json", "{\"a\":2}");
		System.in.read();
		stop();
	}
	
	@Override
	public Response serve(String uri, Method method,
			Map<String, String> header, Map<String, String> parms,
			Map<String, String> files) {
		System.out.println("URI:"+uri);
		System.out.println("Params:"+parms+ ", size:"+parms.get(NanoHTTPD.QUERY_STRING_PARAMETER));
		System.out.println("Method:"+method.toString());
		return super.serve(uri, method, header, parms, files);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		new Main();
		
	}

}
