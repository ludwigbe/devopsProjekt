package de.THM.devops.repository;

import de.THM.devops.dto.LoginDto;

public interface AuthService {

    String login(LoginDto loginDto);
}
