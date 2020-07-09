package com.elfari.desafio.security;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;


public class AuthorizationUtil {
	
	private static Logger logger = LogManager.getLogger(AuthorizationUtil.class);

	static void addAuthentication(String username, HttpServletResponse response) {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.SECOND, SecurityConstants.SEC_TOKEN_EXPIRATION_TIME);
		Date after = calendar.getTime();

		String strNow = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(now);
		String strAfter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(after);
		
		response.addHeader("accessToken", makeToken(username, now, after));
		response.addHeader("tokenType", SecurityConstants.TOKEN_PREFIX);
		response.addHeader("expiresIn", String.valueOf(SecurityConstants.SEC_TOKEN_EXPIRATION_TIME));
		response.addHeader("issued", strNow != null ? strNow : StringUtils.EMPTY);
		response.addHeader("expires", strAfter != null ? strAfter : StringUtils.EMPTY);
	}

	private static String makeToken(String subject, Date issued, Date expires) {
		SignatureAlgorithm algoritimoAssinatura = SignatureAlgorithm.HS512;

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SecurityConstants.PRIVATE_KEY);

		SecretKeySpec key = new SecretKeySpec(apiKeySecretBytes, algoritimoAssinatura.getJcaName());

		JwtBuilder jwtBuilder = Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(issued)
				.setExpiration(expires)
				.signWith(algoritimoAssinatura, key);

		return jwtBuilder.compact();
	}

	static Authentication getAuthentication(String token) {
		Claims claims = decode(token);
		
		if (claims != null) {
			String user = claims.getSubject();
			
			if (user != null) {
				List<GrantedAuthority> grantedAuths = Collections.emptyList();
				return new UsernamePasswordAuthenticationToken(user, null, grantedAuths);
			}
		}

		return null;
	}

	private static Claims decode(String token) {
		Claims claims = null;
		
		try {
			claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SecurityConstants.PRIVATE_KEY))
					.parseClaimsJws(token).getBody();
		} catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
		
		return claims;
	}

}
