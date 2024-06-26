package com.manuelr.banking.digital.onboarding.util

const val SECRET: String = "secret"
const val GRANT_TYPE: String = "grant_type"
const val PROVIDER: String = "provider"
const val REALM_ACCESS = "realm_access"
const val ROLES = "roles"
const val AUTH_BASE_URI: String = "\${app.config.api.auth.base-uri}"
const val AUTH_LOGIN_URI: String = "\${app.config.api.auth.login-uri}"
const val AUTH_REFRESH_TOKEN_URI: String = "\${app.config.api.auth.refresh-uri}"
const val AUTH_SIGNUP_URI: String = "\${app.config.api.auth.sign-up-uri}"
const val USERS_PATH = "/api/v1/users/"