package github.peaterpita.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import github.peaterpita.model.Staff;
import github.peaterpita.model.User;
import github.peaterpita.repository.StaffRepository;
import github.peaterpita.repository.UserRepository;
import github.peaterpita.security.JwtUtil;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final StaffRepository staffRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo, StaffRepository staffRepo, BCryptPasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.staffRepo = staffRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String attemptLogin(String username, String password) {
        Optional<User> userOpt = userRepo.findByUsername(username);

        if (userOpt.isEmpty())
            return null;

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPasswordHash()))
            return null;

        String role = staffRepo.findByUserId(user.getId()).map(Staff::getRole).orElse("STUDENT");
        return jwtUtil.generateToken(user.getUsername(), role);
    }
}
