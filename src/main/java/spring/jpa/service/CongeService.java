package spring.jpa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.jpa.model.Conge;
import spring.jpa.repository.CongeRepository;

@Service
public class CongeService {
	@Autowired
	private CongeRepository congeRepository;

	public Page<Conge> getCongesSortPage(int pageNumber, int pageSize, String sortField, String sortDirection) {
		Sort sort = Sort.by(sortField);
		sort = sortDirection.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		return congeRepository.findAll(pageable);
	}

}
