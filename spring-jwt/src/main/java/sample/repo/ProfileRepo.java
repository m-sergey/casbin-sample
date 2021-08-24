package sample.repo;

import sample.model.Profile;

import java.util.Optional;

public class ProfileRepo {

    public Optional<Profile> find(String username) {
        Profile profile = new Profile();
        profile.setUsername(username);
        return Optional.of(profile);
    }
}
