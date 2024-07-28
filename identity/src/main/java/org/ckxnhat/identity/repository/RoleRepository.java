package org.ckxnhat.identity.repository;

import org.ckxnhat.identity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-24 16:02:39.979
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
