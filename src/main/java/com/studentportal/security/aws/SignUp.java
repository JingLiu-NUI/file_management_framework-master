package com.studentportal.security.aws;

import com.amazonaws.services.cognitoidp.model.*;
import com.studentportal.user.UserRole;
import org.apache.commons.lang3.StringUtils;

public class SignUp {
    private static AwsCognito awsCognito = AwsCognito.getInstance();

    public static AwsCognitoResult doWork(String email, String given_name,
                                          String family_name, UserRole userRole) {
        if (StringUtils.isBlank(email)) {
            return new AwsCognitoResult(false, ResultReasons.FIELDS_INCOMPLETE);
        } else {
            try {
                AdminCreateUserRequest request = new AdminCreateUserRequest()
                        .withUserPoolId(awsCognito.getCognitoPoolId())
                        .withUsername(email)
                        .withUserAttributes(
                                new AttributeType()
                                        .withName("email")
                                        .withValue(email),
                                new AttributeType()
                                        .withName("given_name")
                                        .withValue(given_name),
                                new AttributeType()
                                        .withName("family_name")
                                        .withValue(family_name),
                                new AttributeType()
                                        .withName("custom:user_role")
                                        .withValue(userRole.name()),
                                new AttributeType()
                                        .withName("email_verified")
                                        .withValue("true"))
                        .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                        .withForceAliasCreation(Boolean.FALSE);

                awsCognito.getClient().adminCreateUser(request);
                return new AwsCognitoResult(true,
                        ResultReasons.TEMP_PASSWORD_SENT);
            } catch (InvalidParameterException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.INVALID_PARAMS);
            } catch (UsernameExistsException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.EMAIL_EXISTS);
            } catch (TooManyRequestsException e) {
                System.out.println(e.getLocalizedMessage());
                return new AwsCognitoResult(false,
                        ResultReasons.TOO_MANY_REQUESTS);
            }
        }
    }
}
