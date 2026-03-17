package com.Group117.hrm_system.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Group117.hrm_system.Repository.TaiKhoanRepository;
import com.Group117.hrm_system.entity.TaiKhoan;


public class UpgradingDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private final TaiKhoanRepository taiKhoanRepository;

    public UpgradingDaoAuthenticationProvider(PasswordEncoder passwordEncoder,
                                              UserDetailsService userDetailsService,
                                              TaiKhoanRepository taiKhoanRepository) {
        super(userDetailsService); // Spring Security 6+: DaoAuthenticationProvider requires UserDetailsService in constructor
        setPasswordEncoder(passwordEncoder);
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            super.additionalAuthenticationChecks(userDetails, authentication);
        } catch (BadCredentialsException ex) {
            // If password matching failed, compare with the stored value directly (e.g. when stored in plain text or contains trailing spaces)
            String presentedPassword = (authentication.getCredentials() == null) ? null : authentication.getCredentials().toString();
            String storedPassword = userDetails.getPassword();

            if (presentedPassword != null && storedPassword != null) {
                // Normalize whitespace to reduce errors coming from DB string padding/truncation
                String presentedTrimmed = presentedPassword.trim();
                String storedTrimmed = storedPassword.trim();

                if (presentedTrimmed.equals(storedTrimmed)) {
                    // Upgrade stored password to BCrypt so future logins work properly
                    if (!storedTrimmed.startsWith("$2a$") && !storedTrimmed.startsWith("$2b$") && !storedTrimmed.startsWith("$2y$")) {
                        TaiKhoan tk = taiKhoanRepository.findByUsername(userDetails.getUsername());
                        if (tk != null) {
                            tk.setPassword(getPasswordEncoder().encode(presentedTrimmed));
                            taiKhoanRepository.save(tk);
                        }
                    }
                    return; // treat as successful authentication
                }
            }
            throw ex;
        }
    }
}