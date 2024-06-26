package com.manuelr.banking.digital.onboarding.converter

import com.manuelr.banking.digital.onboarding.util.REALM_ACCESS
import com.manuelr.banking.digital.onboarding.util.ROLES
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class JwtAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities = extractAuthorities(jwt)
        return JwtAuthenticationToken(jwt, authorities)
    }

    private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
        val realmAccess = jwt.getClaim<Map<String, Any>>(REALM_ACCESS) ?: return emptyList()
        val roles = realmAccess[ROLES] as? List<*> ?: return emptyList()
        return roles.filterIsInstance<String>().map { SimpleGrantedAuthority(it) }
    }
}
