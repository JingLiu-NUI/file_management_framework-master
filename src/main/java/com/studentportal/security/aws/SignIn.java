package com.studentportal.security.aws;

import com.amazonaws.services.cognitoidp.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SignIn {
    private static AwsCognito awsCognito = AwsCognito.getInstance();

    public static AwsCognitoResult doWork(String email, String password) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            return new AwsCognitoResult(false,
                    ResultReasons.FIELDS_INCOMPLETE);
        } else {
            try {
                Map<String, String> authParams = new HashMap<>();
                authParams.put("USERNAME", email);
                authParams.put("PASSWORD", password);

                AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                        .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                        .withAuthParameters(authParams)
                        .withClientId(awsCognito.getCognitoClientId())
                        .withUserPoolId(awsCognito.getCognitoPoolId());

                AdminInitiateAuthResult authResponse = awsCognito.getClient()
                        .adminInitiateAuth(authRequest);

                if (StringUtils.isBlank(authResponse.getChallengeName())) {
                    awsCognito.updateCredentialsCookies(authResponse.getAuthenticationResult());
                    return new AwsCognitoResult(true, ResultReasons.LOGGED_IN);
                } else if (ChallengeNameType.NEW_PASSWORD_REQUIRED.name().equals(
                        authResponse.getChallengeName())) {
                    System.out.println("temporary password used");
                    return new AwsCognitoResult(false,
                            ResultReasons.TEMP_PASSWORD_USED);
                } else {
                    throw new RuntimeException("unexpected challenge on signin: "
                            + authResponse.getChallengeName());
                }
            } catch (UserNotFoundException e) {
                System.out.println("No such user");
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.NO_SUCH_USER);
            } catch (NotAuthorizedException e) {
                System.out.println("Not authorized");
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.NOT_AUTHORIZED);
            } catch (TooManyRequestsException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.TOO_MANY_REQUESTS);
            }
        }
    }
}
