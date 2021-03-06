package controllers.certs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import akka.actor.ActorRef;
import org.sunbird.incredible.processor.JsonKey;
import org.sunbird.cert.actor.operation.CertActorOperation;

import controllers.BaseController;
import org.sunbird.request.Request;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This controller is responsible for certificate generation.
 * @author manzarul
 *
 */
public class CertsGenerationController  extends BaseController{


	@Inject
	@Named("certificate-generator_actor")
	private ActorRef certGenerateActorRef;

	@Inject
	@Named("certificate_verifier_actor")
	private ActorRef certVerifierActorRef;

	@Inject
	@Named("template_validate_actor")
	private ActorRef templateValidateActorRef;

	/**
	   * This method will accept request for certificate generation.
	   * it will do request validation and processing of request.
	   * @return a CompletableFuture of success response
	   */
	  public CompletionStage<Result> generateCerificate(Http.Request httpRequest) {
		CompletionStage<Result> response = handleRequest(certGenerateActorRef, httpRequest,
				request -> {
					Request req = (Request) request;
					Map<String, Object> context = new HashMap<>();
					context.put(JsonKey.VERSION, JsonKey.VERSION_1);
					req.setContext(context);
					new CertValidator().validateGenerateCertRequest(req);
					return null;
					},
				CertActorOperation.GENERATE_CERTIFICATE.getOperation());
	    return response;
	  }


	public CompletionStage<Result> generateCertificateV2(Http.Request httpRequest) {
		CompletionStage<Result> response = handleRequest(certGenerateActorRef, httpRequest,
				request -> {
					Request req = (Request) request;
					Map<String, Object> context = new HashMap<>();
					context.put(JsonKey.VERSION, JsonKey.VERSION_2);
					req.setContext(context);
					new CertValidator().validateGenerateCertRequest(req);
					return null;
				},
				CertActorOperation.GENERATE_CERTIFICATE_V2.getOperation());
		return response;
	}

	  public CompletionStage<Result> generateSignUrl(Http.Request httpRequest) {
			CompletionStage<Result> response = handleRequest(certGenerateActorRef, httpRequest,
					null,
					CertActorOperation.GET_SIGN_URL.getOperation());
		    return response;
		  }

    public CompletionStage<Result> verifyCerificate(Http.Request httpRequest) {
        CompletionStage<Result> response = handleRequest(certVerifierActorRef, httpRequest,
                request -> {
                    Request req = (Request) request;
                    new VerificationReqValidator().validateVerificationRequest(req);
                    return null;
                },
                CertActorOperation.VERIFY_CERT.getOperation());
        return response;
    }

    public CompletionStage<Result> validateTemplate(Http.Request httpRequest) {
		CompletionStage<Result> response =  handleRequest(templateValidateActorRef, httpRequest,
				request -> {
					Request req = (Request) request;
					new TemplateReqValidator().validateTemplateRequest(req);
					return null;
				},
                CertActorOperation.VALIDATE_TEMPLATE.getOperation());
		return response;
    }
}