package com.medcard.security;

import com.medcard.entities.User;
import com.medcard.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentUserFinder {

	private final UserRepository userRepository;

	public long getCurrentUserId() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		User user = userRepository.findByEmail(username);

		if (user != null) {
			return user.getId();
		} else {
			return -1;
		}
	}
}
