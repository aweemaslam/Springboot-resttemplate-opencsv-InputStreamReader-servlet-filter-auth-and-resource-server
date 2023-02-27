package com.hiring.assignment.oauth2.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    private final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"
            + "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAgJXDWYBKBe/VK0pA14D6\n"
            + "l8+bflnglz9jaGvew2d/XkJkewlcRovO8OsUQASEWhDRBArYm5tqQ3Iq5b+RhsEG\n"
            + "1B/0fKTkBrToDh7Foaj2PSLBAn6DelsiqdXdSHUTJcm0NhvXzKGssnjTNd5C7yvm\n"
            + "/uKBCUL0RaPRFOQDQLzgMq/s3BVIB9KxI4nucWWWrTPpJjU7d4cUAfqUC6TP8EM4\n"
            + "ZHyoRef13fdMUwiqXFl8K+hY5lfp6au8csqOs2XFirOE4Vv7sPCVzdr5c6YgW/eL\n"
            + "VLo+dng6x/nI6/D98N2CM+Pp55EKFmMgUeTWYB4HTck38QVtLQ0UMbJEIvJce3/V\n"
            + "0Yy0L8LvEyvKQGB/rsv+2uJNQma7zfweXVhwIGWATzvLyr6IKIReZAHfyvu3oFEX\n"
            + "kTBkeatAcDWPyFBgZgSZIv6H87eCyRtsJEyATBM3oUTr9niANdRCLtCUiDl7Dsx1\n"
            + "spVPpie3Uzbu0XDvuQuhzVLbzUSt/U27fo0DpXPZYGq4IMuX4ezOxKYN0sQKCzi2\n"
            + "eVu42DAYDvXcbeRPZJgygCb5mtCdoLoeD5i/TfeaPx9t/essHhBKC2qIHpQqUpQe\n"
            + "IOIdh5xlpvgaoKi1Iuec/uvXW4MRdYrLNuw4C8+CEV16uWDBGJcXkg80+XiiRy8C\n"
            + "3theGLtCB/2gJUXKtjg6qkcCAwEAAQ==\n" + "-----END PUBLIC KEY-----";
    private final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n"
            + "MIIJJwIBAAKCAgEAgJXDWYBKBe/VK0pA14D6l8+bflnglz9jaGvew2d/XkJkewlc\n"
            + "RovO8OsUQASEWhDRBArYm5tqQ3Iq5b+RhsEG1B/0fKTkBrToDh7Foaj2PSLBAn6D\n"
            + "elsiqdXdSHUTJcm0NhvXzKGssnjTNd5C7yvm/uKBCUL0RaPRFOQDQLzgMq/s3BVI\n"
            + "B9KxI4nucWWWrTPpJjU7d4cUAfqUC6TP8EM4ZHyoRef13fdMUwiqXFl8K+hY5lfp\n"
            + "6au8csqOs2XFirOE4Vv7sPCVzdr5c6YgW/eLVLo+dng6x/nI6/D98N2CM+Pp55EK\n"
            + "FmMgUeTWYB4HTck38QVtLQ0UMbJEIvJce3/V0Yy0L8LvEyvKQGB/rsv+2uJNQma7\n"
            + "zfweXVhwIGWATzvLyr6IKIReZAHfyvu3oFEXkTBkeatAcDWPyFBgZgSZIv6H87eC\n"
            + "yRtsJEyATBM3oUTr9niANdRCLtCUiDl7Dsx1spVPpie3Uzbu0XDvuQuhzVLbzUSt\n"
            + "/U27fo0DpXPZYGq4IMuX4ezOxKYN0sQKCzi2eVu42DAYDvXcbeRPZJgygCb5mtCd\n"
            + "oLoeD5i/TfeaPx9t/essHhBKC2qIHpQqUpQeIOIdh5xlpvgaoKi1Iuec/uvXW4MR\n"
            + "dYrLNuw4C8+CEV16uWDBGJcXkg80+XiiRy8C3theGLtCB/2gJUXKtjg6qkcCAwEA\n"
            + "AQKCAgA5Vg54j0ryoMHV/tMxBRM4tFsqmRsbdil2e3smeLUDq+kwL7lUv6y0Iq6x\n"
            + "6RG3M35wJgH2SPO2RWc5cRWMF/BakDEtjz0afHppKXGIp1W9ZwzXduBbo92uC24T\n"
            + "jBjQpcNUyU+NsJ3YKyZLA+Om0FW9W/Sb054mm2h0v0NwD4iNMYMVk+u9iUZxiWSo\n"
            + "bdslOluBLQVqQ/I4+6oeEwyhnue7bxZ9rXQl2MrQz7FfYouK7J0PgS4NgwWRzh6Z\n"
            + "ggz2zxu2fZSajnzOARFwbyhIufYH/tAxdWUInKnqA1jImY0gZEV9e2AbIXJc8gRh\n"
            + "caJiHhJyx6+qAgX6LvpQ9u8kJpmJprBiZguBVlGSOmdIf6gioU+T5xA3freim0i9\n"
            + "tPzTb01v2cJGn8LNVsYZieGbT/THcOwdDbFD0vUroNBfEg4Spbyrb7avdk4SIjI2\n"
            + "MqPjDpcDzS/qrKJd8jebyq3utTGYxaW1ReyYZZoVlvLpSPiZ3q6imof64MUT6LAo\n"
            + "U2UFFYSEan3Le5Yxd8OcWLrpORdTh6phZbj7tFQhZaD4XiwgyHQG2F0gJwWBftl6\n"
            + "AE5TlJjYyUPUtoFYvaYS6ftV2Nj9fODZLLlbjpHOx1vu+P45PXJz2b6FbIpbspii\n"
            + "9l1+l6/etzzdbbVnzoecC+yTf9FIJUPgctVnKA+NEQZRv9QbqQKCAQEA0Fd3dpMf\n"
            + "J/ajuCaGcRULrg3KNMpVn60kHSV/xpaO/90dINO6HX5YeTSjmWqiCq9c4nQt1KFI\n"
            + "lLCe89ug9HvovalF4CKiHbc6S8coI3tOlJx443ugvd+hmrXslJpeIcUjaVADTbMf\n"
            + "YyoIB6lV4BJTYSLl9hYDdJUJUqISfmI9HkkLfY607fMil66GC99pfPpXJHofTPS6\n"
            + "yuPsBxPSGigOBQlADhFwCBgjNAS9oStTE6dKn1Y9yEQjuc8c6IqgK+jw1dJBLmBm\n"
            + "akVSbar59uisF4C+v2akZTfhtPotvdTHCbFQI7Ftnd38QujJNlLs74gfYhvIWjlR\n"
            + "GwDFgTONdzndzQKCAQEAnf+6uJnyKMlrRIehqJEwPvW5XZOtpm4TZ9oKhoUcGfhW\n"
            + "4R0WFvgtuE/Ds5plmw5olowbFKZpmYWeQglW2MrHaJX+dVJEDH1BrDyRCZ8RBtNq\n"
            + "hKFHFyEU1MH0Bf72SHwjbYDZoe0FJGt7vig5UJmEVxLZ282nsDSQj98gHcJmypfo\n"
            + "DKgNKFTouakEGBsyWNp7fcY7ylWgUVeKY9bNrNejDabhQtCn1Uk5QuWjnuvYBVMt\n"
            + "C3o6Ehg4c2uWZqcDYE62SDYFuMRPZzuETldKcKMQWdsHf0XfF68H1k6PPpqmIgHS\n"
            + "yhz8f/nk0NlqcJN1pYDB+uf46SbVBm2bLLqGtL10YwKCAQBIup/rdsRjkQfqGxjH\n"
            + "lQ80vX+fGhr12N1Ih3SpgJl+3FN0yDSqYxbDiQqrXbwfesQ0EVaGaZ8KuPVkL4Fq\n"
            + "l27R9qt76NsrAHGLToTGjh8KZe0VjGy6m7ywY12pKpI9u7H264WbtDH6zgtrkUN3\n"
            + "Ky6MNECOvXSLWBOfExDdqbGoRtuKAy1J+5xR0wzcTCtpA4M0KqWOmcgzV4lnlxW7\n"
            + "J8xtGaOQxamUScjQOe9wuc0QiU/Ve6epp4/JJ3HyA6KScYjRO8qhQ+m8o2J5Ajys\n"
            + "YuDge4MbEdvFsdJK+SLKp+KrkYhmTWP0vi93tAe5vQ0VOTGrcANq66NSh9xqk/KI\n"
            + "clzpAoIBAG7xumiT2QIg5Uy6vJ5ETQ94tk/qOf5qvv+mkMuCvofR5Revt+orHNeW\n"
            + "UwJTwVMO3AwSl5V5gR9HPyh5rF0QjtN2t7YkLRpj/fB2mxWZd3hGjj5RjTzFgv4e\n"
            + "rz5imQeu+6WR28AjUgAP5VVSo2RgWBhYaVg54a0OTBBqif+7mThbUJtKieqvLLfp\n"
            + "4bEXr9PKnvpjb2qVk7xqbwfKNqROyvT0IKstHxzajPXXkHwweYDLtg6gJhS8oy8Q\n"
            + "9gxg7DL9MmwkWwIx/turZC0qiF/tcLbX4TzSkGPuCN/ITDeWml+4zg4UKo2mqLCq\n"
            + "al5gfTTY491OdG8rUFxyLItM8vgJLKcCggEADXsCpXSeGM8HEB51zs1EbeNDW9mm\n"
            + "bdJzJ1Jt9sOsZLuAx//z7UyyhnUrjGIvSBE0rfaiwpc0YyXo+a/pVgqgJPOky79q\n"
            + "nT3giXbUCvGDKCtpVoiELcvVj2xYCD88yRcPPR5/InoymE9tAFOYuquFO9tKIFKP\n"
            + "x6DZFyhPGE9x6I03eZ+m6r8JS9nLCJAYGAfM2Dm3OtZeW6XTORS0+K1cehUgS+TD\n"
            + "NdWO2NbgobveuA4bKs1juf8pQHr2srGOPN/6q4kY1aBuvpSBNAXxCmpV6jTjMc5S\n"
            + "fMIwOtyErihUM1eHgH9jIl7++HPfYnXJm6gLBMs5l1uWy5HVpLGIbU9ynQ==\n" + "-----END RSA PRIVATE KEY-----";
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(PRIVATE_KEY);
        converter.setVerifierKey(PUBLIC_KEY);
        return converter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
                .accessTokenConverter(tokenEnhancer());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client").secret(passwordEncoder.encode("client")).scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(20000)
                .refreshTokenValiditySeconds(20000);

    }

}