package ru.lightdigital.semenov.proc_sys.dto.security;

public record LoginRequest(
    String login,
    String password
) { }
