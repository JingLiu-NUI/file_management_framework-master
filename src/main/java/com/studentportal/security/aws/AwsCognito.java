package com.studentportal.security.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.nimbusds.jose.JWSObject;
import com.studentportal.security.auth.Constants;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Cookie;
import java.text.ParseException;

public class AwsCognito {

    private static AwsCognito awsCognito;

    private AWSCognitoIdentityProvider client;
    private AWSCredentialsProvider credentialsProvider;

    private String cognitoPoolId;
    private String cognitoClientId;
    private CredentialsCache tokenCache;

    private AwsCognito() {
        initCredentials();
        this.client = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.EU_WEST_1)
                .build();
        this.cognitoPoolId = "eu-west-1_7seGWwwII";
        this.cognitoClientId = "3sta5gq2elcu49s0u5mrk77bnq";
        this.tokenCache = new CredentialsCache(1000);
    }

    public static AwsCognito getInstance() {
        if (awsCognito == null) {
            return new AwsCognito();
        } else {
            return awsCognito;
        }
    }

    public AWSCognitoIdentityProvider getClient() {
        return client;
    }

    public String getCognitoPoolId() {
        return cognitoPoolId;
    }

    public String getCognitoClientId() {
        return cognitoClientId;
    }

    public CredentialsCache getTokenCache() {
        return tokenCache;
    }

    public void updateCredentialsCookies(AuthenticationResultType authResult) {
//        tokenCache.addToken(authResult.getAccessToken());

        System.out.println("*** ID TOKEN ***");
        try {
            JWSObject jwsObject = JWSObject.parse(authResult.getIdToken());
            JSONObject jsonObject = jwsObject.getPayload().toJSONObject();
            String email = jsonObject.getAsString("email");
            System.out.println(email);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("*****************************");

        Cookie accessTokenCookie = new Cookie(Constants.CookieNames.ACCESS_TOKEN, authResult.getAccessToken());
        if (!StringUtils.isBlank(authResult.getRefreshToken())) {
            Cookie resfreshTokenCookie = new Cookie(Constants.CookieNames.REFRESH_TOKEN, authResult.getRefreshToken());
        }
    }

    public void reportResult(String responseMessage) {
        System.out.println(responseMessage);
    }

    private void initCredentials() {
        this.credentialsProvider = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return new AWSCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return "AKIAIYJXJVW6M6CTC2YA";
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return "WbmYqPT4YpypsiBmlSbDio1CrFhOs1USvhH9lW9b";
                    }
                };
            }

            @Override
            public void refresh() {

            }
        };
    }
}
