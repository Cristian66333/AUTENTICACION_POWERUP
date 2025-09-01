package co.com.powerup.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenResponse {
    private String tokenType;
    private String accessToken;
    private long expiresIn;
}
