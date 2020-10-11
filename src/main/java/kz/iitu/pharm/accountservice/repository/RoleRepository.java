package kz.iitu.pharm.accountservice.repository;

import kz.iitu.pharm.accountservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByName(String role);
}
