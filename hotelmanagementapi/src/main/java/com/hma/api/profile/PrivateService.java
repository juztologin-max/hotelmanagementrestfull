package com.hma.api.profile;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PrivateService {
    private final ProfileRepo repo;

    public PrivateService(ProfileRepo repo) {
        this.repo = repo;
    }

    public Optional<Profile> getProfileWithId(Long id) {
        return repo.findById(id);
    }

}
