package com.security.lab6.kms;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("!production")
@RestController
@RequestMapping("/kms")
@RequiredArgsConstructor
public class KmsController {

	private final IKmsService kmsService;

	@GetMapping
	public String encryptKey(@RequestParam String dek) {
		return kmsService.encryptDek(dek);
	}

}
