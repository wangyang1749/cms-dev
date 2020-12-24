package com.wangyang.authorize.jwt;

import com.wangyang.authorize.pojo.dto.SpringUserDto;
import com.wangyang.authorize.pojo.dto.CmsToken;
import com.wangyang.service.service.IOptionService;
import com.wangyang.pojo.enums.PropertyEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider  implements InitializingBean {
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String base64Secret;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;
    private final long tokenValidityRefreshInSeconds;
    private final long tokenValidityInSecondsRefreshForRememberMe;
    @Autowired
    IOptionService optionService;

    private Key key;


    public TokenProvider(
            @Value("${jwt.base64-secret}") String base64Secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            @Value("${jwt.token-validity-in-seconds-for-remember-me}") long tokenValidityInSecondsForRememberMe,
            @Value("${jwt.token-refresh-validity-in-seconds}")long tokenValidityRefreshInSeconds,
            @Value("${jwt.token-refresh-validity-in-seconds-for-remember-me}")long tokenValidityInSecondsRefreshForRememberMe) {
        this.base64Secret = base64Secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.tokenValidityInMillisecondsForRememberMe = tokenValidityInSecondsForRememberMe * 1000;
        this.tokenValidityRefreshInSeconds = tokenValidityRefreshInSeconds * 1000;
        this.tokenValidityInSecondsRefreshForRememberMe = tokenValidityInSecondsRefreshForRememberMe * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public CmsToken refreshToken(Authentication authentication,boolean rememberMe) {
        return generateToken(authentication,rememberMe,this.tokenValidityRefreshInSeconds,this.tokenValidityInSecondsRefreshForRememberMe);

    }
    public CmsToken createToken(Authentication authentication, boolean rememberMe){
        return generateToken(authentication,rememberMe,this.tokenValidityInMilliseconds,this.tokenValidityInMillisecondsForRememberMe);
    }

    public CmsToken generateToken(Authentication authentication, boolean rememberMe, long time, long rememberMeTime) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + rememberMeTime);
        } else {
            validity = new Date(now + time);
        }

        SpringUserDto springUserDto = (SpringUserDto) authentication.getPrincipal();
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("ID", springUserDto.getId())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
        return new CmsToken(token,validity.getTime());
    }

//    public boolean needRefresh(String token){
//        Claims claims = Jwts.parser()
//                .setSigningKey(key)
//                .parseClaimsJws(token)
//                .getBody();
//        Date expDate = claims.getExpiration();
//        long from = expDate.getTime()+1000*60;//1000*60 =1分钟
//        long to = new Date().getTime();
//        long diff = to - from;
//        if(diff<0){
//            return true;
//        }
//        return false;
//    }
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

//        User principal = new User(claims.getSubject(), "", authorities);
        SpringUserDto springUserDto = new SpringUserDto();
        springUserDto.setUsername(claims.getSubject());
        int id = (Integer)claims.get("ID");
        springUserDto.setId(id);
        Date expDate = claims.getExpiration();
        springUserDto.setExpDate(expDate);
        return new UsernamePasswordAuthenticationToken(springUserDto, token, authorities);
    }
    public Authentication getAuthenticationCustomize(String token) {
        Integer id = optionService.getPropertyIntegerValue(PropertyEnum.ADMIN_ID);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{"ROLE_ADMIN"})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        SpringUserDto springUserDto = new SpringUserDto();
        springUserDto.setId(id);
        springUserDto.setUsername("API请求");
        return new UsernamePasswordAuthenticationToken(springUserDto, token, authorities);
    }


    public boolean validateTokenCustomize(String token) {
        String value = optionService.getPropertyStringValue(PropertyEnum.CMS_TOKEN);
        if(token!=null&&token.equals(value)){
            return true;
        }
        return false;
    }
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
