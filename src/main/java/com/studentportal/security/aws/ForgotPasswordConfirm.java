package com.studentportal.security.aws;

import com.amazonaws.services.cognitoidp.model.*;
import org.apache.commons.lang3.StringUtils;

public class ForgotPasswordConfirm {
    private static AwsCognito awsCognito = AwsCognito.getInstance();;

    public static AwsCognitoResult doWork(String email, String password, String verificationCode) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password) ||
                StringUtils.isBlank(verificationCode)) {
            return new AwsCognitoResult(false,
                    ResultReasons.FIELDS_INCOMPLETE);
        } else {
            try {
                ConfirmForgotPasswordRequest confirmRequest = new ConfirmForgotPasswordRequest()
                        .withClientId(awsCognito.getCognitoClientId())
                        .withConfirmationCode(verificationCode)
                        .withUsername(email)
                        .withPassword(password);

                awsCognito.getClient().confirmForgotPassword(confirmRequest);
                return new AwsCognitoResult(true,
                        ResultReasons.RESET_CONFIRMED);
            } catch (CodeMismatchException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.VERIFICATION_CODE_MISMATCH);
            } catch (ExpiredCodeException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.EXPIRED_VERIFICATION_CODE);
            } catch (InvalidParameterException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.INVALID_PARAMS);
            } catch (InvalidPasswordException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.INVALID_PASSWORD);
            } catch (UserNotFoundException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.NO_SUCH_USER);
            } catch (UserNotConfirmedException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.USER_NOT_CONFIRMED);
            } catch (TooManyFailedAttemptsException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.TOO_MANY_ATTEMPTS);
            } catch (TooManyRequestsException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.TOO_MANY_REQUESTS);
            }
        }
    }
}
