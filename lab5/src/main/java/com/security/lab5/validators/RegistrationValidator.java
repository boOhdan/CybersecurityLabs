package com.security.lab5.validators;

import com.security.lab5.auth.dto.RegistrationForm;
import org.passay.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RegistrationValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return RegistrationForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		var userRegistration = (RegistrationForm) target;
		validatePassword(userRegistration.getPassword(), errors);
		if (!userRegistration.getPassword().equals(userRegistration.getRepeatPassword())) {
			errors.rejectValue("repeatPassword", null, "passwords not equal");
		}
	}

	private void validatePassword(String password, Errors errors) {
		PasswordValidator validator = new PasswordValidator(Arrays.asList(
				new LengthRule(8, 30),
				new UppercaseCharacterRule(1),
				new DigitCharacterRule(1),
				new SpecialCharacterRule(1),
				new AlphabeticalSequenceRule(3,false),
				new QwertySequenceRule(3,false),
				new WhitespaceRule()));
		var ruleResult = validator.validate(new PasswordData(password));

		if (!ruleResult.isValid()) {
			List<String> messages = validator.getMessages(ruleResult);
			String errorMessage = String.join(",", messages);
			errors.rejectValue("password", null, errorMessage);
		}
	}
}
