package com.studentportal.security.aws;

import com.amazonaws.services.cognitoidp.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SignUpConfirm {
    private static AwsCognito awsCognito = AwsCognito.getInstance();

    public static AwsCognitoResult doWork(String email, String tempPsswd, String finalPsswd) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(tempPsswd) ||
                StringUtils.isBlank(finalPsswd)) {
            return new AwsCognitoResult(false, ResultReasons.FIELDS_INCOMPLETE);
        } else {
            try {
                Map<String, String> initialParams = new HashMap<>();
                initialParams.put("USERNAME", email);
                initialParams.put("PASSWORD", tempPsswd);

                AdminInitiateAuthRequest initialRequest = new AdminInitiateAuthRequest()
                        .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                        .withAuthParameters(initialParams)
                        .withClientId(awsCognito.getCognitoClientId())
                        .withUserPoolId(awsCognito.getCognitoPoolId());

                AdminInitiateAuthResult initialResponse = awsCognito.getClient()
                        .adminInitiateAuth(initialRequest);
                if (!ChallengeNameType.NEW_PASSWORD_REQUIRED.name().equals(initialResponse.getChallengeName())) {
                    throw new RuntimeException("unexpected challenge: " + initialResponse.getChallengeName());
                }

                Map<String, String> challengeResponses = new HashMap<>();
                challengeResponses.put("USERNAME", email);
                challengeResponses.put("PASSWORD", tempPsswd);
                challengeResponses.put("NEW_PASSWORD", finalPsswd);

                AdminRespondToAuthChallengeRequest finalRequest = new AdminRespondToAuthChallengeRequest()
                        .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                        .withChallengeResponses(challengeResponses)
                        .withClientId(awsCognito.getCognitoClientId())
                        .withUserPoolId(awsCognito.getCognitoPoolId())
                        .withSession(initialResponse.getSession());

                AdminRespondToAuthChallengeResult challengeResponse = awsCognito.getClient()
                        .adminRespondToAuthChallenge(finalRequest);

                if (StringUtils.isBlank(challengeResponse.getChallengeName())) {
                    awsCognito.updateCredentialsCookies(challengeResponse.getAuthenticationResult());
                    return new AwsCognitoResult(true, ResultReasons.REGISTER_CONFIRMED);
                } else {
                    throw new RuntimeException("unexpected challenge: " +
                            challengeResponse.getChallengeName());
                }
            } catch (InvalidPasswordException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.INVALID_PASSWORD);
            } catch (UserNotFoundException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.NO_SUCH_USER);
            } catch (NotAuthorizedException e) {
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
