package org.ckxnhat.identity.service;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.identity.repository.RoleRepository;
import org.ckxnhat.identity.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-24 17:17:11.236
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
}
