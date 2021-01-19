/**
 * 
 */
package org.sunbird.health.actor;

import org.sunbird.BaseActor;
import org.sunbird.request.Request;
import org.sunbird.response.Response;
/**
 * @author manzarul
 *
 */
public class HealthActor extends BaseActor{

	@Override
	public void onReceive(Request request) throws Throwable {
		Response response = new Response();
		response.getResult().put("response", "Success");
		sender().tell(response, getSelf());
	}

}
