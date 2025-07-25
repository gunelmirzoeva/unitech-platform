package com.unitech.auth_service.service;

public interface TokenBlacklistService {
    void blacklistToken(String token, long expirationMillis);
    boolean isTokenBlacklisted(String token);
}