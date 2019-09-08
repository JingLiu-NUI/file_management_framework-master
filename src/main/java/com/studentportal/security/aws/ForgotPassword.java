package com.studentportal.security.aws;

import com.amazonaws.services.cognitoidp.model.*;
import org.apache.commons.lang3.StringUtils;

public class ForgotPassword {

    private static AwsCognito awsCognito = AwsCognito.getInstance();
    ;

    public static AwsCognitoResult doWork(String email) {
        if (StringUtils.isBlank(email)) {
            return new AwsCognitoResult(false,
                    ResultReasons.FIELDS_INCOMPLETE);
        } else {
            try {
                ForgotPasswordRequest forgotRequest = new ForgotPasswordRequest()
                        .withClientId(awsCognito.getCognitoClientId())
                        .withUsername(email);

                awsCognito.getClient().forgotPassword(forgotRequest);
                return new AwsCognitoResult(true,
                        ResultReasons.VERIFICATION_CODE_SENT);
            } catch (CodeDeliveryFailureException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.CODE_DELIVERY_FAILED);
            } catch (UserNotConfirmedException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.USER_NOT_CONFIRMED);
            } catch (NotAuthorizedException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.NOT_AUTHORIZED);
            } catch (InvalidParameterException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.INVALID_PARAMS);
            } catch (UserNotFoundException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.NO_SUCH_USER);
            } catch (TooManyRequestsException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.TOO_MANY_REQUESTS);
            }
        }
    }
}
