/********************************Dechateau******************************************/
package spring.jpa.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import spring.jpa.service.CodeGeneratorPersist;

@Component
public class UserEntityListener {
	private final CodeGeneratorPersist codeGeneratorService;

	@Lazy
	@Autowired
	public UserEntityListener(CodeGeneratorPersist codeGeneratorService) {
		this.codeGeneratorService = codeGeneratorService;
	}

	@PrePersist
	@PreUpdate
	public void prePersist(User user) {
		if (user.getCode() == null || user.getCode().isEmpty()) {
			user.setCode(codeGeneratorService.generateCode());
		}
	}
}
