/********************************Dechateau******************************************/
package spring.jpa.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratorPersist {
	private final AtomicLong counter = new AtomicLong();

	public String generateCode() {
		return "USER0" + counter.incrementAndGet();
	}
}
